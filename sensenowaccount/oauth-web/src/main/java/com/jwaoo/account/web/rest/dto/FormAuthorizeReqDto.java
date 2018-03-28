package com.jwaoo.account.web.rest.dto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class FormAuthorizeReqDto
{

    @Pattern(regexp = "^[0-9a-zA-Z_+.@]{6,40}$")
    @Length(max = 30)
    private String account;

    /** 密码MD5 */
    @Pattern(regexp = "^[0-9a-z]{32}$")
    @Length(max = 32)
    private String password;

    @NotNull
    private Integer type;
    
    private String openId;
    
    private String accessToken;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return StringUtils.lowerCase(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
	}

	public String getOpenId()
	{
		return openId;
	}

	public void setOpenId(String openId)
	{
		this.openId = openId;
	}

	public String getAccessToken()
	{
		return accessToken;
	}

	public void setAccessToken(String accessToken)
	{
		this.accessToken = accessToken;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
