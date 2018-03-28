package com.jwaoo.account.utils;

public enum OauthTypeEnum
{

	PLATFORM(1, "平台"),

    GOOGLE(2, "google"),

    FACEBOOK(3, "facebook"),

    TWITTER(4, "twitter");


	private int value;
	private String text;

	OauthTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static OauthTypeEnum getEnum(Integer val) {
		for (OauthTypeEnum v : values()){
			if (v.getValue() == val){
				return v;
			}
		}
		return null;
    }
}
