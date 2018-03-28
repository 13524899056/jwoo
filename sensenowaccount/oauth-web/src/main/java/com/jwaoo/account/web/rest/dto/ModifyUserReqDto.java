package com.jwaoo.account.web.rest.dto;

import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;


public class ModifyUserReqDto {

    @Length(max = 30)
    private String nickname;

    @Length(max = 250)
    private String avatar;

    private Integer gender;

    @Length(max = 10)
    private String birthday;

    private LocalDate birthdayDate;

    @Length(max = 250)
    private String address;

    @Length(max = 250)
    private String instr;


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
//    public GenderType getGender() {
//        return gender;
//    }
//
//    public void setGender(GenderType gender) {
//        this.gender = gender;
//    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public LocalDate getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(LocalDate birthdayDate) {
        this.birthdayDate = birthdayDate;
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

}
