package com.jwaoo.account.model;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

public class UserInfo implements Serializable
{
    
    private Long uid;

    private String ssnNo;

    private String payPassword;

    private Integer vip;

    private Date vipEndTime;

    private Integer isVerified;

    private Integer exp;

    private Integer level;

    private Long gold;

    private Long coin;

    private Long fCoin;

    private Long balance;


    public UserInfo(){}

    public UserInfo(Long uid)
    {
        this.uid = uid;
        this.ssnNo = StringUtils.EMPTY;
        this.payPassword = StringUtils.EMPTY;
        this.vip = 0;
        this.isVerified = 0;
        this.exp = 0;
        this.level = 1;
        this.gold = 0l;
        this.coin = 0l;
        this.fCoin = 0l;
        this.balance = 0l;
    }

    public UserInfo(Long uid, Integer exp, Integer level, Long gold, Long coin, Long fCoin)
    {
        this.uid = uid;
        this.ssnNo = StringUtils.EMPTY;
        this.payPassword = StringUtils.EMPTY;
        this.vip = 0;
        this.isVerified = 0;
        this.exp = exp;
        this.level = level;
        this.gold = gold;
        this.coin = coin;
        this.fCoin = fCoin;
        this.balance = 0l;
    }

    public UserInfo(Long uid, String ssnNo, String payPassword, Integer vip, Date vipEndTime, Integer isVerified, Integer exp, Integer level, Long gold, Long coin, Long fCoin, Long balance)
    {
        this.uid = uid;
        this.ssnNo = ssnNo;
        this.payPassword = payPassword;
        this.vip = vip;
        this.vipEndTime = vipEndTime;
        this.isVerified = isVerified;
        this.exp = exp;
        this.level = level;
        this.gold = gold;
        this.coin = coin;
        this.fCoin = fCoin;
        this.balance = balance;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getSsnNo() {
        return ssnNo;
    }

    public void setSsnNo(String ssnNo) {
        this.ssnNo = ssnNo;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
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

    public Integer getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Integer isVerified) {
        this.isVerified = isVerified;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getGold() {
        return gold;
    }

    public void setGold(Long gold) {
        this.gold = gold;
    }

    public Long getCoin() {
        return coin;
    }

    public void setCoin(Long coin) {
        this.coin = coin;
    }

    public Long getfCoin() {
        return fCoin;
    }

    public void setfCoin(Long fCoin) {
        this.fCoin = fCoin;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }
}