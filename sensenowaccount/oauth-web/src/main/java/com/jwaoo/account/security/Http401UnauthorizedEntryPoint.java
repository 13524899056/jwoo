package com.jwaoo.account.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Returns a 401 error code (Unauthorized) to the client.
 */
@Component
public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    private final Logger log = LoggerFactory.getLogger(Http401UnauthorizedEntryPoint.class);

    /**
     * Always returns a 401 error code to the client.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2)
            throws IOException, ServletException {
        log.debug("Pre-authenticated entry point called. Rejecting access");
        //Exception e = new Exception("this is a log");
        log.info(arg2.getMessage());
        log.info(request.getRequestURI());
        log.info(request.getRequestURL().toString());
        //e.printStackTrace();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
    }
}
