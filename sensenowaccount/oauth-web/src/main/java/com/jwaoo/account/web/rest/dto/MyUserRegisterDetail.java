package com.jwaoo.account.web.rest.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The security token.
 */
public class MyUserRegisterDetail implements Serializable {

	private static final long serialVersionUID = -104685573380091588L;


    String account;
    String password;
    String phone = StringUtils.EMPTY;
    String email = StringUtils.EMPTY;
	String clientId = StringUtils.EMPTY;
	String channelId = StringUtils.EMPTY;
	String osType = StringUtils.EMPTY;
	String osVersion = StringUtils.EMPTY;
	String hardWare = StringUtils.EMPTY;
	String version = StringUtils.EMPTY;
	String deviceToken = StringUtils.EMPTY;
	String sourceIp = StringUtils.EMPTY;
	String country = StringUtils.EMPTY;
	String area = StringUtils.EMPTY;
    String nickname = StringUtils.EMPTY;
    String avatar = StringUtils.EMPTY;
    Integer gender = 0;
    String birthday = StringUtils.EMPTY;
	String userAgent = StringUtils.EMPTY;


	public MyUserRegisterDetail() {
	}

	public MyUserRegisterDetail(String account, String password) {
		super();
		this.account = account;
		this.password = password;
	}

    public MyUserRegisterDetail(String account, String password, String clientId, String channelId, String osType, String hardWare, String version, String deviceToken, String sourceIp) {
        super();
        this.account = account;
        this.password = password;
        this.clientId = clientId;
        this.channelId = channelId;
        this.osType = osType;
        this.hardWare = hardWare;
        this.version = version;
        this.deviceToken = deviceToken;
        this.sourceIp = sourceIp;
    }


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOsType()
	{
		return osType;
	}

	public void setOsType(String osType)
	{
		this.osType = osType;
	}

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getClientId()
	{
		return clientId;
	}

	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getHardWare()
	{
		return hardWare;
	}

	public void setHardWare(String hardWare)
	{
		this.hardWare = hardWare;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
