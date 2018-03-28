package com.jwaoo.account.dto;

import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class AccountDto implements Serializable
{

    public static final String TBL_ACCOUNT = "acct";

    public static final String ID = "_id";
    public static final String ACCOUNT_ID = "account_id";
//    public static final String UUID = "uuid";
    public static final String NICKNAME = "nickname";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String GENDER = "gender";
    public static final String BIRTHDAY = "birthday";
    public static final String LEVEL = "level";
    public static final String AREA = "area_code";
    public static final String OPEN_ID = "open_id";
    public static final String OAUTH_TYPE = "oauth_type";
    public static final String AVART = "avatar";
    public static final String IS_ONLINE = "is_online";
    public static final String SSN_NO = "ssn_no";
    public static final String STATUS = "status";
    public static final String IS_VERIFIED = "is_verified";
    public static final String VIP_END_TIME = "vip_end_time";
//    public static final String VIP = "vip";
    public static final String DIAMOND = "diamond";
    public static final String COIN = "coin";
    public static final String F_COIN = "f_coin";
//    public static final String DEVICE_NO = "device_no";
    public static final String LAST_LOGIN_TIME = "last_login_time";
    public static final String LAST_ONLINE_TIME = "last_online_time";
    public static final String FREEZE_END_TIME = "freeze_end_time";
//    public static final String LOCATION = "location";
    public static final String CREATE_TIME = "create_time";

    private Long account_id;

    @Id
    private String _id;

    private String nickname;

    private String name;

    private String email;

    private String phone;

    private Integer gender;

    private String area_code;

    private String avatar;

    private String birthday;
//    private LocalDate birthday;

    private String instr;

    private Integer level;

    private Long exp;

    private Long integral;

    private String address;

    private String open_id;

    private Integer oauth_type;

    private Date freeze_end_time;

    private Integer status;

    private Integer is_online;

    private String ssn_no;

    private String pay_password;

//    private Integer vip;

    private Date vip_end_time;

    private Integer is_verified;

    private Integer gold;

    private Long coin;

    private Long f_coin;

    private Long balance;

    private Double[] location;	//经度

//    private String device_no;

//    private Integer channel_id;
//
//    private String client_id;

//    private Integer os_type;

    private Date last_login_time;

    private Date last_online_time;

    private Date create_time;

//    private Date updateTime;


    public AccountDto() {}

    public AccountDto(Long account_id, String _id, String nickname, String name, String email, String phone, Integer gender, String area_code, String avatar, String birthday, String instr, Integer level, Long exp, Long integral, String address, String open_id, Integer oauth_type, Date freeze_end_time, Integer status, Integer is_online, String ssn_no, String pay_password, Date vip_end_time, Integer is_verified, Integer gold, Long coin, Long f_coin, Long balance, Double[] location, Date last_login_time, Date last_online_time, Date create_time) {
        this.account_id = account_id;
        this._id = _id;
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.area_code = area_code;
        this.avatar = avatar;
        this.birthday = birthday;
        this.instr = instr;
        this.level = level;
        this.exp = exp;
        this.integral = integral;
        this.address = address;
        this.open_id = open_id;
        this.oauth_type = oauth_type;
        this.freeze_end_time = freeze_end_time;
        this.status = status;
        this.is_online = is_online;
        this.ssn_no = ssn_no;
        this.pay_password = pay_password;
        this.vip_end_time = vip_end_time;
        this.is_verified = is_verified;
        this.gold = gold;
        this.coin = coin;
        this.f_coin = f_coin;
        this.balance = balance;
        this.location = location;
        this.last_login_time = last_login_time;
        this.last_online_time = last_online_time;
        this.create_time = create_time;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

//    public String getUuid() {
//        return uuid;
//    }
//
//    public void setUuid(String uuid) {
//        this.uuid = uuid;
//    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getInstr() {
        return instr;
    }

    public void setInstr(String instr) {
        this.instr = instr;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public Long getIntegral() {
        return integral;
    }

    public void setIntegral(Long integral) {
        this.integral = integral;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

//    public Integer getVip() {
//        return vip;
//    }
//
//    public void setVip(Integer vip) {
//        this.vip = vip;
//    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public Long getCoin() {
        return coin;
    }

    public void setCoin(Long coin) {
        this.coin = coin;
    }

    public Long getF_coin() {
        return f_coin;
    }

    public void setF_coin(Long f_coin) {
        this.f_coin = f_coin;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

//    public String getDevice_no() {
//        return device_no;
//    }
//
//    public void setDevice_no(String device_no) {
//        this.device_no = device_no;
//    }
//
//    public Integer getOs_type() {
//        return os_type;
//    }
//
//    public void setOs_type(Integer os_type) {
//        this.os_type = os_type;
//    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public Integer getOauth_type() {
        return oauth_type;
    }

    public void setOauth_type(Integer oauth_type) {
        this.oauth_type = oauth_type;
    }

    public Date getFreeze_end_time() {
        return freeze_end_time;
    }

    public void setFreeze_end_time(Date freeze_end_time) {
        this.freeze_end_time = freeze_end_time;
    }

    public Integer getIs_online() {
        return is_online;
    }

    public void setIs_online(Integer is_online) {
        this.is_online = is_online;
    }

    public String getSsn_no() {
        return ssn_no;
    }

    public void setSsn_no(String ssn_no) {
        this.ssn_no = ssn_no;
    }

    public String getPay_password() {
        return pay_password;
    }

    public void setPay_password(String pay_password) {
        this.pay_password = pay_password;
    }

    public Date getVip_end_time() {
        return vip_end_time;
    }

    public void setVip_end_time(Date vip_end_time) {
        this.vip_end_time = vip_end_time;
    }

    public Integer getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(Integer is_verified) {
        this.is_verified = is_verified;
    }

    public Date getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(Date last_login_time) {
        this.last_login_time = last_login_time;
    }

    public Date getLast_online_time() {
        return last_online_time;
    }

    public void setLast_online_time(Date last_online_time) {
        this.last_online_time = last_online_time;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date createTime) {
        this.create_time = createTime;
    }

}
