package com.jwaoo.account.web.rest.dto;

import com.jwaoo.common.core.dto.BaseListReqDto;

import javax.validation.constraints.NotEmpty;

/**
 * @author Jerry
 * @date 2017/8/15 12:06
 */
public class SearchReqDto extends BaseListReqDto
{

    @NotEmpty
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

}
