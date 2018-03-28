package com.jwaoo.account.web.rest.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterReqDto {

    @NotBlank
    private String account;

    @NotBlank
    private String password;

    @NotBlank
    @Size(min = 2, max = 40)
    private String area;

    private String nickname;

    private String avatar;

    private Integer gender;

    private String birthday;


    public String getAccount() {
        return account.trim();
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
