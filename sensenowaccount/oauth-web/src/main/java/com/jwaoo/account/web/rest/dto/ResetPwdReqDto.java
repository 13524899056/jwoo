package com.jwaoo.account.web.rest.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class ResetPwdReqDto extends BasisReqDto{

    @NotBlank
    @Length(min = 2, max = 40)
    private String account;

    @NotBlank
    private String password;

    @NotBlank
    private String code;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password.toLowerCase();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
