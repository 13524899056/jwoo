package com.jwaoo.account.web.rest.dto;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

public class ActiveReqDto
{

    private static String REX_PUSHID = "^[0-9a-zA-Z_\\-:]{32,153}$";
    private static String REX_VOIPID = "^[0-9a-zA-Z]{64}$";
    private static String REX_CLIENTID = "^[0-9a-zA-Z]{24}$";
    private static String REX_ACTIVE = "^(0|1)$";

    @Max(1)
    private Integer active;

    private Integer uid;

    private String uuid;

    @Length(max = 24)
    @Pattern(regexp = "^[0-9a-zA-Z]{0,24}$")
    private String clientId;

//    @Length(max = 25)
    @NotNull
    @DecimalMin("-180")
    @DecimalMax("180")
    private Double longitude;

//    @Length(max = 25)
    @NotNull
    @DecimalMin("-90")
    @DecimalMax("90")
    private Double latitude;

    @Pattern(regexp = "^[0-9a-zA-Z_\\-:]{0,153}$")
    private String pushId;

    @Pattern(regexp = "^[0-9a-zA-Z]{0,64}$")
    private String voipId;

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getVoipId() {
        return voipId;
    }

    public void setVoipId(String voipId) {
        this.voipId = voipId;
    }


    public boolean validPushId()
    {
        boolean res = false;
        if (StringUtils.isNotBlank(pushId))
        {
            res = java.util.regex.Pattern.matches(REX_PUSHID, pushId);
        }
        return res;
    }

    public boolean validVoipId()
    {
        boolean res = false;
        if (StringUtils.isNotBlank(voipId))
        {
            res = java.util.regex.Pattern.matches(REX_VOIPID, voipId);
        }
        return res;
    }

    public boolean validClientId()
    {
        boolean res = false;
        if (StringUtils.isNotBlank(clientId))
        {
            res = java.util.regex.Pattern.matches(REX_CLIENTID, clientId);
        }
        return res;
    }

    public boolean validActive()
    {
        boolean res = false;
        if (active != null)
        {
            res = java.util.regex.Pattern.matches(REX_ACTIVE, active.toString());
        }
        return res;
    }

    public boolean validLongitude()
    {
        boolean res = false;
        if (longitude != null && longitude >= -180 && longitude <= 180)
        {
            res = true;
        }
        return res;
    }

    public boolean validLatitude()
    {
        boolean res = false;
        if (latitude != null && latitude >= -90 && latitude <= 90)
        {
            res = true;
        }
        return res;
    }

}
