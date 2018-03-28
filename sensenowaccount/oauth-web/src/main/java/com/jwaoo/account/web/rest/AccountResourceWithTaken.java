package com.jwaoo.account.web.rest;

import com.alibaba.fastjson.JSONObject;
import com.jwaoo.account.config.Constants;
import com.jwaoo.account.dto.AcctClientDto;
import com.jwaoo.account.model.Account;
import com.jwaoo.account.sevice.AccountDtoUtil;
import com.jwaoo.account.sevice.AccountService;
import com.jwaoo.account.sevice.AcctClientService;
import com.jwaoo.account.util.ActiveEnum;
import com.jwaoo.account.utils.GenderEnum;
import com.jwaoo.account.web.rest.dto.*;
import com.jwaoo.common.core.utils.*;
import com.jwaoo.common.core.words.NgWordUtil;
import com.jwaoo.common.security.dto.HeaderDto;
import com.jwaoo.common.security.token.provider.Token;
import com.jwaoo.common.security.token.service.TokenService;
import com.jwaoo.common.security.web.PreTokenAndIpCheck;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/v1")
public class AccountResourceWithTaken extends PreTokenAndIpCheck {

	@Autowired
	private AccountService accountService;


	/**
	 * GET /account -> get the current user.
	 */
	@RequestMapping(value = "/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAccount(@ModelAttribute("TokenCheck")Token token) {
		try {
            Account user = accountService.findById(token.getId());
            if (user == null) {
                return error(ResultCode.TOKEN_INVALID);
            }
            UserDTO userDto = BeanMapDozer.map(user, UserDTO.class);
            return success(userDto);
        } catch (Exception e) {
            e.printStackTrace();
            return error(ResultCode.API_ERROR);
        }
	}

    /**
     * 获取用户列表
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> list(@ModelAttribute("TokenCheck")Token token) {
		try {
//            List<Account> users = accountService.findAll(10, 0);
            List<Account> users = accountService.findAll(token.getId());
            List<UserDTO> list = users.parallelStream().map(user -> BeanMapDozer.map(user, UserDTO.class)).collect(Collectors.toList());
//            List<UserDTO> list = new ArrayList<UserDTO>();
//            users.stream().forEach(user -> list.add(BeanMapDozer.map(user, UserDTO.class)));
            return success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return error(ResultCode.API_ERROR);
        }
	}

	/**
	 * GET /account -> get the current user.
	 */
	@RequestMapping(value = "/user/{uid}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserInfo(@ModelAttribute("TokenCheck")Token token, @PathVariable("uid") String uid)
    {
		try {
            if (StringUtils.isBlank(uid)) {
                return error(ResultCode.INVALID);
            }
            Account user = null;
            if (NumberUtils.isNumber(uid)) {
                user = accountService.findById(Long.valueOf(uid));
            } else {
                user = accountService.findByUuid(uid);
            }
            if (user == null) {
                return error(ResultCode.INVALID);
            }
            UserDTO userDto = BeanMapDozer.map(user, UserDTO.class);
            return success(userDto);
        } catch (Exception e) {
            e.printStackTrace();
            return error(ResultCode.API_ERROR);
        }
	}

	/**
	 * POST /account -> update the current user information.
	 * @throws java.io.IOException
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> modify(@ModelAttribute("TokenCheck")Token token, @Valid @RequestBody ModifyUserReqDto dto, BindingResult result)
    {
        try {
            if (result.hasErrors())
            {
                return error(ResultCode.INVALID);
            }
            if (StringUtils.isBlank(dto.getNickname()) && StringUtils.isBlank(dto.getAvatar()) && StringUtils.isBlank(dto.getAddress()) && StringUtils.isBlank(dto.getInstr()) && StringUtils.isBlank(dto.getBirthday()) && dto.getGender() == null)
            {
                return error(ResultCode.INVALID);
            }
            if (dto.getGender() != null) {
                GenderEnum genderEnum = GenderEnum.getEnum(dto.getGender());
                if (genderEnum == null) {
                    return error(ResultCode.INVALID);
                }
            }
            if (StringUtils.isNotBlank(dto.getBirthday()))
            {
                try {
                    LocalDate birthday = LocalDate.parse(dto.getBirthday());
                    if (birthday.isBefore(LocalDate.now(ZoneId.of(DateUtil.ZONE_DEFAULT)).minusYears(Constants.USER_MIX_AGE)))
                    {
                        dto.setBirthdayDate(birthday);
                    }else
                    {
                        return error(ResultCode.INVALID);
                    }
                } catch (Exception ex)
                {
                    return error(ResultCode.INVALID);
                }

            }
            //TODO 敏感词校验，昵称唯一性校验
            if (NgWordUtil.getInstance().isContentKeyWords(dto.getNickname() + dto.getAddress() + dto.getInstr()))
            {
                return error(ResultCode.NGWORD_ERROR);
            }
            if (StringUtils.isNotBlank(dto.getNickname()))
            {
                Account account = accountService.findByNickname(dto.getNickname(), token.getId());
                if (account != null)
                {
                    return error(ResultCode.EXISTS_ACCOUNT);
                }
            }
            boolean res = accountService.update(token.getId(), dto);
            if (res)
            {
//            UserDTO userDto = BeanMapDozer.map(user, UserDTO.class);
                return success();
            }else
            {
                return error(ResultCode.FAIL);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return error(ResultCode.API_ERROR);
        }
	}

    /**
     * 用户搜索
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> search(@ModelAttribute("TokenCheck") Token token, @Valid @RequestBody SearchReqDto dto, BindingResult result)
    {
        try
        {
            if (result.hasErrors())
            {
                return error(ResultCode.INVALID);
            }
            //TODO 敏感词校验，昵称唯一性校验
            if (NgWordUtil.getInstance().isContentKeyWords(dto.getQuery()))
            {
                return error(ResultCode.NGWORD_ERROR);
            }
//            String sourceIp = getClientIpAddress();
//            String country = getCountry();
//            HeaderDto header = getHeader();
            return success(AccountDtoUtil.getInstance().search(token.getId(), dto.getQuery(), dto.getNum()));
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return error(ResultCode.API_ERROR);
        }
    }

    /**
     * 附近用户
     */
    @RequestMapping(value = "/nearby", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> nearby(@ModelAttribute("TokenCheck") Token token, @Valid @RequestBody NearByReqDto dto, BindingResult result)
    {
        try
        {
            if (result.hasErrors())
            {
                return error(ResultCode.INVALID);
            }
            HeaderDto header = getHeader();
            AcctClientDto acctClientDto = new AcctClientDto(token.getId(), null, header.getClient_id(), null, null, new Double[]{dto.getLongitude(), dto.getLatitude()}, ActiveEnum.ACTIVED.getValue());
            List<AcctClientDto> list = AcctClientService.getInstance().findByNear(acctClientDto, dto.getDistance()==null? Constants.getDistance():dto.getDistance(), dto.getNum()==null?Constants.getPageSize():dto.getNum());
            return success(AcctClientService.getInstance().convertGeoResult(list));
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return error(ResultCode.API_ERROR);
        }
    }

    /**
     * 更新设备推送ID或位置信息
     */
    @RequestMapping(value = "/active", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> active(@ModelAttribute("TokenCheck") Token token, @RequestBody ActiveReqDto dto, BindingResult result)
    {
        try {
            if (result.hasErrors() || ((!dto.validPushId() || (StringUtils.isNotBlank(dto.getVoipId()) && !dto.validVoipId())) && (!dto.validLongitude() || !dto.validLatitude())))
            {
                return error(ResultCode.INVALID);
            }
//            String sourceIp = getClientIpAddress();
//            String country = getCountry();
            HeaderDto header = getHeader();
            OsType osType = OsType.getEnum(header.getOs_type());
            AcctClientDto acctClientDto = AcctClientService.getInstance().findByAccountId(token.getId(), header.getClient_id());
            if (acctClientDto == null)
            {
                return error(ResultCode.INVALID);
            }
            if (dto.validPushId())
            {
                acctClientDto.setPush_token(dto.getPushId());
                acctClientDto.setVoip_token(dto.validVoipId()?dto.getVoipId():null);
                acctClientDto.setDevice_no(header.getDevice_no());
                acctClientDto.setOs_type(osType!=null?osType.getValue():null);
                acctClientDto.setOs_version(header.getOs_version());
                acctClientDto.setVersion(header.getClient_version());
                acctClientDto.setLast_token_time(new Date());
            }
            if (dto.validLongitude() && dto.validLatitude())
            {
                acctClientDto.setLoc(new Double[]{dto.getLongitude(), dto.getLatitude()});
                acctClientDto.setUpdate_time(new Date());
            }
            AcctClientService.getInstance().update(acctClientDto);
            // TODO clear old push bind
            AcctClientService.getInstance().clearPushToken(new AcctClientDto(acctClientDto.get_id(), null, header.getClient_id(), osType.getValue(), dto.getPushId(), null));
            return success();
        } catch (Exception e) {
            e.printStackTrace();
            return error(ResultCode.API_ERROR);
        }
    }

    /**
     * 申请认证
     */
    @RequestMapping(value = "/verify", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verify(@ModelAttribute("TokenCheck") Token token, @Valid @RequestBody VerifyReqDto dto, BindingResult result) throws Exception
    {
        if (result.hasErrors() || !StrUtil.validHttpUrl(dto.getImgUrl()) || !StrUtil.validHttpUrl(dto.getAudioUrl()))
        {
            return error(ResultCode.INVALID);
        }
        boolean res = accountService.applyVerify(token.getId(), dto.getImgUrl(), dto.getAudioUrl());
        if (res)
        {
            return success();
        } else
        {
            return error(ResultCode.FAIL);
        }
    }

    /**
     * check token
     */
    @RequestMapping(value = "/checkToken", method = RequestMethod.GET)
    public ResponseEntity<?> checkToken(@ModelAttribute("TokenCheck")Token token)
    {
        if (token != null) {
            JSONObject json = new JSONObject();
            json.put("uid", token.getId());
            //TODO add blacklist
            json.put("blacklist", new ArrayList<>());
            return success(json);
        } else {
            return error(ResultCode.TOKEN_INVALID);
        }
    }

	/**
	 * POST /change_password -> changes the current user's password
	 * @throws java.io.IOException
	 */
	@RequestMapping(value = "/password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> changePassword(@ModelAttribute("TokenCheck")Token token, @Valid @RequestBody ChangePasswordReqDto dto, BindingResult result) throws IOException
    {
		try {
            if (result.hasErrors())
            {
                return error(ResultCode.INVALID);
            }
            if (!accountService.validPassword(dto.getOrgPassword()) || !accountService.validPassword(dto.getNewPassword()))
            {
                return error(ResultCode.INVALID);
            }
            boolean isOk = accountService.changePassword(token.getId(), dto.getOrgPassword(), dto.getNewPassword());
            if (!isOk)
            {
                return error(ResultCode.INVALID);
            }

            // 重新登录，返回accesstoken
            Token accessToken = accountService.reLogin(token.getId());
            if (accessToken != null)
            {
//                String sourceIp = getClientIpAddress();
//                String country = getCountry();
                HeaderDto header = getHeader();
                //剔除上次登录的token
                accountService.replaceToken(header, accessToken);
            }
            int perfect = Constants.REG_NOT_PERFECT;
            if (StringUtils.isNotBlank(accessToken.getNickname()) && StringUtils.isNotBlank(accessToken.getAvatar()) && (accessToken.getGender() == GenderEnum.MALE.getValue() || accessToken.getGender() == GenderEnum.FEMALE.getValue()))
            {
                perfect = Constants.REG_PERFECT;
            }
            return success(new TokenDto(accessToken.getToken(), perfect));
        } catch (Exception e) {
            e.printStackTrace();
            return error(ResultCode.API_ERROR);
        }
	}


    /**
     * sign out
     */
	@RequestMapping(value = "/signout", method = RequestMethod.GET)
	public ResponseEntity<?> logout(@ModelAttribute("TokenCheck")Token token) throws Exception
    {
		TokenService.getInstance().clearToken(token.getToken());
        HeaderDto header = getHeader();
        OsType osType = OsType.getEnum(header.getOs_type());
        // TODO clear old push bind
        AcctClientService.getInstance().clearPushToken(new AcctClientDto(null, null, header.getClient_id(), osType.getValue(), null, token.getToken()));
		// 返回 access token
		return success();
	}

    @RequestMapping(value = "/data/ul/replace", method = RequestMethod.GET)
    public ResponseEntity<?> dataReplace(@ModelAttribute("TokenCheck")Token token) throws Exception
    {
        if (Constants.isTestMode()) {
            accountService.updateAccList();
        }
        return success();
    }

//	@RequestMapping(value = "/refreshToken", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	@Timed
//	public ResponseEntity<?> tokenLease(@ModelAttribute("TokenCheckGetTokenString")String tokenstring) {
//        try {
//            Token token = accountService.reLogin(SecurityUtils.getCurrentUserId());
//            return success(token.getToken());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return error(ResultCode.API_ERROR);
//        }
//	}
	
}

