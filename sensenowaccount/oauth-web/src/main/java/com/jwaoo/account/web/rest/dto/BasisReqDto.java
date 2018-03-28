package com.jwaoo.account.web.rest.dto;

import javax.validation.constraints.Size;


public class BasisReqDto
{

    private String clientId;
    
	private Integer channelId;
    
	private Integer osType;
    
	private String hardWare;
	
	@Size(max = 20)
	private String version;

	
	public String getClientId()
	{
		return clientId;
	}

	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}

	public Integer getChannelId()
	{
		return channelId;
	}

	public void setChannelId(Integer channelId)
	{
		this.channelId = channelId;
	}

	public Integer getOsType()
	{
		return osType;
	}

	public void setOsType(Integer osType)
	{
		this.osType = osType;
	}

	public String getHardWare()
	{
		return hardWare;
	}

	public void setHardWare(String hardWare)
	{
		this.hardWare = hardWare;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}
	
	
}
