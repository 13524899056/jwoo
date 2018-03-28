package com.jwaoo.account.sevice;

import com.jwaoo.account.config.Constants;
import com.jwaoo.account.dto.AccountDto;
import com.jwaoo.account.dto.AcctClientDto;
import com.jwaoo.account.dto.ForgetPwdDto;
import com.jwaoo.account.dto.VerifiesApplyDto;
import com.jwaoo.account.mapper.*;
import com.jwaoo.account.model.*;
import com.jwaoo.account.util.AESSecurityUtil;
import com.jwaoo.account.util.ActiveEnum;
import com.jwaoo.account.utils.OauthTypeEnum;
import com.jwaoo.account.utils.UserStatusEnum;
import com.jwaoo.account.utils.VerifiedStatusEnum;
import com.jwaoo.account.web.rest.dto.ModifyUserReqDto;
import com.jwaoo.account.web.rest.dto.MyUserRegisterDetail;
import com.jwaoo.common.core.utils.DateUtil;
import com.jwaoo.common.core.utils.OsType;
import com.jwaoo.common.core.utils.StrUtil;
import com.jwaoo.common.security.domain.MyUserDetail;
import com.jwaoo.common.security.dto.HeaderDto;
import com.jwaoo.common.security.token.provider.Token;
import com.jwaoo.common.security.token.service.TokenService;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Cryptos;
import org.springside.modules.utils.Digests;
import org.springside.modules.utils.Encodes;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class AccountService {

    private final Logger log = LoggerFactory.getLogger(AccountService.class);
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AccountMapper accountMapper;
    
    @Autowired
    private AuthorityMapper authorityMapper;
    
    @Autowired
    private UserRegisterMapper userRegisterMapper;

    @Autowired
    private LoginHistoryMapper loginHistoryMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private ForgetPwdService forgetPwdService;

    @Autowired
    private VipHistoryMapper vipHistoryMapper;

    @Autowired
    private VerifiesApplyMapper verifiesApplyMapper;


    /**
     * 注册用户.
     * @param udt
     * @return
     * @throws Exception 
     */
    public boolean registerUser(MyUserRegisterDetail udt) throws Exception
    {
        boolean res = false;
        if (udt != null)
        {
            UserRegister userRegister = new UserRegister();
            userRegister.setClientId(udt.getClientId());
            userRegister.setChannelId(udt.getChannelId());
            userRegister.setDeviceNo(udt.getDeviceToken());
            userRegister.setHardWare(udt.getHardWare());
            userRegister.setOsType(StringUtils.isNotBlank(udt.getOsType()) && OsType.getEnum(udt.getOsType()) != null ? OsType.getEnum(udt.getOsType()).getValue() : null);
            userRegister.setSourceIp(udt.getSourceIp());
            userRegister.setArea(udt.getCountry());
            userRegister.setRegisterStatus(Constants.REG_NOT_PERFECT);
            userRegister.setOsVersion(udt.getOsVersion());
            userRegister.setClientVersion(udt.getVersion());
            userRegister.setOauthType(OauthTypeEnum.PLATFORM.getValue());
            String uuid = StrUtil.getUUID();
            userRegister.setUuid(uuid);
            // 保存注册信息
            int rct = userRegisterMapper.insertSelective(userRegister);
            if (rct > 0)
            {
                Authority authority = authorityMapper.findByName("ROLE_USER");
                Set<Authority> authorities = new HashSet<>();
                Account user = new Account();
                user.setNickname(StringUtils.isNotBlank(udt.getNickname()) ? udt.getNickname() : udt.getAccount());
                user.setEmail(StringUtils.isNotBlank(udt.getEmail()) ? udt.getEmail() : "");
                user.setPhone(StringUtils.isNotBlank(udt.getPhone()) ? udt.getPhone() : "");
                user.setAreaCode(udt.getArea());
                user.setUuid(uuid);
                if (udt.getGender() != null)
                {
                    user.setGender(udt.getGender());
                }
                if (StringUtils.isNotBlank(udt.getAvatar()))
                {
                    user.setAvatar(udt.getAvatar());
                }
                if (StringUtils.isNotBlank(udt.getBirthday()))
                {
                    user.setBirthday(LocalDate.parse(udt.getBirthday()));
                }

                String encryptedPassword = passwordEncoder.encode(getMd5Passwd(udt.getPassword()));
                user.setPassword(encryptedPassword);
                user.setLevel(Constants.DEFAULT_LEVEL.toString());
                authorities.add(authority);
                //TODO 保存用户信息
                rct = accountMapper.insertSelective(user);
                if(rct > 0)
                {
                    rct = userInfoMapper.insertSelective(new UserInfo(user.getId()));
                    AccountDto accountDto = accountMapper.selectAccountInfo(user.getId());
                    AccountDtoUtil.getInstance().save(accountDto);
                }
            }
            res = rct > 0 ? true : false;
        }
        return res;
    }

	/**
	 * 忘记密码
	 *
	 * @param acct
	 * @return
	 * @throws Exception
	 */
	public boolean forgotPassword(Account acct) throws Exception
    {

        String newpwd = null;
        boolean res = false;
        // is exists create temp pwd
        ForgetPwdDto fp = forgetPwdService.findByUid(acct.getUuid());
        if (acct.getStatus() == UserStatusEnum.LOCKED.getValue() && fp != null)
        {
            newpwd = decryptPwdBySmsCode(fp.getPwd());
            res = true;
        }else
        {
            newpwd = StrUtil.randomString(6);
            byte pb[] = AESSecurityUtil.encrypt(newpwd.getBytes(), Constants.getAuthPwdKey().getBytes());
            String passaes = Hex.encodeHexString(pb);

            String pwdMd5 = getMd5Passwd(passaes); // hash 密码，和app上传的密码保持一致
            String newPassword = passwordEncoder.encode(pwdMd5);
            Account user = new Account();
            user.setId(acct.getId());
            user.setPassword(newPassword);
            user.setStatus(UserStatusEnum.LOCKED.getValue());
            // 生成6位随机密码进行更新
            res = update(user);
            // save temp pwd
            forgetPwdService.save(acct.getUuid(), new ForgetPwdDto(acct.getId(), passaes, acct.getEmail(), System.currentTimeMillis()+(ForgetPwdService.FGP_EXPIRES*1000)));
        }

		if (res)
        {
            // 如查更新成功发送密码到用户邮箱
            MailService.getInstance().sendForgotPassword(acct, acct.getEmail(), newpwd);
            return res;
		}
		return false;
	}

    /**
     * 校验临时密码
     * @param key
     * @param loginName
     * @param pwd
     * @return
     */
    public boolean validTempPwd(String key, String loginName, String pwd)
    {
        boolean res = false;
        ForgetPwdDto fp = forgetPwdService.findByUid(key);
        if (fp != null)
        {
            if (pwd.equals(fp.getPwd()) && loginName.equals(fp.getLoginName()) && System.currentTimeMillis()<fp.getExpires())
            {
                res = true;
            }
        }
        return res;
    }


    /**
     * 更新用户.
     */
    public boolean update(Long accountId, ModifyUserReqDto dto) throws Exception
    {
        Account model = new Account();
        if (StringUtils.isNotBlank(dto.getNickname()))
        {
        	model.setNickname(dto.getNickname());
        }
        if (StringUtils.isNotBlank(dto.getAvatar()))
        {
        	model.setAvatar(dto.getAvatar());
        }
        if (dto.getGender() != null)
        {
        	model.setGender(dto.getGender());
//        	model.setGender(dto.getGender().getValue());
        }
        if (dto.getBirthday() != null)
        {
            model.setBirthday(dto.getBirthdayDate());
        }
        if (StringUtils.isNotBlank(dto.getAddress()))
        {
            model.setAddress(dto.getAddress());
        }
        if (StringUtils.isNotBlank(dto.getInstr()))
        {
            model.setInstr(dto.getInstr());
        }
        model.setId(accountId);
        boolean res = update(model);
        if (res)
        {
            updateAccountInfo(model);
        }
        return res;
    }

//    /**
//     * 重置密码
//     *
//     * @param loginName
//     * @param password
//     * @param verificationCode
//     * @return
//     * @throws ValidateCodeMisException
//     * @throws java.io.IOException
//     */
//    public boolean resetPassword(String loginName, String password, String verificationCode) throws Exception
//    {
//        // 短信MD5获取短信明文
//       String code = MailVerificationCodeProvider.getInstance().validateCode(verificationCode);
//        if (code == null) {
//            throw new ValidateCodeMisException();
//        }
//
//        Account currentUser = findByAccount(loginName);
//        if (currentUser == null) {
//            return false;
//        }
//
//		Boolean changePw = changePw(password, currentUser.getId());
//
//		log.debug("Changed password for User: {}", currentUser);
//		return changePw;
//	}

    /**
     * 修改密码.
     *
     * @param orgPassword -- 原密码MD5
     * @param newPassword -- 对等加密之后的新密码
     * @return
     * @throws java.io.IOException
     */
    public boolean changePassword(Long uid, String orgPassword, String newPassword) throws Exception
    {
        Account currentUser = findById(uid);
        if (currentUser == null)
        {
            return false;
        }
        //TODO
        String md5Pass = this.getMd5Passwd(orgPassword);
        // 原密码是否匹配
        if (!passwordEncoder.matches(md5Pass, currentUser.getPassword()))
        {
        	log.error("changepassword failt, org pass is not matched");
            return false;
        }
        Boolean changePw = changePw(newPassword, currentUser.getId());
        log.debug("Changed password for User: {}", currentUser);
        if (changePw)
        {
            forgetPwdService.remove(currentUser.getUuid());
        }
        return changePw;
    }

//    /**
//     * 取得带权限的用户信息.
//     *
//     * @return
//     */
//    public Account getUserWithAuthorities() throws Exception
//    {
//        return getUserWithAuthorities(SecurityUtils.getCurrentUserId());
//    }

//    /**
//     * 取得带权限的用户信息.
//     *
//     * @return
//     */
//    public Account getUserWithAuthorities(Long userId) throws Exception
//    {
//        Account currentUser = findById(userId);
//        if (currentUser != null && !currentUser.equals(""))
//        {
//            currentUser.getAuthorities().size(); // eagerly load the association
//        }
//        return currentUser;
//    }

//    public Account getUserInfo(Long userId) throws Exception
//    {
//    	Account user = findById(userId);
//    	if (user != null && !user.equals(""))
//        {
//    		user.getAuthorities().size(); // eagerly load the association
//        }
//        return user;
//    }

    public Token reLogin(Long loginId) throws Exception
    {
        Account userDb = findById(loginId);
        if (userDb != null)
        {
            MyUserDetail details = getAuths(userDb);
            PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(details, details, details.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(token);
        /* Id 没有变，覆盖redis中的原来的 key-value条目 */
            Token accessToken = TokenService.getInstance().getToken(details);
            if (accessToken != null)
            {
                return accessToken;
            }
        }
        return null;
    }

    /**
     * 密码校验
     * @param password
     * @return
     * @throws Exception
     */
    public boolean validPassword(String password) throws Exception
    {
        boolean res = false;
        if (StrUtil.validPwd(password))
        {
            String orgPwd = decryptPwdBySmsCode(password);
            if (StringUtils.isNotBlank(orgPwd) && orgPwd.trim().length()>=6 && orgPwd.trim().length()<=15)
            {
                res = true;
            }
        }
        return res;
    }

    private MyUserDetail getAuths(Account account) throws Exception
    {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        for (Authority authority : account.getAuthorities())
//        {
//            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
//            grantedAuthorities.add(grantedAuthority);
//        }
        return new MyUserDetail(account.getId(), account.getNickname(), account.getPassword(), account.getUuid(), account.getGender(), account.getAreaCode(), account.getAvatar(), account.getLevel(),grantedAuthorities);
    }

    public String getMd5Passwd(String password) throws Exception
    {
        String pwdOrg = decryptPwdBySmsCode(password);
        return md5(pwdOrg, Constants.M5_SAL);
    }

    private String decryptPwdBySmsCode(String pwd)
    {
    	String s = Cryptos.aesDecrypt(Encodes.decodeHex(pwd), Constants.getAuthPwdKey().getBytes(Constants.DEFAULT_CHARSET));
        return new String(s);
    }

    public String md5(String pwdOrg, String sal) throws Exception
    {
        StringBuilder sb = new StringBuilder(sal);
        InputStream is1 = new ByteArrayInputStream(pwdOrg.getBytes(Constants.DEFAULT_CHARSET));
        sb.append(Constants.getAuthPwdToken()).append(Encodes.encodeHex(Digests.md5(is1)));
        InputStream is2 = new ByteArrayInputStream(sb.toString().getBytes(Constants.DEFAULT_CHARSET));
        byte[] md5 = Digests.md5(is2);
        return Encodes.encodeHex(md5);
    }


    private Boolean changePw(String newPassword, Long uid) throws Exception
    {
//        String pwdOrg = decryptPwdBySmsCode(newPassword); // 解密密码
//        String pwdMd5 = getMd5Passwd(newPassword); // hash 密码，和app上传的密码保持一致
//        String encryptedPassword = passwordEncoder.encode(pwdMd5);
        String encryptedPassword = passwordEncoder.encode(getMd5Passwd(newPassword));
        Account model = new Account();
        model.setId(uid);
        model.setPassword(encryptedPassword);
        model.setStatus(UserStatusEnum.NORMAL.getValue());
        return update(model);
    }
    

    /**
     * 保存注册信息到缓存
     * @param myUserRegisterDetail
     * @return
     */
    public MyUserRegisterDetail saveUserInfoToRedis(MyUserRegisterDetail myUserRegisterDetail) throws Exception
    {
        if (myUserRegisterDetail != null)
        {
            String key = md5(Constants.M5_SAL, myUserRegisterDetail.getAccount());
            myUserRegisterDetail = RegisterService.getInstance().saveUserRegisterDetailToRedis(key, myUserRegisterDetail);
            if (myUserRegisterDetail != null)
            {
                myUserRegisterDetail = RegisterService.getInstance().getRedisUserDetail(key);
            }
            if (myUserRegisterDetail != null)
            {
                // 邮箱注册用户
                if (StringUtils.isNotBlank(myUserRegisterDetail.getEmail()))
                {
                    MailService.getInstance().sendVerificationCodeToUserEmail(myUserRegisterDetail.getAccount(), key);
                }else
                {
                    // 手机注册用户
                }
                return myUserRegisterDetail;
            }
        }
        return null;
    }

    /**
     * 根据登陆名查询
     * @param account
     * @return
     * @throws Exception
     */
	public Account findByAccount(String account) throws Exception
    {
        Account model = null;
        if (StringUtils.isNotBlank(account))
        {
            model = accountMapper.findByAccount(account);
        }
		return model;
	}

    public List<Account> findAll(Long uid) throws Exception
    {
        List<Account> list = accountMapper.findAll();
        if (uid != null && uid.longValue() > 0)
        {
            list.removeIf(obj -> obj.getId().longValue() == uid.longValue());
        }
        return list;
    }

//    public List<Account> findAll(Integer size, Integer limit) throws Exception
//    {
//        return accountMapper.findAllList(size, limit);
//    }
//
//    public long countAll() throws Exception
//    {
//        return accountMapper.findAllCount();
//    }

    /**
     * 根据UUID查询用户信息
     * @param uuid
     * @return
     * @throws Exception
     */
	public Account findByUuid(String uuid) throws Exception
    {
        Account model = null;
        if (StringUtils.isNotBlank(uuid))
        {
            model = accountMapper.findByUuid(uuid);
        }
		return model;
	}

    /**
     * 根据ID查询用户信息
     * @param id
     * @return
     */
	public Account findById(Long id) throws Exception
    {
        Account user = null;
        if (id != null)
        {
            user = accountMapper.findById(id);
        }
		return user;
	}

    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return
     * @throws Exception
     */
	public Account findByPhone(String phone, Long uid) throws Exception
    {
        Account model = null;
        if (StringUtils.isNotBlank(phone))
        {
            model = accountMapper.findByPhone(phone, uid);
        }
		return model;
	}

    /**
     * 根据Email查询
     * @param email
     * @return
     * @throws Exception
     */
	public Account findByEmail(String email, Long uid) throws Exception
    {
		Account model = null;
		if (StringUtils.isNotBlank(email))
        {
            model = accountMapper.findByEmail(email, uid);
		}
		return model;
	}

    /**
     * 根据用户昵称查询用户信息
     * @param nickname
     * @param uid
     * @return
     * @throws Exception
     */
	public Account findByNickname(String nickname, Long uid) throws Exception
    {
		Account model = null;
		if (StringUtils.isNotBlank(nickname))
        {
            model = accountMapper.findByNickname(nickname, uid);
		}
		return model;
	}

    /**
     * 更新用户信息
     * @param user
     * @return
     */
	public boolean update(Account user)
    {
        boolean res = false;
        if (user != null)
        {
            int ct = accountMapper.updateByPrimaryKeySelective(user);
            res = ct>0?true:false;
        }
        return res;
	}

    /**
     * 保存用户信息
     * @param user
     * @return
     */
	public boolean saveUser(Account user) throws Exception
    {
        boolean res = false;
        if (user != null)
        {
            int ct = accountMapper.insertSelective(user);
            res = ct>0?true:false;
        }
		return res;
	}

    /**
     * replace token
     * @param header
     * @param token
     */
    public void replaceToken(HeaderDto header, Token token) throws Exception
    {
        AcctClientDto dto = AcctClientService.getInstance().findByAccountId(token.getId(), header.getClient_id());
        if (dto != null && !token.equals(dto.getToken()))
        {
            TokenService.getInstance().clearToken(dto.getToken());
            dto.setToken(token.getToken());
            dto.setChannel_id(header.getChannel_id());
            dto.setDevice_no(header.getDevice_no());
            dto.setOs_type(OsType.getEnum(header.getOs_type())!=null?OsType.getEnum(header.getOs_type()).getValue():null);
            dto.setOs_version(header.getOs_version());
            dto.setVersion(header.getClient_version());
            AcctClientService.getInstance().update(dto);
        } else
        {
            dto = new AcctClientDto();
            dto.setAccount_id(token.getId());
            dto.setUuid(token.getUuid());
            dto.setClient_id(header.getClient_id());
            dto.setChannel_id(header.getChannel_id());
            dto.setDevice_no(header.getDevice_no());
            dto.setOs_type(OsType.getEnum(header.getOs_type()) != null ? OsType.getEnum(header.getOs_type()).getValue() : null);
            dto.setOs_version(header.getOs_version());
            dto.setVersion(header.getClient_version());
            dto.setToken(token.getToken());
            dto.setActive(ActiveEnum.ACTIVED.getValue());
            AcctClientService.getInstance().save(dto);
        }
    }

    /**
     * save login history
     * @param accountId
     * @param token
     * @param sourceIp
     * @param dto
     * @return
     * @throws Exception
     */
    public boolean saveLoginHistory(Long accountId, String token, String sourceIp, HeaderDto dto) throws Exception
    {
        LoginHistory model = new LoginHistory();
        model.setUid(accountId);
        model.setClientId(dto.getClient_id());
        model.setChannelId(dto.getChannel_id());
        model.setOsType(OsType.getEnum(dto.getOs_type())!=null?OsType.getEnum(dto.getOs_type()).getValue():null);
        model.setVersion(dto.getClient_version());
        model.setHardWare(dto.getHard_ware());
        model.setToken(token);
        model.setSourceIp(sourceIp);
        int ct = loginHistoryMapper.insertSelective(model);
        return ct>0 ? true : false;
    }

    /**
     * 修改账号信息
     * @param account
     * @return
     */
    public void updateAccountInfo(Account account) throws Exception
    {
        AccountDto accountDto = accountMapper.selectAccountInfo(account.getId());
        AccountDtoUtil.getInstance().update(accountDto);
        // clear token info
        if ((account.getStatus() != null && account.getStatus() == UserStatusEnum.DISABLE.getValue()) || (account.getFreezeEndTime() != null && account.getFreezeEndTime().after(new Date())))
        {
            List<AcctClientDto> list = AcctClientService.getInstance().findByUuidOrAccountId(null, account.getId());
            list.parallelStream().forEach(obj -> TokenService.getInstance().clearToken(obj.getToken()));
        }
    }

    public UserInfo findUserInfoByUid(Long accountId)
    {
        return userInfoMapper.findByUid(accountId);
    }

    public boolean updateUserInfo(UserInfo userInfo) throws Exception
    {
        int cnt = userInfoMapper.updateByPrimaryKeySelective(userInfo);
        return cnt>0?true:false;
    }

    /**
     * 添加金币
     * @param accountId
     * @param coin
     * @param fCoin
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addCoin(Long accountId, Long coin, Long fCoin) throws Exception
    {
        int cnt = userInfoMapper.addCoin(accountId, coin, fCoin);
        if (cnt > 0)
        {
            updateAccountInfo(new Account(accountId));
//            AccountDto accountDto = accountMapper.selectAccountInfo(accountId);
//            AccountDtoUtil.getInstance().update(accountDto);
            return true;
        }
        return false;
    }

    /**
     * 使用金币
     * @param accountId
     * @param coin
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public int useCoin(Long accountId, Long coin) throws Exception
    {
        //TODO check total coin first use f_coin
        UserInfo model = userInfoMapper.findByUid(accountId);
        if (model != null)
        {
            Long total = model.getCoin().longValue() + model.getfCoin().longValue();
            if (total.longValue() >= coin.longValue())
            {
                UserInfo userInfo = new UserInfo();
                userInfo.setUid(accountId);
                //TODO CAS
                if (model.getfCoin().longValue() >= coin.longValue())
                {
                    userInfo.setfCoin(model.getfCoin().longValue() - coin.longValue());
                }else
                {
                    userInfo.setfCoin(0l);
                    userInfo.setCoin(total.longValue() - coin.longValue());
                }
                int cnt = userInfoMapper.updateByPrimaryKeySelective(userInfo);
                if (cnt > 0)
                {
                    updateAccountInfo(new Account(accountId));
//                    AccountDto accountDto = new AccountDto();
//                    accountDto.setAccount_id(accountId);
//                    accountDto.setCoin(userInfo.getCoin());
//                    accountDto.setF_coin(userInfo.getfCoin());
//                    AccountDtoUtil.getInstance().update(accountDto);
                }
                return cnt;
            }
            return -1;
        }
        return 0;
    }


    /**
     * 修改钻石数
     * @param accountId
     * @param type
     * @param num
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateDiamond(Long accountId, int type, Long num) throws Exception
    {
        int res = 0;
        if (type < com.jwaoo.common.core.config.Constants.INCREASE)
        {
            UserInfo userInfo = userInfoMapper.findByUid(accountId);
            if (userInfo != null)
            {
                if (userInfo.getGold().longValue() >= num)
                {
                    UserInfo model = new UserInfo(accountId);
                    model.setGold(userInfo.getGold().longValue() - num.longValue());
                    res = userInfoMapper.updateByPrimaryKeySelective(model);
                } else
                {
                    res = -1;
                }
            }
        }else
        {
            res = userInfoMapper.updateDiamond(accountId, type, num);
        }
        if (res > 0)
        {
            updateAccountInfo(new Account(accountId));
        }
        return res;
    }

    /**
     * Vip 购买
     * @param accountId
     * @param days
     * @return
     * @throws Exception
     */
    public boolean updateVip(Long accountId, Integer days) throws Exception
    {
        boolean res = false;
        UserInfo model = userInfoMapper.findByUid(accountId);
        if (model != null)
        {
            Date start = model.getVipEndTime() != null ? model.getVipEndTime() : new Date();
            Date end = DateUtil.addDay(start, days);
            model = new UserInfo();
            model.setUid(accountId);
            model.setVipEndTime(end);
            int cnt = userInfoMapper.updateByPrimaryKeySelective(model);
            if (cnt > 0)
            {
                //TODO update vipEndTime & add vip history
                vipHistoryMapper.insertSelective(new VipHistory(accountId, 0, start, end));
//                AccountDto accountDto = new AccountDto();
//                accountDto.setAccount_id(accountId);
//                accountDto.setVip_end_time(end);
//                AccountDtoUtil.getInstance().update(accountDto);
                updateAccountInfo(new Account(accountId));
                res = true;
            }
        }
        return res;
    }

    /**
     * 修改经验值
     * @param accountId
     * @param type
     * @param num
     * @return
     * @throws Exception
     */
    public boolean updateExp(Long accountId, int type, Long num) throws Exception
    {
        boolean res = false;
        UserInfo userInfo = userInfoMapper.findByUid(accountId);
        if (userInfo != null)
        {
            int exp = userInfo.getExp().intValue();
            if (type < com.jwaoo.common.core.config.Constants.INCREASE)
            {
                exp = exp < num.intValue() ? 0 : exp - num.intValue();
            } else
            {
                exp = exp + num.intValue();
            }
            // TODO level logic
            int level = userInfo.getLevel();
            UserInfo model = new UserInfo(accountId);
            model.setExp(exp);
            model.setLevel(level);
//            int cnt = userInfoMapper.updateByPrimaryKeySelective(model);
            res = updateUserInfo(model);
            if (res)
            {
                updateAccountInfo(new Account(accountId));
            }
        }
        return res;
    }

    /**
     * 申请认证
     * @param accountId
     * @param imgUrl
     * @param audioUrl
     * @return
     */
    public boolean applyVerify(Long accountId, String imgUrl, String audioUrl) throws Exception
    {
        boolean res = false;
        int cnt = verifiesApplyMapper.insertSelective(new VerifiesApply(accountId, imgUrl, audioUrl, VerifiedStatusEnum.WIATING.getValue()));
        if (cnt > 0)
        {
            UserInfo userInfo = new UserInfo(accountId);
            userInfo.setIsVerified(VerifiedStatusEnum.WIATING.getValue());
            res = updateUserInfo(userInfo);
            if (res)
            {
//                AccountDto accountDto = new AccountDto();
//                accountDto.setAccount_id(accountId);
//                accountDto.setIs_verified(VerifiedStatusEnum.WIATING.getValue());
//                AccountDtoUtil.getInstance().update(accountDto);
                updateAccountInfo(new Account(accountId));
            }
        }
        return res;
    }

    /**
     * 查询认证申请列表
     * @param dto
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<VerifiesApplyDto> findVerifyByCondition(VerifiesApplyDto dto, Integer pageNo, Integer pageSize)
    {
        return verifiesApplyMapper.findByCondition(dto, ((pageNo <= 1?1:pageNo) - 1) * pageSize, pageSize);
    }

    public int countVerifyByCondition(VerifiesApplyDto dto)
    {
        return verifiesApplyMapper.countByCondition(dto);
    }

    /**
     * 查询认证信息
     * @param id
     * @return
     */
    public VerifiesApplyDto findVerifyApplyById(Long id)
    {
        return verifiesApplyMapper.findVerifyApplyById(id);
    }

    /**
     * 修改认证状态
     * @param id
     * @param status
     * @param remark
     * @return
     * @throws Exception
     */
    public boolean updateVerifyApplyStatus(Long id, VerifiedStatusEnum status, String remark) throws Exception
    {
        boolean res = false;
        VerifiesApply verifyApply = verifiesApplyMapper.selectByPrimaryKey(id);
        if (verifyApply != null)
        {
            int cnt = verifiesApplyMapper.updateByPrimaryKeySelective(new VerifiesApply(id, remark, status.getValue()));
            if (cnt > 0)
            {
                if (status.getValue() == VerifiedStatusEnum.PASS.getValue())
                {
                    UserInfo userInfo = new UserInfo(verifyApply.getUid());
                    userInfo.setIsVerified(VerifiedStatusEnum.PASS.getValue());
                    res = updateUserInfo(userInfo);
                    if (res)
                    {
//                        AccountDto accountDto = new AccountDto();
//                        accountDto.setAccount_id(verifyApply.getUid());
//                        accountDto.setIs_verified(VerifiedStatusEnum.PASS.getValue());
//                        AccountDtoUtil.getInstance().update(accountDto);
                        updateAccountInfo(new Account(verifyApply.getUid()));
                    }
                } else
                {
                    res = true;
                }
            }
        }
        return res;
    }

    public void updateAccList()
    {
        List<Account> list = accountMapper.findAll();
        list.parallelStream().forEach(obj -> {
            try {
                updateAccountInfo(obj);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
