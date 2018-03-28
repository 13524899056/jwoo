package com.jwaoo.account.mapper;

import com.jwaoo.account.model.UserInfo;
import com.jwaoo.account.model.UserInfoExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface UserInfoMapper
{

    int countByExample(UserInfoExample example);

    int deleteByExample(UserInfoExample example);

    int deleteByPrimaryKey(Long uid);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    List<UserInfo> selectByExampleWithRowbounds(UserInfoExample example, RowBounds rowBounds);

    List<UserInfo> selectByExample(UserInfoExample example);

    UserInfo selectByPrimaryKey(Long uid);

    int updateByExampleSelective(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    int updateByExample(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    int addCoin(@Param("uid") Long uid, @Param("coin") Long coin, @Param("fCoin") Long fCoin);

    UserInfo findByUid(Long uid);

    int updateDiamond(@Param("uid") Long uid, @Param("type")Integer type, @Param("num")Long num);

}