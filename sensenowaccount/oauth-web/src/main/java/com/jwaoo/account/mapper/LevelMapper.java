package com.jwaoo.account.mapper;

import com.jwaoo.account.model.Level;
import com.jwaoo.account.model.LevelExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface LevelMapper {
    int countByExample(LevelExample example);

    int deleteByExample(LevelExample example);

    int deleteByPrimaryKey(Short level);

    int insert(Level record);

    int insertSelective(Level record);

    List<Level> selectByExampleWithRowbounds(LevelExample example, RowBounds rowBounds);

    List<Level> selectByExample(LevelExample example);

    Level selectByPrimaryKey(Short level);

    int updateByExampleSelective(@Param("record") Level record, @Param("example") LevelExample example);

    int updateByExample(@Param("record") Level record, @Param("example") LevelExample example);

    int updateByPrimaryKeySelective(Level record);

    int updateByPrimaryKey(Level record);
}