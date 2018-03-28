package com.jwaoo.account.sevice;

import com.jwaoo.account.dto.AccountDto;
import com.jwaoo.account.dto.AcctClientDto;
import com.jwaoo.account.web.rest.dto.UserDTO;
import com.jwaoo.common.core.config.Constants;
import com.jwaoo.common.core.config.Global;
import com.jwaoo.common.core.mongo.MongoDBUtil;
import com.jwaoo.common.core.mongo.Sort;
import com.jwaoo.common.core.utils.BeanMapDozer;
import com.jwaoo.common.core.utils.BeanUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Jerry
 * @date 2017/8/3 14:14
 */
public class AccountDtoUtil
{
    private static String dbName = Global.getConfigCfg("mongcfg:dbname", "");

    private static AccountDtoUtil instance = new AccountDtoUtil();

    private AccountDtoUtil(){}

    public static AccountDtoUtil getInstance()
    {
        return instance;
    }


    /**
     * 保存用户信息
     * @param dto
     * @throws Exception
     */
    public void save(AccountDto dto) throws Exception
    {
        if (dto != null)
        {
            Map<String, Object> map = BeanUtil.transBean2Map(dto);
            MongoDBUtil.add(map, dbName, AccountDto.TBL_ACCOUNT);
        }
    }

    /**
     * 修改用户信息
     * @param dto
     * @throws Exception
     */
    public void update(AccountDto dto) throws Exception
    {
        Map<String, Object> m = new HashMap<>();
        if (dto.get_id() != null)
        {
            m.put(AccountDto.ID, dto.get_id());
        }
        if (dto.getAccount_id() != null)
        {
            m.put(AccountDto.ACCOUNT_ID, dto.getAccount_id());
        }
        Map<String, Object> val = BeanUtil.transBean2Map(dto);
        val.remove(AccountDto.ID);
        val.remove(AccountDto.ACCOUNT_ID);
        MongoDBUtil.updateSet(m, val, dbName, AccountDto.TBL_ACCOUNT);
    }

    /**
     * 根据用户ID查询用户信息
     * @param accountId
     * @return
     * @throws Exception
     */
    public AccountDto findByAccountId(Long accountId) throws Exception
    {
        AccountDto dto = null;
        if (accountId != null)
        {
            Map<String, Object> map = new HashMap<>();
            map.put(AccountDto.ACCOUNT_ID, accountId);
            DBObject obj = MongoDBUtil.findOne(map, dbName, AccountDto.TBL_ACCOUNT);
            if (obj != null)
            {
                dto = BeanMapDozer.map(obj, AccountDto.class);
            }
        }
        return dto;
    }

    /**
     * 根据ID查询用户信息
     * @param uuid
     * @return
     * @throws Exception
     */
    public AccountDto findById(String uuid) throws Exception
    {
        AccountDto dto = null;
        if (StringUtils.isNotBlank(uuid))
        {
            Map<String, Object> map = new HashMap<>();
            map.put(AccountDto.ID, uuid);
            DBObject obj = MongoDBUtil.findOne(map, dbName, AccountDto.TBL_ACCOUNT);
            if (obj != null)
            {
                dto = BeanMapDozer.map(obj, AccountDto.class);
            }
        }
        return dto;
    }

    /**
     * 根据条件查询
     * @param dto
     * @return
     * @throws Exception
     */
    public AccountDto findOne(AccountDto dto) throws Exception
    {
        AccountDto accountDto = null;
        if (dto != null)
        {
            Map<String, Object> map = BeanUtil.transBean2Map(dto);
            DBObject obj = MongoDBUtil.findOne(map, dbName, AccountDto.TBL_ACCOUNT);
            if (obj != null)
            {
                accountDto = BeanMapDozer.map(obj, AccountDto.class);
            }
        }
        return accountDto;
    }

    /**
     * 根据条件查询用户列表
     * @param dto
     * @return
     * @throws Exception
     */
    public List<AccountDto> findList(AccountDto dto) throws Exception
    {
        List<AccountDto> list = new ArrayList<>();
        Map<String, Object> map = BeanUtil.transBean2Map(dto);
        List<DBObject> dblist = MongoDBUtil.find(map, null, dbName, AccountDto.TBL_ACCOUNT);
        if (dblist != null && dblist.size() > 0)
        {
//            dblist.parallelStream().forEach(obj -> list.add(BeanMapDozer.map(obj, AccountDto.class)));
            list = dblist.parallelStream().map(obj -> BeanMapDozer.map(obj, AccountDto.class)).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 根据用户ID列表查询用户列表
     * @param uids
     * @return
     * @throws Exception
     */
    public List<AccountDto> findByUids(List<Long> uids) throws Exception
    {
        List<AccountDto> list = new ArrayList<>();
        if (uids != null && uids.size() > 0)
        {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> whers = new HashMap<>();
            whers.put(Constants.MG_SQ_IN, uids);
            param.put(AccountDto.ACCOUNT_ID, whers);
            param.put(AccountDto.STATUS, Constants.MG_STATUS_VALID);
            List<DBObject> dblist = MongoDBUtil.find(param, null, dbName, AccountDto.TBL_ACCOUNT);
            if (dblist != null)
            {
//                dblist.parallelStream().forEach(obj -> list.add(BeanMapDozer.map(obj, AccountDto.class)));
                list = dblist.parallelStream().map(obj -> BeanMapDozer.map(obj, AccountDto.class)).collect(Collectors.toList());
            }
        }
        return list;
    }

    /**
     * 根据条件查询用户列表
     * @param dto
     * @param orderBy
     * @param pageSize
     * @param pageNo
     * @return
     * @throws Exception
     */
    public List<AccountDto> find(AccountDto dto, Sort orderBy, int pageSize, int pageNo) throws Exception
    {
        List<AccountDto> list = new ArrayList<>();
        Map<String, Object> map = BeanUtil.transBean2Map(dto);
        //TODO 模糊查询
        if (StringUtils.isNotBlank(dto.getNickname()))
        {
            map.put(AccountDto.NICKNAME, Pattern.compile(dto.getNickname().trim(), Pattern.CASE_INSENSITIVE));
        }
        if (StringUtils.isNotBlank(dto.getEmail()))
        {
            map.put(AccountDto.EMAIL, Pattern.compile(dto.getEmail().trim(), Pattern.CASE_INSENSITIVE));
        }
        if (StringUtils.isNotBlank(dto.getPhone()))
        {
            map.put(AccountDto.PHONE, Pattern.compile(dto.getPhone().trim(), Pattern.CASE_INSENSITIVE));
        }
        if (dto.getAccount_id() != null)
        {
            map.put(AccountDto.ACCOUNT_ID, dto.getAccount_id());
        }
        List<DBObject> dblist = MongoDBUtil.findList(dbName, AccountDto.TBL_ACCOUNT, map, orderBy, pageNo, pageSize);
        if (dblist != null && dblist.size() > 0)
        {
//            dblist.stream().forEach(obj -> list.add(BeanMapDozer.map(obj, AccountDto.class)));
            list = dblist.stream().map(obj -> BeanMapDozer.map(obj, AccountDto.class)).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 根据条件查询条数
     * @param dto
     * @return
     * @throws Exception
     */
    public long count(AccountDto dto) throws Exception
    {
        Map<String, Object> map = BeanUtil.transBean2Map(dto);
        //TODO 模糊查询
        if (StringUtils.isNotBlank(dto.getNickname()))
        {
            map.put(AccountDto.NICKNAME, Pattern.compile(dto.getNickname().trim(), Pattern.CASE_INSENSITIVE));
        }
        if (StringUtils.isNotBlank(dto.getEmail()))
        {
            map.put(AccountDto.EMAIL, Pattern.compile(dto.getEmail().trim(), Pattern.CASE_INSENSITIVE));
        }
        if (StringUtils.isNotBlank(dto.getPhone()))
        {
            map.put(AccountDto.PHONE, Pattern.compile(dto.getPhone().trim(), Pattern.CASE_INSENSITIVE));
        }
        if (dto.getAccount_id() != null)
        {
            map.put(AccountDto.ACCOUNT_ID, dto.getAccount_id());
        }
        long cut = MongoDBUtil.count(map, dbName, AccountDto.TBL_ACCOUNT);
        return cut;
    }


    /**
     * 根据昵称或ID搜索用户
     * @param query
     * @param num
     * @return
     * @throws Exception
     */
    public List<UserDTO> search (Long uid, String query, Integer num) throws Exception
    {
        List<UserDTO> list = new ArrayList<>();
        if (StringUtils.isNotBlank(query))
        {
            Map<String, Object> param = new HashMap<>();
            List orList = new ArrayList<>();
            orList.add(new BasicDBObject(AccountDto.NICKNAME, Pattern.compile(query.trim(), Pattern.CASE_INSENSITIVE)));
            if (NumberUtils.isNumber(query))
            {
                orList.add(new BasicDBObject(AcctClientDto.ACCOUNT_ID, Long.valueOf(query)));
            }
            if (uid != null && uid.longValue() > 0)
            {
                Map<String, Object> notMap = new HashMap<>();
                notMap.put(Constants.MG_SQ_NE, uid);
                param.put(AccountDto.ACCOUNT_ID, notMap);
            }
            param.put(Constants.MG_SQ_OR, orList);
//            List<DBObject> dblist = MongoDBUtil.find(param, null, dbName, AccountDto.TBL_ACCOUNT);
            List<DBObject> dblist = MongoDBUtil.findList(dbName, AccountDto.TBL_ACCOUNT, param, null, 1, num);
            if (dblist != null && dblist.size() > 0)
            {
                dblist.stream().forEach(
                        obj -> {
                            UserDTO dto = new UserDTO();
                            dto.setId(Long.valueOf(obj.get(AccountDto.ACCOUNT_ID).toString()));
                            dto.setUuid(obj.get(AccountDto.ID).toString());
                            dto.setNickname(obj.get(AccountDto.NICKNAME).toString());
                            dto.setAvatar(obj.get(AccountDto.AVART)!=null ? obj.get(AccountDto.AVART).toString() : StringUtils.EMPTY);
                            dto.setGender(Integer.valueOf(obj.get(AccountDto.GENDER).toString()));
                            dto.setBirthday(obj.get(AccountDto.BIRTHDAY)!=null ? LocalDate.parse(obj.get(AccountDto.BIRTHDAY).toString(), DateTimeFormatter.ISO_LOCAL_DATE) : null);
                            dto.setLevel(obj.get(AccountDto.LEVEL).toString());
//                            dto.setVip(Integer.valueOf(obj.get(AccountDto.VIP).toString()));
                            dto.setVipEndTime(obj.get(AccountDto.VIP_END_TIME)!=null ? new Date(obj.get(AccountDto.VIP_END_TIME).toString()) : null);
                            list.add(dto);
                        });
            }
        }
        return list;
    }

}
