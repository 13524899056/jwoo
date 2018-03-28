package com.jwaoo.account.mapper;

import com.jwaoo.account.model.Authority;
import com.jwaoo.account.model.AuthorityExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author Jerry
 * @date 2017/5/5 14:21
 */
public interface AuthorityMapper {

    Authority findByName(String name);

    int countByExample(AuthorityExample example);

    int deleteByExample(AuthorityExample example);

    int deleteByPrimaryKey(String name);

    int insert(Authority record);

    int insertSelective(Authority record);

    List<Authority> selectByExampleWithRowbounds(AuthorityExample example, RowBounds rowBounds);

    List<Authority> selectByExample(AuthorityExample example);

    int updateByExampleSelective(@Param("record") Authority record, @Param("example") AuthorityExample example);

    int updateByExample(@Param("record") Authority record, @Param("example") AuthorityExample example);

}
