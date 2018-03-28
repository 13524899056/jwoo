package com.jwaoo.account.sevice;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwaoo.account.mapper.UserRegisterMapper;
import com.jwaoo.account.model.UserRegister;
import com.jwaoo.account.utils.OauthTypeEnum;

@Service
@Transactional
public class UserRegisterService
{

	@Autowired
	private UserRegisterMapper userRegisterMapper;

    /**
     * save model
     * @param userRegister
     * @return
     */
	public boolean save(UserRegister userRegister) throws Exception
    {
        boolean res = false;
        if (userRegister != null)
        {
            int ct = userRegisterMapper.insert(userRegister);
            res = ct>0?true:false;
        }
        return res;
	}

    /**
     * save model
     * @param userRegister
     * @return
     */
	public boolean saveSelective(UserRegister userRegister) throws Exception
    {
        boolean res = false;
        if (userRegister != null)
        {
            int ct = userRegisterMapper.insertSelective(userRegister);
            res = ct>0?true:false;
        }
		return res;
	}

    /**
     * find by uuid
     * @param uuid
     * @return
     */
	public UserRegister findByUUid(String uuid) throws Exception
	{
		UserRegister model = null;
        if (StringUtils.isNotBlank(uuid)) {
            model = userRegisterMapper.findByUUid(uuid);
        }
		return model;
	}

    /**
     * 根据第三方登录信息查询用户是否已注册
     * @param openId
     * @param oauthType
     * @return
     */
    public UserRegister findByOpenId(String openId, OauthTypeEnum oauthType) throws Exception
    {
        UserRegister userRegister = null;
        if(StringUtils.isNotBlank(openId) && oauthType != null){
            userRegister = userRegisterMapper.findByOpenId(openId, oauthType.getValue());
        }
        return userRegister;
    }

}
