package com.jwaoo.account.web.rest.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class ChangePasswordReqDto implements Serializable {

    private static final long serialVersionUID = -9155687608863796484L;

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{32}$")
    private String orgPassword;

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{32}$")
    private String newPassword;

    public String getOrgPassword() {
        return orgPassword;
    }

    public void setOrgPassword(String orgPassword) {
        this.orgPassword = orgPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    

}
