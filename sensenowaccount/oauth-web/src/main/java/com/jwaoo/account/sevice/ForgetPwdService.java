package com.jwaoo.account.sevice;

import com.jwaoo.account.dto.ForgetPwdDto;
import com.jwaoo.common.core.config.Constants;
import com.jwaoo.common.core.redis.JedisManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author Jerry 
 * @date 2017年8月25日 下午2:09:29
 */
@Service
public class ForgetPwdService
{

    public static final String RDS_FGPWD = "fgp_";

    public static final long FGP_EXPIRES = 3600*24;


    /**
     * 保存缓存信息
     * @param fp
     * @return
     */
    public boolean save(String key, ForgetPwdDto fp)
    {
		boolean res = false;
		if (fp != null)
		{
            JedisManager.getInstance().set(Constants.REDIS_DB_3, RDS_FGPWD + key, fp, FGP_EXPIRES);
			res = true;
		}
		return res;
	}
    
    /**
     * 查询缓存信息
     * @param key
     * @return
     */
    public ForgetPwdDto findByUid(String key)
    {
    	ForgetPwdDto fp = null;
    	if (StringUtils.isNotBlank(key))
		{
            fp = (ForgetPwdDto) JedisManager.getInstance().get(Constants.REDIS_DB_3, RDS_FGPWD + key, ForgetPwdDto.class);
		}
    	return fp;
    }
    
    /**
     * 移除缓存信息
     * @param key
     * @return
     */
    public boolean remove(String key)
    {
    	boolean res = false;
    	if (StringUtils.isNotBlank(key))
		{
            JedisManager.getInstance().Del(Constants.REDIS_DB_3, RDS_FGPWD + key);
			res = true;
		}
    	return res;
    }

}
