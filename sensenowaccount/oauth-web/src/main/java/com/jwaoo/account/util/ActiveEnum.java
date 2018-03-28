package com.jwaoo.account.util;

/**
 * @author Jerry
 * @date 2017/7/20 18:00
 */
public enum ActiveEnum {

    ACTIVED(1),

    NOT_ACTIVE(0);

    private int value;

    ActiveEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


    public static ActiveEnum getEnum(Integer val) {
        for (ActiveEnum v : values()){
            if (v.getValue() == val){
                return v;
            }
        }
        return null;
    }

}
