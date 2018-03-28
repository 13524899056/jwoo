package com.jwaoo.account.web.filter.gzip;

import javax.servlet.ServletException;
@SuppressWarnings("all")
public class GzipResponseHeadersNotModifiableException extends ServletException {

    public GzipResponseHeadersNotModifiableException(String message) {
        super(message);
    }
}
