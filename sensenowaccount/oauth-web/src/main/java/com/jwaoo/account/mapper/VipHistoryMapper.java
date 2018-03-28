package com.jwaoo.account.mapper;

import com.jwaoo.account.model.VipHistory;
import com.jwaoo.account.model.VipHistoryExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface VipHistoryMapper
{

    int countByExample(VipHistoryExample example);

    int deleteByExample(VipHistoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(VipHistory record);

    int insertSelective(VipHistory record);

    List<VipHistory> selectByExampleWithRowbounds(VipHistoryExample example, RowBounds rowBounds);

    List<VipHistory> selectByExample(VipHistoryExample example);

    VipHistory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") VipHistory record, @Param("example") VipHistoryExample example);

    int updateByExample(@Param("record") VipHistory record, @Param("example") VipHistoryExample example);

    int updateByPrimaryKeySelective(VipHistory record);

    int updateByPrimaryKey(VipHistory record);

}