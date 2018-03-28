package com.jwaoo.account.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@SuppressWarnings("serial")
public class Account implements Serializable
{

    private Long id;

    private String uuid;

    @JsonIgnore
    private String password;

    private String nickname;

    private String name;

    private String email;

    private String phone;

    private Integer gender;

    private String areaCode;

    private String avatar;

    private LocalDate birthday;

    private String instr;

    private String level;

    private Integer exp;

    private Integer integral;

    private String address;

    private String openId;

    private Date freezeEndTime;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    public Account() {}

    public Account(Long accountId)
    {
        this.id = accountId;
    }

    public Account(String uuid, String password, String nickname, String name, String email, String phone, Integer gender, String areaCode, String avatar, LocalDate birthday, String instr, String level, Integer exp, Integer integral, String address, String openId, Date freezeEndTime, Integer status)
    {
        this.uuid = uuid;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.areaCode = areaCode;
        this.avatar = avatar;
        this.birthday = birthday;
        this.instr = instr;
        this.level = level;
        this.exp = exp;
        this.integral = integral;
        this.address = address;
        this.openId = openId;
        this.freezeEndTime = freezeEndTime;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getInstr() {
        return instr;
    }

    public void setInstr(String instr) {
        this.instr = instr;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getFreezeEndTime() {
        return freezeEndTime;
    }

    public void setFreezeEndTime(Date freezeEndTime) {
        this.freezeEndTime = freezeEndTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
