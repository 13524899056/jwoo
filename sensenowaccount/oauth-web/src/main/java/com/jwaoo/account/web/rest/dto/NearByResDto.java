package com.jwaoo.account.web.rest.dto;

import java.util.Date;

/**
 * @author Jerry
 * @date 2017/8/15 11:18
 */
public class NearByResDto
{

    private String id;
    private Long uid;
    private Integer osType;
    private Double[] loc;	//位置
    private Integer active;
    private String nickname;
    private String avatar;
    private Integer gender;
    private Integer level;
    private Integer isVerify;
    private Date vipEndTime;
    private Date lastActiveTime;
    private Date updateTime;

    public NearByResDto(){}

    public NearByResDto(String id, Long uid, Integer osType, Double[] loc, Integer active, String nickname, String avatar, Integer gender, Integer level, Integer isVerify, Date vipEndTime, Date lastActiveTime, Date updateTime)
    {
        this.id = id;
        this.uid = uid;
        this.osType = osType;
        this.loc = loc;
        this.active = active;
        this.nickname = nickname;
        this.avatar = avatar;
        this.gender = gender;
        this.level = level;
        this.isVerify = isVerify;
        this.vipEndTime = vipEndTime;
        this.lastActiveTime = lastActiveTime;
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getOsType() {
        return osType;
    }

    public void setOsType(Integer osType) {
        this.osType = osType;
    }

    public Double[] getLoc() {
        return loc;
    }

    public void setLoc(Double[] loc) {
        this.loc = loc;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(Integer isVerify) {
        this.isVerify = isVerify;
    }

    public Date getVipEndTime() {
        return vipEndTime;
    }

    public void setVipEndTime(Date vipEndTime) {
        this.vipEndTime = vipEndTime;
    }

    public Date getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(Date lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
