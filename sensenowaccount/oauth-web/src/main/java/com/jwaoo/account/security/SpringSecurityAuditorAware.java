package com.jwaoo.account.security;

import com.jwaoo.common.security.utils.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.jwaoo.account.config.Constants;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        Long userId = SecurityUtils.getCurrentUserId();
        return (userId != null ? String.valueOf(userId) : Constants.SYSTEM_ACCOUNT);
    }
}
