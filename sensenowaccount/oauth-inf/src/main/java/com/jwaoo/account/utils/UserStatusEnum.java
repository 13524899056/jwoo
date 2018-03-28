package com.jwaoo.account.utils;

/**
 * @author Jerry 
 * @date 2017年8月28日 上午11:41:53
 */
public enum UserStatusEnum
{
	NORMAL(0),
    DISABLE(1),
	LOCKED(2);
//	FREEZE(3);

	private int value;
	
	public int getValue()
	{
		return value;
	}

    UserStatusEnum(int value){ this.value = value;}

}
