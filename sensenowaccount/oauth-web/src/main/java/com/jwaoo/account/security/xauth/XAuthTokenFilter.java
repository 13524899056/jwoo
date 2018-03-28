package com.jwaoo.account.security.xauth;

import com.alibaba.fastjson.JSON;
import com.jwaoo.account.model.Account;
import com.jwaoo.account.sevice.AccountService;
import com.jwaoo.common.core.utils.Result;
import com.jwaoo.common.core.utils.ResultCode;
import com.jwaoo.common.security.domain.MyUserDetail;
import com.jwaoo.common.security.dto.HeaderDto;
import com.jwaoo.common.security.token.provider.Token;
import com.jwaoo.common.security.token.service.TokenService;
import com.jwaoo.common.security.utils.AuthoritiesConstants;
import com.jwaoo.common.security.utils.ChannelUtils;
import com.jwaoo.common.security.utils.ClientUtils;
import com.jwaoo.common.security.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
public class XAuthTokenFilter extends GenericFilterBean {
    private final Logger log = LoggerFactory.getLogger(XAuthTokenFilter.class);

    /**
     * X-App-Client-Type: android/iOS VersionName VersionCode
     * 例如：
     * X-App-Client-Type: android 1.0.5 10005
     * X-App-Client-type: iOS 1.1.0 10010
     */

    private final AccountService userService;

    public XAuthTokenFilter(AccountService userService) {
        this.userService = userService;
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

            String uri = httpServletRequest.getRequestURI();
//            uri.matches("^/v[1-9]/registration/verification\\w+$");
//            if (!uri.startsWith("/v(1-9)/registration/verification")) {
            if (!uri.endsWith("/registration/verification/email") && !uri.startsWith("/oauth/resources") && !uri.startsWith("/oauth/v1/refresh/active") && !uri.startsWith("/oauth/v1/checkToken")) {
                // header valid
                HeaderDto headerDto = new HeaderDto(httpServletRequest);
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                Validator validator = factory.getValidator();
                Set<ConstraintViolation<HeaderDto>> constraintViolations = validator.validate(headerDto);

                if (constraintViolations != null && constraintViolations.size() > 0) {
                    returnJson(servletResponse, JSON.toJSONString(new Result<>(ResultCode.INVALID)));
                    return ;
                }
                //TODO valid client & channel
                CompletableFuture client = CompletableFuture.supplyAsync(() -> {
                    try{
                        return ClientUtils.findById(headerDto.getClient_id());
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                        return null;
                    }
                });
                CompletableFuture channel = CompletableFuture.supplyAsync(() -> {
                            try{
                                return ChannelUtils.findById(headerDto.getChannel_id());
                            }catch (Exception ex)
                            {
                                ex.printStackTrace();
                                return null;
                            }
                        }
                );
                if (client.get() == null || channel.get() == null) {
                    returnJson(servletResponse, JSON.toJSONString(new Result<>(ResultCode.INVALID)));
                    return ;
                }
                servletRequest.setAttribute(Constants.HEADER, headerDto);

                // valid token
                String authToken = httpServletRequest.getHeader(AuthoritiesConstants.XAUTH_TOKEN_HEADER_NAME);
                log.debug("get the x-auth-token is {}", authToken);
                if (StringUtils.isNotBlank(authToken)) {
                    Token token = TokenService.getInstance().findToken(authToken);
                    if (token != null) {
                        headerDto.setToken(authToken);
                        if (token.getId() != null) {
                            Account account = userService.findById(token.getId());
                            if (account != null) {
                                log.debug("userFromDatabase is not null");
                                MyUserDetail details = getAuths(account);
                                log.debug("details={}", details.toString());
                                if (TokenService.getInstance().validateToken(token, details)) {
                                    log.debug("token is ok");
                                    // final UsernamePasswordAuthenticationToken token = new
                                    // UsernamePasswordAuthenticationToken(details,
                                    // details.getPassword(), details.getAuthorities());
                                    PreAuthenticatedAuthenticationToken atoken = new PreAuthenticatedAuthenticationToken(details, details, details.getAuthorities());
                                    SecurityContextHolder.getContext().setAuthentication(atoken);
                                }
                            }
                        }
                    }
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception ex) {
            ex.printStackTrace();
            returnJson(servletResponse, JSON.toJSONString(new Result<>(ResultCode.API_ERROR)));
        }
    }

    private MyUserDetail getAuths(Account account) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        for (Authority authority : account.getAuthorities()) {
//            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
//            grantedAuthorities.add(grantedAuthority);
//        }
        return new MyUserDetail(account.getId(), account.getNickname(),account.getPassword(), account.getUuid(), account.getGender(), account.getAreaCode(), account.getAvatar(), account.getLevel(), grantedAuthorities);
    }

    private void returnJson(ServletResponse response, String json)
    {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
            logger.error("response error",e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
