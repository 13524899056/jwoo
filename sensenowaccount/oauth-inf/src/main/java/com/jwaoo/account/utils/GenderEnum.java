package com.jwaoo.account.utils;

/**
 * @author Jerry
 * @date 2017/7/5 17:33
 */
public enum GenderEnum {

    UNKNOWN(0),

    MALE(1),

    FEMALE(2);

    private Integer value;

    GenderEnum(Integer value){
        this.value= value;
    }

    public int getValue() {
        return value;
    }

    public static GenderEnum getEnum(Integer val) {
        for (GenderEnum v : values()){
            if (v.getValue() == val){
                return v;
            }
        }
        return null;
    }

}
