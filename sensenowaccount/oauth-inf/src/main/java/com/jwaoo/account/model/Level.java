package com.jwaoo.account.model;

import java.io.Serializable;

public class Level implements Serializable
{

    private Integer level;

    private String name;

    private String descr;

    private Long limitExp;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Long getLimitExp() {
        return limitExp;
    }

    public void setLimitExp(Long limitExp) {
        this.limitExp = limitExp;
    }
}