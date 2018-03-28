package com.jwaoo.account.dto;

import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Jerry
 * @date 2017/7/11 11:39
 */
@SuppressWarnings("serial")
public class AcctClientDto implements Serializable
{

    public static final String TBL_ACCT_CLIENT = "acct_client";

    public static final String ID = "_id";
    public static final String UUID = "uuid";
    public static final String ACCOUNT_ID = "account_id";
    public static final String CLIENT_ID = "client_id";
    public static final String CHANNEL_ID = "channel_id";
    public static final String OS_TYPE = "os_type";
    public static final String OS_VERSION = "os_version";
    public static final String VERSION = "version";
    public static final String AREA = "area";
    public static final String LOC = "loc";
    public static final String TOKEN = "token";
    public static final String ACTIVE = "active";
    public static final String DEVICE_NO = "device_no";
    public static final String PUSH_TOKEN = "push_token";
    public static final String VOIP_TOKEN = "voip_token";
    public static final String LAST_ACTIVE_TIME = "last_active_time";
    public static final String LAST_TOKEN_TIME = "last_token_time";

    @Id
    private String _id;
    private String uuid;
    private Long account_id;
    private String channel_id;
    private String client_id;
    private Integer os_type;
    private String os_version;
    private String version;
    private String area;
    private Double[] loc;	//位置
    private String token;
    private Integer active;
    private String device_no;
    private String push_token;
    private String voip_token;
    private Date last_active_time;
    private Date last_token_time;
    private Date update_time;


    public AcctClientDto() {}

    public AcctClientDto(String id, Long accountId, String clientId, Integer osType, String pushToken, String token) {
        this._id = id;
        this.account_id = accountId;
        this.client_id = clientId;
        this.os_type = osType;
        this.push_token = pushToken;
        this.token = token;
    }

    public AcctClientDto(Long accountId, String channelId, String clientId, Integer osType, String deviceNo) {
        this.account_id = accountId;
        this.channel_id = channelId;
        this.client_id = clientId;
        this.os_type = osType;
        this.device_no = deviceNo;
    }

    public AcctClientDto(Long accountId, String channelId, String clientId, Integer osType, String deviceNo, Double[] loc, Integer active) {
        this.account_id = accountId;
        this.channel_id = channelId;
        this.client_id = clientId;
        this.os_type = osType;
        this.loc = loc;
        this.active = active;
        this.device_no = deviceNo;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public Integer getOs_type() {
        return os_type;
    }

    public void setOs_type(Integer os_type) {
        this.os_type = os_type;
    }

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String os_version) {
        this.os_version = os_version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Double[] getLoc() {
        return loc;
    }

    public void setLoc(Double[] loc) {
        this.loc = loc;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }

    public String getPush_token() {
        return push_token;
    }

    public void setPush_token(String push_token) {
        this.push_token = push_token;
    }

    public String getVoip_token() {
        return voip_token;
    }

    public void setVoip_token(String voip_token) {
        this.voip_token = voip_token;
    }

    public Date getLast_token_time() {
        return last_token_time;
    }

    public void setLast_token_time(Date last_token_time) {
        this.last_token_time = last_token_time;
    }

    public Date getLast_active_time() {
        return last_active_time;
    }

    public void setLast_active_time(Date last_active_time) {
        this.last_active_time = last_active_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

}
