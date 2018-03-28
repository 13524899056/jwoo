package com.jwaoo.account.model;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class VerifiesApply implements Serializable
{

    private Long id;

    private Long uid;

    private String imageUrl;

    private String audioUrl;

    private String remark;

    private Integer status;

    private Date createTime;

    private Date updateTime;


    public VerifiesApply () {}

    public VerifiesApply(Long id, String remark, Integer status)
    {
        this.id = id;
        this.remark = remark;
        this.status = status;
    }

    public VerifiesApply(Long uid, String imageUrl, String audioUrl, Integer status)
    {
        this.uid = uid;
        this.imageUrl = imageUrl;
        this.audioUrl = audioUrl;
        this.status = status;
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
}