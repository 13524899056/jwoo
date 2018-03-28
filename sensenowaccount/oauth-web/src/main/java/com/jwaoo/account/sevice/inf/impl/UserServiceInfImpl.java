package com.jwaoo.account.sevice.inf.impl;

import com.jwaoo.account.dto.AccountDto;
import com.jwaoo.account.dto.AcctClientDto;
import com.jwaoo.account.dto.VerifiesApplyDto;
import com.jwaoo.account.model.Account;
import com.jwaoo.account.model.VerifiesApply;
import com.jwaoo.account.service.inf.UserServiceInf;
import com.jwaoo.account.sevice.AccountDtoUtil;
import com.jwaoo.account.sevice.AccountService;
import com.jwaoo.account.sevice.AcctClientService;
import com.jwaoo.account.utils.UserStatusEnum;
import com.jwaoo.account.utils.VerifiedStatusEnum;
import com.jwaoo.common.core.mongo.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * @author Jerry
 * @date 2017/4/27 11:47
 */
@Component
public class UserServiceInfImpl implements UserServiceInf
{

    @Autowired
    private AccountService accountService;

    /**
     * 根据ID查询用户信息
     * @param uid
     * @return
     */
    public AccountDto findInfoById(Long uid) throws Exception
    {
        return AccountDtoUtil.getInstance().findByAccountId(uid);
    }

    public Account findById(Long uid) throws Exception
    {
        return accountService.findById(uid);
    }

    public Account findByUUid(String uuid) throws Exception
    {
        return accountService.findByUuid(uuid);
    }

    public AccountDto findInfoByUUid(String uuid) throws Exception
    {
        return AccountDtoUtil.getInstance().findById(uuid);
    }

    public List<Account> findByIds(List<Long> uids) throws Exception
    {
        return null;
    }

    public List<AccountDto> findInfoByIds(List<Long> uids) throws Exception
    {
        return AccountDtoUtil.getInstance().findByUids(uids);
    }

    public List<AcctClientDto> findAcctClient(String clientId, List<Long> uids) throws Exception
    {
        return AcctClientService.getInstance().findPushIdsByAccountId(clientId, uids);
    }

    public long count(AccountDto dto) throws Exception
    {
        return AccountDtoUtil.getInstance().count(dto);
    }

    /**
     * 查询用户列表
     * @param dto
     * @param orderBy
     * @param pageSize
     * @param pageNo
     * @return
     * @throws Exception
     */
    public List<AccountDto> findList(AccountDto dto, Sort orderBy, Integer pageSize, Integer pageNo) throws Exception
    {
        return AccountDtoUtil.getInstance().find(dto, orderBy, pageSize, pageNo);
    }

    /**
     * 修改账号状态
     * @param uid
     * @param status
     * @return
     * @throws Exception
     */
    public boolean updateAcctStatus(Long uid, UserStatusEnum status) throws Exception
    {
        Account model = new Account();
        model.setId(uid);
        model.setStatus(status.getValue());
        boolean res = accountService.update(model);
        if (res)
        {
            accountService.updateAccountInfo(model);
        }
        return res;
    }

    /**
     * 冻结账号
     * @param uid
     * @param freezeEndTime
     * @return
     * @throws Exception
     */
    public boolean freezeAccount(Long uid, Date freezeEndTime) throws Exception
    {
        Account model = new Account();
        model.setId(uid);
        model.setFreezeEndTime(freezeEndTime);
        boolean res = accountService.update(model);
        if (res)
        {
            accountService.updateAccountInfo(model);
        }
        return res;
    }

    /**
     * 添加金币
     * @param uid
     * @param coin
     * @param fCoin
     * @return
     * @throws Exception
     */
    public boolean addCoin(Long uid, Long coin, Long fCoin) throws Exception
    {
        return accountService.addCoin(uid, coin, fCoin);
    }

    /**
     * 消耗金币
     * @param uid
     * @param coin
     * @return
     * @throws Exception
     */
    @Override
    public int useCoin(Long uid, Long coin) throws Exception
    {
        return accountService.useCoin(uid, coin);
    }

    /**
     * 购买VIP
     * @param uid
     * @param days
     * @return
     * @throws Exception
     */
    @Override
    public boolean buyVip(Long uid, Integer days) throws Exception
    {
        return accountService.updateVip(uid, days);
    }

    @Override
    public boolean updateExp(Long uid, int type, Integer num) throws Exception
    {
        return false;
    }

    @Override
    public boolean verfiedApply(VerifiesApply model) throws Exception
    {
        return accountService.applyVerify(model.getUid(), model.getImageUrl(), model.getAudioUrl());
    }

    /**
     * 修改认证状态
     * @param id
     * @param status
     * @param remark
     * @return
     * @throws Exception
     */
    @Override
    public boolean updateVerifiedStatus(Long id, VerifiedStatusEnum status, String remark) throws Exception
    {
        return accountService.updateVerifyApplyStatus(id, status, remark);
    }

    /**
     * 修改钻石数
     * @param uid
     * @param type
     * @param num
     * @return
     * @throws Exception
     */
    @Override
    public int updateDiamond(Long uid, int type, Long num) throws Exception
    {
        return accountService.updateDiamond(uid, type, num);
    }

    /**
     * 查询认证申请列表
     * @param dto
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public List<VerifiesApplyDto> findVerifyApplyList(VerifiesApplyDto dto, Integer pageNo, Integer pageSize) throws Exception
    {
        return accountService.findVerifyByCondition(dto, pageNo, pageSize);
    }

    @Override
    public long countVerifyApply(VerifiesApplyDto dto) throws Exception
    {
        return accountService.countVerifyByCondition(dto);
    }

    /**
     * 查询认证信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public VerifiesApplyDto findVerifyApplyById(Long id) throws Exception
    {
        return accountService.findVerifyApplyById(id);
    }

}
