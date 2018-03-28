package com.jwaoo.account.sevice;

import com.jwaoo.account.model.LoginHistory;
import com.jwaoo.account.mapper.LoginHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class LoginHistoryService
{

	@Autowired
	private LoginHistoryMapper loginHistoryMapper;

	public int saveLoginHistory(LoginHistory loginHistory)
	{
		loginHistory.setCreateTime(new Date());
		return loginHistoryMapper.insert(loginHistory);
	}

	public int save(LoginHistory loginHistory)
	{
		loginHistory.setCreateTime(new Date());
		return loginHistoryMapper.insertSelective(loginHistory);
	}

	public List<LoginHistory> findByAccountId(Long accountId)
	{
        List<LoginHistory> list = new ArrayList<>();
        if (accountId != null) {
            list = loginHistoryMapper.findByAccountId(accountId);
        }
		return list;
	}


}
