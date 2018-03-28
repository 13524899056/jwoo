package com.jwaoo.account.mapper;

import com.jwaoo.account.model.UserBankCard;
import com.jwaoo.account.model.UserBankCardExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface UserBankCardMapper {

    int countByExample(UserBankCardExample example);

    int deleteByExample(UserBankCardExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserBankCard record);

    int insertSelective(UserBankCard record);

    List<UserBankCard> selectByExampleWithRowbounds(UserBankCardExample example, RowBounds rowBounds);

    List<UserBankCard> selectByExample(UserBankCardExample example);

    UserBankCard selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserBankCard record, @Param("example") UserBankCardExample example);

    int updateByExample(@Param("record") UserBankCard record, @Param("example") UserBankCardExample example);

    int updateByPrimaryKeySelective(UserBankCard record);

    int updateByPrimaryKey(UserBankCard record);

}