package com.jwaoo.account.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserAuthority implements Serializable{

    private Long userId;

    private String authorityName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }
}