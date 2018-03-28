package com.jwaoo.account.mapper;

import com.jwaoo.account.model.UserRegister;
import com.jwaoo.account.model.UserRegisterExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface UserRegisterMapper
{


	/**
	 * 保存
	 * 
	 * @mbg.generated
	 */
	int insert(UserRegister record);

	/**
	 * 选择不为空的字段保存
	 * 
	 * @mbg.generated
	 */
	int insertSelective(UserRegister record);


	/**
	 * 根据主键选择不为空的字段更新
	 * 
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(UserRegister record);

	/**
	 * 根据主键更新
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKey(UserRegister record);

	/**
	 * 根据uuid查询
	 * @param uuid
	 * @return
	 */
	UserRegister findByUUid(String uuid);

    /**
     * 根据授权类型和OpenId查询用户是否已存在
     * @param openId
     * @param oauthType
     * @return
     */
	UserRegister findByOpenId(@Param("openId")String openId, @Param("oauthType")Integer oauthType);

    int countByExample(UserRegisterExample example);

    int deleteByExample(UserRegisterExample example);

    int deleteByPrimaryKey(String uuid);

    List<UserRegister> selectByExampleWithRowbounds(UserRegisterExample example, RowBounds rowBounds);

    List<UserRegister> selectByExample(UserRegisterExample example);

    UserRegister selectByPrimaryKey(String uuid);

    int updateByExampleSelective(@Param("record") UserRegister record, @Param("example") UserRegisterExample example);

    int updateByExample(@Param("record") UserRegister record, @Param("example") UserRegisterExample example);

}