package com.jwaoo.account.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author Jerry
 * @date 2017/12/20 11:45
 */
public class VerifiesApplyDto implements Serializable
{

    private Long id;

    private Long uid;

    private String imageUrl;

    private String audioUrl;

    private String remark;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private String nickname;

    private Integer gender;

    private String avatar;

    private String areaCode;

    private LocalDate birthday;


    public VerifiesApplyDto() {}

    public VerifiesApplyDto(Long id, Long uid, String imageUrl, String audioUrl, String remark, Integer status, Date createTime, Date updateTime, String nickname, Integer gender, String avatar, String areaCode, LocalDate birthday) {
        this.id = id;
        this.uid = uid;
        this.imageUrl = imageUrl;
        this.audioUrl = audioUrl;
        this.remark = remark;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.nickname = nickname;
        this.gender = gender;
        this.avatar = avatar;
        this.areaCode = areaCode;
        this.birthday = birthday;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
