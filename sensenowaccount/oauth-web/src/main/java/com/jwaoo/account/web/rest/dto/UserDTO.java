package com.jwaoo.account.web.rest.dto;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Date;

public class UserDTO {

    private Long id = 0l;

    private String uuid = StringUtils.EMPTY;

    private String nickname = StringUtils.EMPTY;

    private String avatar = StringUtils.EMPTY;

    private String phone = StringUtils.EMPTY;

    private String email = StringUtils.EMPTY;

    private Integer gender = 0;

//    @Pattern(regexp = "^((1[3458]\\d)|(170))\\d{8}$", message = "电话号码格式不正确")
    private String address = StringUtils.EMPTY;

    private String instr = StringUtils.EMPTY;

//    private Integer perfect = 0;

//    private String area = StringUtils.EMPTY;// 地区（id;name）

    private LocalDate birthday;

    private String level = StringUtils.EMPTY;

    private Integer vip = 0;

    private Date vipEndTime;
    

    public UserDTO() {}

    public UserDTO(Long id, String uuid, String nickname, String avatar, Integer gender)
    {
        this.id = id;
        this.uuid = uuid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.gender = gender;
    }

    public UserDTO(Long id, String uuid, String nickname, String avatar, Integer gender, LocalDate birthday, String level, Integer vip, Date vipEndTime)
    {
        this.id = id;
        this.uuid = uuid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.gender = gender;
        this.birthday = birthday;
        this.level = level;
        this.vip = vip;
        this.vipEndTime = vipEndTime;
    }

    public UserDTO(Long id, String uuid, String nickname, String avatar, String phone, String email, Integer gender, String address, String instr, LocalDate birthday, String level)
    {
        this.id = id;
        this.uuid = uuid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.address = address;
        this.instr = instr;
        this.birthday = birthday;
        this.level = level;
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInstr() {
        return instr;
    }

    public void setInstr(String instr) {
        this.instr = instr;
    }

//    public String getArea() {
//        return area;
//    }
//
//    public void setArea(String area) {
//        this.area = area;
//    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public Date getVipEndTime() {
        return vipEndTime;
    }

    public void setVipEndTime(Date vipEndTime) {
        this.vipEndTime = vipEndTime;
    }
}
