package com.jwaoo.account.dto;

import java.io.Serializable;

/**
 * @author Jerry 
 * @date 2017年8月25日 下午2:12:26
 */
public class ForgetPwdDto implements Serializable
{

	private static final long serialVersionUID = 3491999925078904138L;
	
	private Long uid;
	private String pwd;
	private String loginName;
	private long expires;

	public ForgetPwdDto(){}
	
	public ForgetPwdDto(Long uid, String pwd, String loginName, long expires)
	{
		super();
		this.uid = uid;
		this.pwd = pwd;
		this.loginName = loginName;
		this.expires = expires;
	}


	public Long getUid()
	{
		return uid;
	}
	
	public void setUid(Long uid)
	{
		this.uid = uid;
	}
	
	public String getPwd()
	{
		return pwd;
	}
	
	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}
	
	public String getLoginName()
	{
		return loginName;
	}
	
	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
	}

	public long getExpires()
	{
		return expires;
	}

	public void setExpires(long expires)
	{
		this.expires = expires;
	}
	
}
