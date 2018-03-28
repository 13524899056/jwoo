package com.jwaoo.account.service.inf;

import com.jwaoo.account.dto.AccountDto;
import com.jwaoo.account.dto.AcctClientDto;
import com.jwaoo.account.dto.VerifiesApplyDto;
import com.jwaoo.account.model.Account;
import com.jwaoo.account.model.VerifiesApply;
import com.jwaoo.account.utils.UserStatusEnum;
import com.jwaoo.account.utils.VerifiedStatusEnum;
import com.jwaoo.common.core.mongo.Sort;

import java.util.Date;
import java.util.List;

/**
 * @author Jerry
 * @date 2017/4/27 11:46
 */
public interface UserServiceInf
{

    /**
     * 根据ID查询用户信息
     * @param uid
     * @return
     * @throws Exception
     */
    Account findById(Long uid) throws Exception;

    AccountDto findInfoById(Long uid) throws Exception;

    /**
     * 根据UUID查询用户信息
     * @param uuid
     * @return
     * @throws Exception
     */
    Account findByUUid(String uuid) throws Exception;

    AccountDto findInfoByUUid(String uuid) throws Exception;

    /**
     * 根据用户ID列表查询用户信息
     * @param uids
     * @return
     * @throws Exception
     */
    List<Account> findByIds(List<Long> uids) throws Exception;

    List<AccountDto> findInfoByIds(List<Long> uids) throws Exception;

    /**
     * 根据用户ID查询推送ID
     * @param clientId
     * @param uids
     * @return
     * @throws Exception
     */
    List<AcctClientDto> findAcctClient(String clientId, List<Long> uids) throws Exception;

    /**
     * 根据条件查询用户数
     * @param dto
     * @return
     * @throws Exception
     */
    long count(AccountDto dto) throws Exception;

    /**
     * 根据条件查询用户列表
     * @param dto
     * @param orderby
     * @param pageSize
     * @param pageNo
     * @return
     * @throws Exception
     */
    List<AccountDto> findList(AccountDto dto, Sort orderby, Integer pageSize, Integer pageNo) throws Exception;

    /**
     * 修改账号状态
     * @param uid
     * @param status
     * @return
     * @throws Exception
     */
    boolean updateAcctStatus(Long uid, UserStatusEnum status) throws Exception;

    /**
     * 账号冻结
     * @param uid
     * @param freezeEndTime
     * @return
     * @throws Exception
     */
    boolean freezeAccount(Long uid, Date freezeEndTime) throws Exception;

    /**
     * 添加金币
     * @param uid
     * @param coin
     * @param fCoin
     * @return
     * @throws Exception
     */
    boolean addCoin(Long uid, Long coin, Long fCoin) throws Exception;

    /**
     * 使用金币
     * @param uid
     * @param coin
     * @return
     * @throws Exception
     */
    int useCoin(Long uid, Long coin) throws Exception;

    /**
     * 购买VIP
     * @param uid
     * @param days
     * @return
     * @throws Exception
     */
    boolean buyVip(Long uid, Integer days) throws Exception;

    /**
     * 修改经验值
     * @param uid
     * @param type
     * @param num
     * @return
     * @throws Exception
     */
    boolean updateExp(Long uid, int type, Integer num) throws Exception;

    /**
     * 添加认证信息
     * @param model
     * @return
     * @throws Exception
     */
    boolean verfiedApply(VerifiesApply model) throws Exception;

    /**
     * 修改认证状态
     * @param id
     * @param status
     * @param remark
     * @return
     * @throws Exception
     */
    boolean updateVerifiedStatus(Long id, VerifiedStatusEnum status, String remark) throws Exception;

    /**
     * 修改用户钻石数
     * @param uid
     * @param type
     * @param num
     * @return
     * @throws Exception
     */
    int updateDiamond(Long uid, int type, Long num) throws Exception;

    /**
     * 查询认证申请列表
     * @param dto
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<VerifiesApplyDto> findVerifyApplyList(VerifiesApplyDto dto, Integer pageNo, Integer pageSize) throws Exception;

    /**
     * 查询认证申请记录数
     * @param dto
     * @return
     */
    long countVerifyApply(VerifiesApplyDto dto) throws Exception;

    VerifiesApplyDto findVerifyApplyById(Long id) throws Exception;

}
