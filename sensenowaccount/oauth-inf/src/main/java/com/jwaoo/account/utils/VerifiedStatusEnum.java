package com.jwaoo.account.utils;

/**
 * @author Jerry
 * @date 2017/11/14 15:55
 */
public enum VerifiedStatusEnum
{
    WIATING(0),
    PASS(1),
    REJECT(2);

    private int value;

    VerifiedStatusEnum(int value)
    {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static VerifiedStatusEnum getEnum(Integer val) {
        for (VerifiedStatusEnum v : values()){
            if (v.getValue() == val){
                return v;
            }
        }
        return null;
    }

}
