package com.jwaoo.account.mapper;

import com.jwaoo.account.model.LoginHistory;
import com.jwaoo.account.model.LoginHistoryExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface LoginHistoryMapper
{

	/**
	 * 保存
	 * @mbg.generated
	 */
	int insert(LoginHistory record);

	/**
	 * 选择不为这字段保存
	 * @mbg.generated
	 */
	int insertSelective(LoginHistory record);

	/**
	 * 根据主键查询
	 * @mbg.generated
	 */
	List<LoginHistory> findByAccountId(Long accountId);

    int countByExample(LoginHistoryExample example);

    int deleteByExample(LoginHistoryExample example);

    int deleteByPrimaryKey(Long id);

    List<LoginHistory> selectByExampleWithRowbounds(LoginHistoryExample example, RowBounds rowBounds);

    List<LoginHistory> selectByExample(LoginHistoryExample example);

    LoginHistory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") LoginHistory record, @Param("example") LoginHistoryExample example);

    int updateByExample(@Param("record") LoginHistory record, @Param("example") LoginHistoryExample example);

    int updateByPrimaryKeySelective(LoginHistory record);

    int updateByPrimaryKey(LoginHistory record);

}