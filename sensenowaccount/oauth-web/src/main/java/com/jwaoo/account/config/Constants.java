package com.jwaoo.account.config;

import com.jwaoo.common.core.config.Global;

import java.nio.charset.Charset;

/**
 * Application constants.
 */
public final class Constants 
{

    private Constants() {}

    public static final String SYSTEM_ACCOUNT = "-1";

    /**
     * 系统默认编码.
     */
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    public static final Charset ASSIC_CHARSET = Charset.forName("UTF-8");

    public static final Integer DEFAULT_LEVEL = 0;

    public static final String M5_SAL = "jwsn7_";

    public static final int REG_NOT_PERFECT = 0;
    public static final int REG_PERFECT = 1;

    public static final int ACCOUNT_UNLOCKED = 0;
    public static final int ACCOUNT_LOCKED = 1;

    public static final int USER_MIX_AGE = 12;

    public static String getAuthPwdToken() {
        return Global.getConfigCfg("auth.token", "");
    }

    public static String getAuthPwdKey() {
        return Global.getConfigCfg("auth.changeKey", "");
    }

    public static Integer getDistance() {
        return Integer.valueOf(Global.getConfigCfg("cfg:distance", "5"));
    }

    public static Boolean isTestMode() {
        return Boolean.valueOf(Global.getConfigCfg("cfg:isTestMode", "false"));
    }

    public static Integer getPageSize() {
        return Integer.valueOf(Global.getConfig("pageSize", "10"));
    }

}
