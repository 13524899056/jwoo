package com.jwaoo.account.sevice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jwaoo.account.web.rest.dto.MyUserRegisterDetail;
import com.jwaoo.common.core.config.Global;
import com.jwaoo.common.core.redis.JedisManager;
import com.jwaoo.common.security.utils.Constants;

public class RegisterService {
	
	private final static Logger log = LoggerFactory.getLogger(RegisterService.class);
	
    private static long expires;
//    private static long expires = 3600*12;

    private static RegisterService instance = new RegisterService();

    private RegisterService(){}

    public static RegisterService getInstance(){
    	return instance;
	}

	static {
        String expirStr = Global.getConfigCfg("reg.expires", Constants.REGISTER_EXPIRES_DEFAULT + "");
        expires = Long.valueOf(expirStr);
        log.debug("user redis expires={} days", expires);
        expires = expires * 24 * 3600;//ms
        log.debug("user redis expires={} ms", expires);
	}

    public MyUserRegisterDetail saveUserRegisterDetailToRedis(String key, MyUserRegisterDetail model){
        JedisManager.getInstance().set(Constants.REDIS_DB_REGISTER, key, model, expires);
		return model;
	}
    
    public MyUserRegisterDetail updateRedisUserDetail(String key,MyUserRegisterDetail model){
        JedisManager.getInstance().set(Constants.REDIS_DB_REGISTER, key, model, expires);
        return model;
    }
    
    public MyUserRegisterDetail getRedisUserDetail(String key){
        return (MyUserRegisterDetail) JedisManager.getInstance().get(Constants.REDIS_DB_REGISTER, key, MyUserRegisterDetail.class);
    }

    public void del(String key) {
        JedisManager.getInstance().Del(Constants.REDIS_DB_REGISTER, key);
    }

}
