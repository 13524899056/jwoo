package com.jwaoo.account.web.rest.dto;

import javax.validation.constraints.Size;
import java.io.Serializable;

@SuppressWarnings("serial")
public class AuthorizeReqDto implements Serializable {


    @Size(max = 10)
    private String osType;

    @Size(max = 20)
    private String osVersion;

    @Size(max = 17)
    private String mac; // 系统WiFi Mac地址

    @Size(max = 32)
    private String clientId;

    @Size(max = 10)
    private String channelId;

    @Size(max = 20)
    private String version;

    @Size(max = 200)
    private String hardWare;

    @Size(max = 40)
    private String deviceNo;

    @Size(max = 10)
    private String networkMode;

    @Size(max = 100)
    private String hedno;

    private String userAgent;


    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public String getChannelId()
    {
        return channelId;
    }

    public void setChannelId(String channelId)
    {
        this.channelId = channelId;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHardWare() {
        return hardWare;
    }

    public void setHardWare(String hardWare) {
        this.hardWare = hardWare;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getNetworkMode() {
        return networkMode;
    }

    public void setNetworkMode(String networkMode) {
        this.networkMode = networkMode;
    }

    public String getHedno() {
        return hedno;
    }

    public void setHedno(String hedno) {
        this.hedno = hedno;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
