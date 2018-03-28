package com.jwaoo.account.web.rest;

import com.jwaoo.account.config.Constants;
import com.jwaoo.account.dto.AcctClientDto;
import com.jwaoo.account.model.Account;
import com.jwaoo.account.sevice.*;
import com.jwaoo.account.util.ActiveEnum;
import com.jwaoo.account.utils.GenderEnum;
import com.jwaoo.account.utils.OauthTypeEnum;
import com.jwaoo.account.utils.UserStatusEnum;
import com.jwaoo.account.web.rest.dto.*;
import com.jwaoo.common.core.utils.LogUtils;
import com.jwaoo.common.core.utils.ResultCode;
import com.jwaoo.common.core.utils.StrUtil;
import com.jwaoo.common.core.words.NgWordUtil;
import com.jwaoo.common.security.domain.MyUserDetail;
import com.jwaoo.common.security.dto.HeaderDto;
import com.jwaoo.common.security.token.provider.Token;
import com.jwaoo.common.security.token.service.TokenService;
import com.jwaoo.common.security.web.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/v1")
public class AccountController extends BaseController
{

	private final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OauthService oauthService;


    /**
     * 获取授权Token
     * @throws Exception
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authorize(@Valid @RequestBody FormAuthorizeReqDto dto, BindingResult result) throws Exception
    {
        if (result.hasErrors())
        {
            return error(ResultCode.INVALID);
        }
        OauthTypeEnum type = OauthTypeEnum.getEnum(dto.getType());
        String sourceIp = getClientIpAddress();
        String country = getCountry();
        HeaderDto header = getHeader();
        if (type == null || type.getValue() == OauthTypeEnum.PLATFORM.getValue())
        {
            // 平台账号登录
            if ((!StrUtil.validEmail(dto.getAccount()) && !StrUtil.validFullPhone(dto.getAccount())) || !accountService.validPassword(dto.getPassword()))
            {
                return error(ResultCode.INVALID);
            }
            Token tk = null;
            MyUserDetail details = null;
            try {
                String md5 = accountService.md5(Constants.M5_SAL, dto.getAccount());
                MyUserRegisterDetail udt = RegisterService.getInstance().getRedisUserDetail(md5);
                if (udt != null) {
                    return error(ResultCode.NOT_VERIFIED);
                }
                String md5Pass = accountService.getMd5Passwd(dto.getPassword());
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.getAccount(), md5Pass);
                Authentication authentication = authenticationManager.authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                details = (MyUserDetail) authentication.getPrincipal();
                tk = TokenService.getInstance().getToken(details);
            } catch (LockedException lex)
            {
                //TODO valid temp pwd
                details = (MyUserDetail) lex.getExtraInformation();
                boolean res = accountService.validTempPwd(details.getUuid(), dto.getAccount(), dto.getPassword());
                if (!res)
                {
                    return error(ResultCode.ACCOUNT_ERROR);
                }
                tk = TokenService.getInstance().getTempToken(details);
            } catch (Exception e)
            {
                throw e;
            }
            accountService.replaceToken(header, tk);
            //保存登录日志
            accountService.saveLoginHistory(details.getId(), tk.getToken(), sourceIp, header);
            int perfect = Constants.REG_NOT_PERFECT;
            if (StringUtils.isNotBlank(details.getUsername()) && StringUtils.isNotBlank(details.getAvatar()) && (details.getGender() == GenderEnum.MALE.getValue() || details.getGender() == GenderEnum.FEMALE.getValue()))
            {
                perfect = Constants.REG_PERFECT;
            }
            return success(new TokenDto(tk.getToken(), perfect, !details.isAccountNonLocked() ? Constants.ACCOUNT_LOCKED : Constants.ACCOUNT_UNLOCKED));
        } else
        {
            //第三方登录
            if (StringUtils.isBlank(dto.getAccessToken()) || StringUtils.isBlank(dto.getOpenId()))
            {
                return error(ResultCode.INVALID);
            }

            Map<String, Object> json = null;
            if (type.getValue() == OauthTypeEnum.GOOGLE.getValue())
            {
                // google oauth
                json = oauthService.googleLogin(dto, header, sourceIp, country);
            } else if(type.getValue() == OauthTypeEnum.FACEBOOK.getValue())
            {
                // facebook oauth
                json = oauthService.facebookLogin(dto, header, sourceIp, country);
            } else if(type.getValue() == OauthTypeEnum.TWITTER.getValue())
            {
                // twitter oauth
                json = oauthService.twitterLogin(dto, header, sourceIp, country);
            }

            if (Boolean.valueOf(json.get("res").toString()))
            {
                //TODO 保存登录日志
                accountService.saveLoginHistory(Long.valueOf(json.get("id").toString()), json.get("token").toString(), sourceIp, header);
                //TODO 返回用户token
                return success(new TokenDto(json.get("token").toString(), (int) json.get("perfect"), (int)json.get("locked")));
            } else
            {
                return error((ResultCode) json.get("code"));
            }
        }
    }

    /**
     * POST /register -> register the user.
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterReqDto dto, BindingResult result)
    {
        try {
            if (result.hasErrors()) {
                return error(ResultCode.INVALID);
            }
            String sourceIp = getClientIpAddress();
            String country = getCountry();
            HeaderDto header = getHeader();
//            if (StringUtils.isNotBlank(country) && country.equals("CN")){
//                log.error("country {}, is not allowed", country);
//                return error(ResultCode.NO_SERVICE);
//            }
            if ((!StrUtil.validEmail(dto.getAccount()) && !StrUtil.validFullPhone(dto.getAccount())) || !accountService.validPassword(dto.getPassword()))
            {
                return error(ResultCode.INVALID);
            }
            if (dto.getGender() != null) {
                GenderEnum genderEnum = GenderEnum.getEnum(dto.getGender());
                if (genderEnum == null) {
                    return error(ResultCode.INVALID);
                }
            }
            //TODO 敏感词校验，昵称唯一性校验
            if (NgWordUtil.getInstance().isContentKeyWords(dto.getNickname()))
            {
                return error(ResultCode.NGWORD_ERROR);
            }
            if (StringUtils.isNotBlank(dto.getBirthday())) {
                try {
                    LocalDate.parse(dto.getBirthday());
                } catch (Exception ex)
                {
                    return error(ResultCode.INVALID);
                }
            }
            Account user = accountService.findByAccount(dto.getAccount());
            if (user != null) {
                return error(ResultCode.EXISTS_ACCOUNT);
            }
            // 查询redis
            String key = accountService.md5(Constants.M5_SAL, dto.getAccount());
            MyUserRegisterDetail userDet = RegisterService.getInstance().getRedisUserDetail(key);
            if (userDet != null) {
                return error(ResultCode.EXISTS_ACCOUNT);
            }
            userDet = new MyUserRegisterDetail(dto.getAccount(), dto.getPassword());
            userDet.setArea(dto.getArea());
            userDet.setClientId(header.getClient_id());
            userDet.setChannelId(header.getChannel_id());
            userDet.setOsType(header.getOs_type());
            userDet.setOsVersion(header.getOs_version());
            userDet.setHardWare(header.getHard_ware());
            userDet.setVersion(header.getClient_version());
            userDet.setDeviceToken(header.getDevice_no());
            userDet.setSourceIp(sourceIp);
            userDet.setCountry(country);
            userDet.setGender(dto.getGender());
            userDet.setNickname(dto.getNickname());
            userDet.setAvatar(dto.getAvatar());
            userDet.setBirthday(dto.getBirthday());
            userDet.setUserAgent(header.getUser_agent());
            if (StrUtil.validEmail(dto.getAccount())) {
                userDet.setEmail(dto.getAccount());
            }else {
                userDet.setPhone(dto.getAccount());
            }
            userDet = accountService.saveUserInfoToRedis(userDet);
            if (userDet != null) {
                return success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return error(ResultCode.API_ERROR);
    }

    /**
     * 注册mail验证
     */
    @RequestMapping(value = "/registration/verification/email", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView getRegiseterVerificationEmail(@RequestParam String code)
    {
        ModelAndView mv = new ModelAndView();
        if (!StrUtil.validM5Str(code))
        {
            mv.addObject("status", "403");
            mv.setViewName("verification_failed");
            mv.addObject("message", "Forbidden");
            return mv;
        }
        try {
            // 如果为空时间过期提醒用户得新获取
            MyUserRegisterDetail userDet = RegisterService.getInstance().getRedisUserDetail(code);
            if (userDet == null) {
                mv.addObject("status", "403");
                mv.setViewName("expiration_time");
                mv.addObject("message", "This link has already expired,Please acquire it again");
                return mv;
            }
            Account user = accountService.findByAccount(userDet.getAccount());
            if (user != null) {
                mv.addObject("status", "200");
                mv.setViewName("successfully");
                mv.addObject("message", "You have already validated your Email. Injoy SenseLovers APP!");
                return mv;
            }
            //TODO 保存注册信息
            boolean res = accountService.registerUser(userDet);
            // 验证成功
            if (res) {
                RegisterService.getInstance().del(code);
                mv.addObject("status", "200");
                mv.setViewName("successfully");
                mv.addObject("message", "Your email has been successfully verified. Now, go log into the app and enjoy!");
                return mv;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mv.addObject("status", "404");
        mv.setViewName("verification_failed");
        mv.addObject("message", "The network is busy, please try again later");
        return mv;
    }

    /**
     * 重新获取验证码
     */
    @RequestMapping(value = "/again/verification/email", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> againGetVerificationEmail(@Valid @RequestBody ForgotReqDto dto, BindingResult result)
    {
        if (result.hasErrors()) {
            return error(ResultCode.INVALID);
        }
        if (!StrUtil.validEmail(dto.getAccount())) {
            return error(ResultCode.INVALID);
        }
        try {
            String redisKey = accountService.md5(Constants.M5_SAL, dto.getAccount());
            MyUserRegisterDetail userDet = RegisterService.getInstance().getRedisUserDetail(redisKey);
            if (userDet != null) {
                MailService.getInstance().sendVerificationCodeToUserEmail(userDet.getAccount(), redisKey);
                return success();
            }else {
                Account user = accountService.findByAccount(dto.getAccount());
                if (user != null) {
                    // 账号已经存在
                    return error(ResultCode.EXISTS_ACCOUNT);
                }
                return error(ResultCode.FORBIDDEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return error(ResultCode.API_ERROR);
    }

    /**
     * 忘记密码 生成一个临时密码
     */
    @RequestMapping(value = "/forgot/password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> forgetPassword(@Valid @RequestBody ForgotReqDto dto, BindingResult result)
    {
        if (result.hasErrors()) {
            return error(ResultCode.INVALID);
        }
        // 邮件地址不能为空
        if (!StrUtil.validEmail(dto.getAccount()) && !StrUtil.validFullPhone(dto.getAccount())){
            return error(ResultCode.INVALID);
        }
        try {
            Account user = accountService.findByAccount(dto.getAccount());
            if (user == null) {
                return error(ResultCode.NOT_EXISTS_ACCOUNT);
            } else if (user.getStatus() == UserStatusEnum.DISABLE.getValue() || (user.getFreezeEndTime()!=null && user.getFreezeEndTime().getTime() > System.currentTimeMillis()))
            {
                return error(ResultCode.INVALID_ACCOUNT);
            }
            // 不为空
            boolean res = accountService.forgotPassword(user);
            if (res) {
                return success();
            }
            return error(ResultCode.FAIL);
        } catch (Exception e) {
            LogUtils.log4Error("Forgot password has been successfully processed", e);
            return error(ResultCode.API_ERROR);
        }
    }

    /**
     * is exists account
     */
    @RequestMapping(value = "/exists", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> exists(@Valid @RequestBody ForgotReqDto dto, BindingResult result) throws Exception
    {
        if ((!StrUtil.validEmail(dto.getAccount()) && !StrUtil.validFullPhone(dto.getAccount())))
        {
            return error(ResultCode.INVALID);
        }
        String key = accountService.md5(Constants.M5_SAL, dto.getAccount());
        MyUserRegisterDetail userDet = RegisterService.getInstance().getRedisUserDetail(key);
        if (userDet != null) {
            return success(ResultCode.EXISTS_ACCOUNT);
        }
        Account user = accountService.findByAccount(dto.getAccount());
        if (user != null) {
            return success(ResultCode.EXISTS_ACCOUNT);
        }
        return success(ResultCode.NOT_EXISTS_ACCOUNT);
    }

    @RequestMapping(value = "/refresh/active/{uid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshActive(@PathVariable("uid") String uid, @RequestBody ActiveReqDto dto, BindingResult result)
    {
        if (result.hasErrors()) {
                return error(ResultCode.INVALID);
            }
        try {
            if (!dto.validActive() || StringUtils.isBlank(uid) || !dto.validClientId()) {
                return error(ResultCode.INVALID);
            }
            ActiveEnum activeType = ActiveEnum.getEnum(dto.getActive());
            if (activeType == null) {
                return error(ResultCode.INVALID);
            }
            //TODO
            AcctClientDto acctClientDto = AcctClientService.getInstance().findByUuid(uid, dto.getClientId());
            if (acctClientDto == null && NumberUtils.isNumber(uid)) {
                acctClientDto = AcctClientService.getInstance().findByAccountId(Long.valueOf(uid), dto.getClientId());
            }
            if (acctClientDto == null) {
                return error(ResultCode.INVALID);
            }
            acctClientDto.setActive(activeType.getValue());
            acctClientDto.setLast_active_time(new Date());
            AcctClientService.getInstance().update(acctClientDto);
            return success();
        } catch (Exception e) {
            e.printStackTrace();
            return error(ResultCode.API_ERROR);
        }
    }

}
