package com.jwaoo.account.mapper;

import com.jwaoo.account.dto.AccountDto;
import com.jwaoo.account.model.Account;
import com.jwaoo.account.model.AccountExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface AccountMapper
{

	/**
	 * 根据用户名查询
	 * @param account
	 * @return
	 */
	Account findByAccount(@Param("account") String account);

	/**
	 * 添加
	 * @param record
	 * @return
	 */
	int insert(Account record);

	/**
	 * 选择不为空的字段添加
	 * @param record
	 * @return
	 */
	int insertSelective(Account record);

	/**
	 * 根据mail查询
	 * @param email
	 * @return
	 */
	Account findByEmail(@Param("email") String email, Long uid);

    /**
     * 根据手机号查询用户信息
     * @param phone
     * @param uid
     * @return
     */
	Account findByPhone(@Param("phone") String phone, Long uid);

    /**
     * 根据昵称查询用户信息
     * @param nickname
     * @param uid
     * @return
     */
	Account findByNickname(@Param("nickname") String nickname, @Param("id")Long uid);

	/**
	 * 选择不为空的字段更新
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(Account record);

	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
	Account findById(@Param("id")Long id);

    /**
     * 根据UUID 查询用户信息
     * @param uuid
     * @return
     */
	Account findByUuid(@Param("uuid")String uuid);

	/**
	 * 根据主键修改
	 * @param record
	 * @return
	 */
	int updateById(Account record);

    /**
     * 查询用户记录数
     * @return
     */
    long findAllCount();

    /**
     * 查询所有用户信息
     * @param size
     * @param limit
     * @return
     */
    List<Account> findAllList(@Param("size")Integer size, @Param("limit")Integer limit);

    List<Account> findAll();

    int countByExample(AccountExample example);

    List<Account> selectByExampleWithRowbounds(AccountExample example, RowBounds rowBounds);

    List<Account> selectByExample(AccountExample example);

    int updateByExampleSelective(@Param("record") Account record, @Param("example") AccountExample example);

    int updateByExample(@Param("record") Account record, @Param("example") AccountExample example);

    AccountDto selectAccountInfo(@Param("uid") Long uid);

}
