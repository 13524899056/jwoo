package com.jwaoo.account.sevice;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.jwaoo.account.utils.UserStatusEnum;
import com.jwaoo.common.core.config.Global;
import com.jwaoo.common.core.utils.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jwaoo.account.config.Constants;
import com.jwaoo.account.model.Account;
import com.jwaoo.account.model.UserRegister;
import com.jwaoo.account.utils.GenderEnum;
import com.jwaoo.account.utils.OauthTypeEnum;
import com.jwaoo.account.web.rest.dto.FormAuthorizeReqDto;
import com.jwaoo.common.security.dto.HeaderDto;
import com.jwaoo.common.security.token.provider.Token;

/**
 * @author Jerry
 * @date 2017/5/15
 */
@Service
public class OauthService {

    private static String GOOGLE_URL = Global.getConfigCfg("oauth:google.url", "https://www.googleapis.com/");
    private static String FACEBOOK_URL = Global.getConfigCfg("oauth:fb.url", "https://graph.facebook.com/");
    private static String TWITTER_URL = Global.getConfigCfg("oauth:tw.url", "https://api.twitter.com/");

    private static String FB_CONSUMER_KEY = Global.getConfigCfg("oauth:fb.key", "1920951494841027");
    private static String FB_CONSUMER_SECRET = Global.getConfigCfg("oauth:fb.secret", "497eee54bf34556581a7364636379a76");

    private static String TW_CONSUMER_KEY = Global.getConfigCfg("oauth:tw.key", "aDJ3woAjfcCyr1YMHg133OIGU");
    private static String TW_CONSUMER_SECRET = Global.getConfigCfg("oauth:tw.secret", "UGskQvq1idmCGcDWYg4pQIYF79lPXu9eKoxiYwEakJTDMY5Qr1");

    @Autowired
    private UserRegisterService userRegisterService;

    @Autowired
    private AccountService accountService;

    /**
     * Google 第三方授权登录
     * @param dto
     * @param header
     * @param ip
     * @param area
     * @return
     */
    public Map<String, Object> googleLogin(FormAuthorizeReqDto dto, HeaderDto header, String ip, String area) throws Exception
    {
        Map<String, Object> rst = new HashMap<String, Object>();
        rst.put("res", false);
        rst.put("code", ResultCode.API_ERROR);
        try
        {
            //TODO 验证token
            String resStr = HttpClientUtils.getGetResponse(GOOGLE_URL + "oauth2/v3/tokeninfo?id_token=" + dto.getAccessToken(), null);
            if (StringUtils.isNotBlank(resStr))
            {
                JSONObject json = JSON.parseObject(resStr);
                if (!json.containsKey("error_description") && dto.getOpenId().equalsIgnoreCase(json.getString("sub")))
                {
                    //TODO 第三方账号是否验证
                    Boolean emailVerfied = json.getBoolean("email_verified");
                    if (emailVerfied == null || !emailVerfied)
                    {
                        //TODO email未验证
                        LogUtils.log4Error(" google auth email not verfied error res : " + resStr);
                        rst.put("code", ResultCode.NOT_VERIFIED);
                    } else
                    {
                        //TODO 是否存在平台
                        UserRegister userRegister = userRegisterService.findByOpenId(dto.getOpenId(), OauthTypeEnum.GOOGLE);
                        if (userRegister == null)
                        {
//                            Map<String, Object> header = new HashMap<>();
//                            header.put("Authorization", "Bearer " + accessToken);
//                            resStr = HttpClientUtils.getGetResponse(GOOGLE_URL + "oauth2/v1/userinfo", header);
//                            if (StringUtils.isNotBlank(resStr))
//                            {
//
//                            }
                            userRegister = new UserRegister();
                            userRegister.setClientId(header.getClient_id());
                            userRegister.setChannelId(header.getChannel_id());
                            userRegister.setDeviceNo(header.getDevice_no());
                            userRegister.setHardWare(header.getHard_ware());
                            userRegister.setOsType(OsType.getEnum(header.getOs_type()) != null ? OsType.getEnum(header.getOs_type()).getValue() : null);
                            userRegister.setOsVersion(header.getOs_version());
                            userRegister.setClientVersion(header.getClient_version());
                            userRegister.setOauthType(OauthTypeEnum.GOOGLE.getValue());
                            userRegister.setOpenId(dto.getOpenId());
                            userRegister.setArea(area);
                            userRegister.setSourceIp(ip);
                            String uuid = StrUtil.getUUID();
                            userRegister.setUuid(uuid);
                            userRegister.setRegisterStatus(Constants.REG_NOT_PERFECT);
                            boolean res = userRegisterService.saveSelective(userRegister);
                            if (res)
                            {
                                //TODO 添加用户
                                Account user = new Account();
                                user.setUuid(uuid);
//                            user.setAvatar(json.getString("picture"));
//                            user.setEmail(json.getString("email"));
//                            user.setLoginName(json.getString("email"));
//                            user.setName(json.getString("email"));
//                            user.setName(json.getString("name"));
                                user.setNickname(json.getString("name"));
                                user.setAreaCode(area);
                                res = accountService.saveUser(user);
                                if (res)
                                {
                                    //TODO 登录生成token及授权
                                    Token token = dologin(user.getId(), header);
                                    rst.put("res", true);
                                    rst.put("id", user.getId());
                                    rst.put("token", token.getToken());
                                    rst.put("perfect", Constants.REG_NOT_PERFECT);
                                    rst.put("locked", Constants.ACCOUNT_UNLOCKED);
                                }
                            }
                        } else
                        {
                            //TODO 是否有效用户
                            Account user = accountService.findByUuid(userRegister.getUuid());
                            if (user != null && user.getStatus() != UserStatusEnum.DISABLE.getValue() && (user.getFreezeEndTime() == null || user.getFreezeEndTime().getTime()<System.currentTimeMillis()))
                            {
                                //TODO 已有账号进行登录授权
                                Token token = dologin(user.getId(), header);
                                rst.put("res", true);
                                rst.put("id", user.getId());
                                rst.put("token", token.getToken());
                                int perfect;
                                if (StringUtils.isBlank(user.getNickname()) || StringUtils.isBlank(user.getAvatar()) || user.getGender() == null || user.getGender() == GenderEnum.UNKNOWN.getValue()) {
                                    perfect = Constants.REG_NOT_PERFECT;
                                }else
                                {
                                    perfect = Constants.REG_PERFECT;
                                }
                                rst.put("perfect", perfect);
                                rst.put("locked", user.getStatus() == UserStatusEnum.LOCKED.getValue()?Constants.ACCOUNT_LOCKED:Constants.ACCOUNT_UNLOCKED);
                            } else
                            {
                                LogUtils.log4Error(" google account invalid error res : " + resStr);
                                rst.put("code", ResultCode.INVALID_ACCOUNT);
                            }
                        }
                    }
                } else
                {
                    LogUtils.log4Error(" google auth error res : " + resStr);
                    rst.put("code", ResultCode.TOKEN_INVALID);
                }
            } else
            {
                LogUtils.log4Error(" google auth error res : " + resStr);
                rst.put("code", ResultCode.TOKEN_INVALID);
            }
        } catch (Exception e)
        {
            throw e;
        }
        return rst;
    }


    /**
     * Facebook 第三方登录
     * @param dto
     * @return
     */
    public Map<String, Object> facebookLogin(FormAuthorizeReqDto dto, HeaderDto header, String ip, String area) throws Exception
    {
        Map<String, Object> rst = new HashMap<String, Object>();
        rst.put("res", false);
        rst.put("code", ResultCode.API_ERROR);
        //TODO 验证token
        String url = FACEBOOK_URL + "debug_token?input_token=" + dto.getAccessToken()+"&access_token=" + URLEncoder.encode(FB_CONSUMER_KEY + "|" + FB_CONSUMER_SECRET, Constants.DEFAULT_CHARSET.toString());
        String resStr = HttpClientUtils.getGetResponse(url, null);
        if (StringUtils.isNotBlank(resStr))
        {
            JSONObject json = JSON.parseObject(resStr);
            if (!json.containsKey("error") && dto.getOpenId().equalsIgnoreCase(json.getJSONObject("data").getString("user_id")))
            {
                Boolean isValid = json.getJSONObject("data").getBoolean("is_valid");
                if(!isValid)
                {
                    //TODO email未验证
                    LogUtils.log4Error(" facebook auth email not verfied error res : " + resStr);
                    rst.put("code", ResultCode.NOT_VERIFIED);
                }else
                {
                    //TODO 是否存在平台
                    UserRegister userRegister = userRegisterService.findByOpenId(dto.getOpenId(), OauthTypeEnum.FACEBOOK);
                    if (userRegister == null)
                    {
                        resStr = HttpClientUtils.getGetResponse(FACEBOOK_URL + "me?fields=name,email,gender,picture,cover,age_range,verified,locale,timezone&access_token=" + dto.getAccessToken(), null);
                        if (StringUtils.isNotBlank(resStr))
                        {
                            json = JSON.parseObject(resStr);
                            userRegister = new UserRegister();
                            userRegister.setClientId(header.getClient_id());
                            userRegister.setChannelId(header.getChannel_id());
                            userRegister.setDeviceNo(header.getDevice_no());
                            userRegister.setHardWare(header.getHard_ware());
                            userRegister.setOsType(OsType.getEnum(header.getOs_type()) != null ? OsType.getEnum(header.getOs_type()).getValue() : null);
                            userRegister.setOsVersion(header.getOs_version());
                            userRegister.setClientVersion(header.getClient_version());
                            userRegister.setOauthType(OauthTypeEnum.FACEBOOK.getValue());
                            userRegister.setOpenId(dto.getOpenId());
                            userRegister.setArea(area);
                            userRegister.setSourceIp(ip);
                            String uuid = StrUtil.getUUID();
                            userRegister.setUuid(uuid);
                            userRegister.setRegisterStatus(Constants.REG_NOT_PERFECT);
                            boolean res = userRegisterService.saveSelective(userRegister);
                            if (res)
                            {
                                //TODO 添加用户
                                Account user = new Account();
//                            user.setAvatar(json.getString("picture"));
//                                user.setEmail(json.getString("email"));
                                user.setNickname(json.getString("name"));
                                user.setUuid(uuid);
//                                user.setName(json.getString("name"));
//                            user.setName(json.getString("email"));
                                user.setGender(Integer.valueOf(json.getString("gender")));
                                user.setAreaCode(area);
                                res = accountService.saveUser(user);
                                if (res)
                                {
                                    //TODO 登录生成token及授权
                                    Token token = dologin(user.getId(), header);
                                    rst.put("res", true);
                                    rst.put("id", user.getId());
                                    rst.put("token", token.getToken());
                                    rst.put("perfect", Constants.REG_NOT_PERFECT);
                                    rst.put("locked", Constants.ACCOUNT_UNLOCKED);
                                }
                            }
                        }
                    } else
                    {
                        //TODO 是否有效用户
                        Account user = accountService.findByUuid(userRegister.getUuid());
                        if (user != null && user.getStatus() != UserStatusEnum.DISABLE.getValue() && (user.getFreezeEndTime() == null || user.getFreezeEndTime().getTime()<System.currentTimeMillis()))
                        {
                            //TODO 已有账号进行登录授权
                            Token token = dologin(user.getId(), header);
                            rst.put("res", true);
                            rst.put("id", user.getId());
                            rst.put("token", token.getToken());
                            int perfect;
                            if (StringUtils.isBlank(user.getNickname()) || StringUtils.isBlank(user.getAvatar()) || user.getGender() == null || user.getGender() == GenderEnum.UNKNOWN.getValue()) {
                                perfect = Constants.REG_NOT_PERFECT;
                            }else
                            {
                                perfect = Constants.REG_PERFECT;
                            }
                            rst.put("perfect", perfect);
                            rst.put("locked", user.getStatus() == UserStatusEnum.LOCKED.getValue()?Constants.ACCOUNT_LOCKED:Constants.ACCOUNT_UNLOCKED);
                        } else
                        {
                            LogUtils.log4Error(" facebook account invalid error res : " + resStr);
                            rst.put("code", ResultCode.INVALID_ACCOUNT);
                        }
                    }
                }
            } else
            {
                LogUtils.log4Error(" facebook auth error res : " + resStr);
                rst.put("code", ResultCode.TOKEN_INVALID);
            }
        } else
        {
            LogUtils.log4Error(" facebook auth error res : " + resStr);
            rst.put("code", ResultCode.TOKEN_INVALID);
        }
        return rst;
    }

    /**
     * Twitter 第三方登录
     * @param dto
     * @return
     */
    public Map<String,Object> twitterLogin(FormAuthorizeReqDto dto, HeaderDto header, String ip, String area) throws Exception
    {
        Map<String, Object> rst = new HashMap<String, Object>();
        rst.put("res", false);
        rst.put("code", ResultCode.API_ERROR);
        if (StringUtils.isNotBlank(dto.getAccessToken()) && dto.getAccessToken().indexOf(":")>5 && dto.getAccessToken().indexOf("@")>6) {
            String oauthToken = dto.getAccessToken().substring(0, dto.getAccessToken().indexOf(":"));
            String oauthSecrt = dto.getAccessToken().substring(dto.getAccessToken().indexOf(":"), dto.getAccessToken().indexOf("@"));
            String consumerKey = dto.getAccessToken().substring(dto.getAccessToken().indexOf("@")+1);
            //TODO 验证token
            String oauth_nonce = UUID.randomUUID().toString().replaceAll("-", ""); // any relatively random alphanumeric string will work here
            String oauth_timestamp = System.currentTimeMillis() / 1000 + "";
            String parameter_string = "include_email=true&oauth_consumer_key=" + consumerKey + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method=HMAC-SHA1" +
                    "&oauth_timestamp=" + oauth_timestamp + "&oauth_token=" + encode(oauthToken) + "&oauth_version=1.0";
            String signature_base_string = "GET&" + encode("https://api.twitter.com/1.1/account/verify_credentials.json") + "&" + encode(parameter_string);
            String oauth_signature = computeSignature(signature_base_string, TW_CONSUMER_SECRET + "&" + encode(oauthSecrt));
            String resStr = HttpClientUtils.getGetResponse(TWITTER_URL + "1.1/account/verify_credentials.json?" + parameter_string + "&oauth_signature=" + oauth_signature, null);
            if (StringUtils.isNotBlank(resStr))
            {
                JSONObject json = JSON.parseObject(resStr);
                if (!json.containsKey("error") && dto.getOpenId().equalsIgnoreCase(json.getString("id_str")))
                {
                    Boolean isValid = json.getBoolean("verified");
                    if (!isValid) {
                        //TODO email未验证
                        LogUtils.log4Error(" twitter auth email not verfied error res : " + resStr);
                        rst.put("code", ResultCode.NOT_VERIFIED);
                    } else {
                        //TODO 是否存在平台
                        UserRegister userRegister = userRegisterService.findByOpenId(dto.getOpenId(), OauthTypeEnum.TWITTER);
                        if (userRegister == null) {
                            resStr = HttpClientUtils.getGetResponse(TWITTER_URL + "me?access_token=" + dto.getAccessToken(), null);
                            if (StringUtils.isNotBlank(resStr)) {
                                json = JSON.parseObject(resStr);
                                userRegister = new UserRegister();
                                userRegister.setClientId(header.getClient_id());
                                userRegister.setChannelId(header.getChannel_id());
                                userRegister.setDeviceNo(header.getDevice_no());
                                userRegister.setHardWare(header.getHard_ware());
                                userRegister.setOsType(OsType.getEnum(header.getOs_type()) != null ? OsType.getEnum(header.getOs_type()).getValue() : null);
                                userRegister.setOsVersion(header.getOs_version());
                                userRegister.setClientVersion(header.getClient_version());
                                userRegister.setOauthType(OauthTypeEnum.TWITTER.getValue());
                                userRegister.setOpenId(dto.getOpenId());
                                userRegister.setArea(area);
                                userRegister.setSourceIp(ip);
                                String uuid = StrUtil.getUUID();
                                userRegister.setUuid(uuid);
                                userRegister.setRegisterStatus(Constants.REG_NOT_PERFECT);
                                boolean res = userRegisterService.saveSelective(userRegister);
                                if (res)
                                {
                                    //TODO 添加用户
                                    Account user = new Account();
//                                    user.setName(json.getString("name"));
                                    user.setUuid(uuid);
                                    user.setNickname(json.getString("name"));
                                    user.setGender(Integer.valueOf(json.getString("gender")));
                                    user.setAreaCode(area);
                                    res = accountService.saveUser(user);
                                    if (res)
                                    {
                                        //TODO 登录生成token及授权
                                        Token token = dologin(user.getId(), header);
                                        rst.put("res", true);
                                        rst.put("id", user.getId());
                                        rst.put("token", token.getToken());
                                        rst.put("perfect", Constants.REG_NOT_PERFECT);
                                        rst.put("locked", Constants.ACCOUNT_UNLOCKED);
                                    }
                                }
                            }
                        } else {
                            //TODO 是否有效用户
                            Account user = accountService.findByUuid(userRegister.getUuid());
                            if (user != null && user.getStatus() != UserStatusEnum.DISABLE.getValue() && (user.getFreezeEndTime() == null || user.getFreezeEndTime().getTime()<System.currentTimeMillis())) {
                                //TODO 已有账号进行登录授权
                                Token token = dologin(user.getId(), header);
                                rst.put("res", true);
                                rst.put("id", user.getId());
                                rst.put("token", token.getToken());
                                int perfect;
                                if (StringUtils.isBlank(user.getNickname()) || StringUtils.isBlank(user.getAvatar()) || user.getGender() == null || user.getGender() == GenderEnum.UNKNOWN.getValue()) {
                                    perfect = Constants.REG_NOT_PERFECT;
                                }else
                                {
                                    perfect = Constants.REG_PERFECT;
                                }
                                rst.put("perfect", perfect);
                                rst.put("locked", user.getStatus() == UserStatusEnum.LOCKED.getValue()?Constants.ACCOUNT_LOCKED:Constants.ACCOUNT_UNLOCKED);
                            } else {
                                LogUtils.log4Error(" twitter account invalid error res : " + resStr);
                                rst.put("code", ResultCode.INVALID_ACCOUNT);
                            }
                        }
                    }
                } else {
                    LogUtils.log4Error(" twitter auth error res : " + resStr);
                    rst.put("code", ResultCode.TOKEN_INVALID);
                }
            } else {
                LogUtils.log4Error(" twitter auth error res : " + resStr);
                rst.put("code", ResultCode.TOKEN_INVALID);
            }
        } else {
            rst.put("code", ResultCode.TOKEN_INVALID);
        }
        return rst;
    }

    /**
     * 登录并生成token
     * @param accountId
     * @param header
     * @return
     * @throws Exception
     */
    private Token dologin(Long accountId, HeaderDto header) throws Exception
    {
        Token token = accountService.reLogin(accountId);
        if (token != null) {
            //剔除上次登录的token
            accountService.replaceToken(header, token);
        }
        return token;
    }

    public String encode(String value) throws Exception
    {
        String encoded = URLEncoder.encode(value, "UTF-8");
        StringBuilder buf = new StringBuilder(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%' && (i + 1) < encoded.length() && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }

    /**
     * gen twitter signature
     * @param baseString
     * @param keyString
     * @return
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    private static String computeSignature(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException
    {
        byte[] keyBytes = keyString.getBytes();
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);

        byte[] text = baseString.getBytes();

        return new String(Base64.encodeBase64(mac.doFinal(text))).trim();
    }

}
