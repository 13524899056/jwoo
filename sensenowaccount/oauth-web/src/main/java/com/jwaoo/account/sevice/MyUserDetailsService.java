package com.jwaoo.account.sevice;

import com.jwaoo.account.mapper.AccountMapper;
import com.jwaoo.account.model.Account;
import com.jwaoo.account.utils.UserStatusEnum;
import com.jwaoo.common.security.domain.MyUserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
@SuppressWarnings("all")
public class MyUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Autowired
    private AccountMapper userMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        log.debug("Authenticating {}", username);
        //User userFromDatabase = userRepository.findOneByLoginName(username);
        Account userFromDatabase = userMapper.findByAccount(username);
        if (userFromDatabase == null) {
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }
        return getAuths(userFromDatabase);
    }

    @Transactional
    public MyUserDetail loadUserById(Long accountId) {
        log.debug("Authenticating by id {}", accountId);
        //User userFromDatabase = userRepository.findOne(integer);
        Account userFromDatabase = userMapper.findById(accountId);
        if (userFromDatabase == null) {
            throw new UsernameNotFoundException("User " + accountId + " was not found in the database");
        }
        return getAuths(userFromDatabase);
    }

    private MyUserDetail getAuths(Account user) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        for (Authority authority : user.getAuthorities()) {
//            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
//            grantedAuthorities.add(grantedAuthority);
//        }
        return new MyUserDetail(user.getId(), user.getNickname(), user.getPassword(), user.getUuid(), user.getGender(), user.getAreaCode(), user.getAvatar(), user.getLevel(), (user.getStatus() == UserStatusEnum.NORMAL.getValue() && (user.getFreezeEndTime() == null || user.getFreezeEndTime().getTime()<System.currentTimeMillis()))?true:false, true, true, user.getStatus()== UserStatusEnum.LOCKED.getValue()?false:true, grantedAuthorities);
    }

}
