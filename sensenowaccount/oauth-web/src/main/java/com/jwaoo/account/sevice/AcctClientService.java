package com.jwaoo.account.sevice;

import com.jwaoo.account.dto.AccountDto;
import com.jwaoo.account.dto.AcctClientDto;
import com.jwaoo.account.web.rest.dto.NearByResDto;
import com.jwaoo.common.core.config.Constants;
import com.jwaoo.common.core.config.Global;
import com.jwaoo.common.core.mongo.MongoDBUtil;
import com.jwaoo.common.core.pagination.Page;
import com.jwaoo.common.core.utils.BeanMapDozer;
import com.jwaoo.common.core.utils.BeanUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.stream.Collectors;


public class AcctClientService
{

    private static String dbName = Global.getConfigCfg("mongcfg:dbname", "");

    private static AcctClientService instance = new AcctClientService();

    private AcctClientService() {}

	public static AcctClientService getInstance()
	{
		return instance;
	}


    /**
     * 保存
     * @param dto
     * @return
     */
	public void save (AcctClientDto dto) throws Exception
    {
		if (dto != null)
		{
            dto.setUpdate_time(new Date());
            Map<String, Object> map = BeanUtil.transBean2Map(dto);
            map.remove(AcctClientDto.ID);
            MongoDBUtil.add(map, dbName, AcctClientDto.TBL_ACCT_CLIENT);
		}
	}

    /**
     * 修改
     * @param dto
     * @throws Exception
     */
	public void update (AcctClientDto dto) throws Exception
    {
        Map<String, Object> m = new HashMap<>();
        m.put(AcctClientDto.ID, new ObjectId(dto.get_id()));
        dto.setUpdate_time(new Date());
        Map<String, Object> val = BeanUtil.transBean2Map(dto);
        val.remove(AcctClientDto.ID);
        MongoDBUtil.updateSet(m, val, dbName, AcctClientDto.TBL_ACCT_CLIENT);
	}

    /**
     * clear old push bind
     * @param dto
     * @throws Exception
     */
    public void clearPushToken (AcctClientDto dto) throws Exception
    {
        Map<String, Object> m = new HashMap<>();
        m.put(AcctClientDto.CLIENT_ID, dto.getClient_id());
        m.put(AcctClientDto.OS_TYPE, dto.getOs_type());
        if (StringUtils.isNotBlank(dto.getPush_token()))
        {
            m.put(AcctClientDto.PUSH_TOKEN, dto.getPush_token());
        }
        if (StringUtils.isNotBlank(dto.getToken()))
        {
            m.put(AcctClientDto.TOKEN, dto.getToken());
        }
        if (StringUtils.isNotBlank(dto.get_id())) {
            Map<String, Object> nemap = new HashMap<>();
            nemap.put(Constants.MG_SQ_NE, new ObjectId(dto.get_id()));
            m.put(AcctClientDto.ID, nemap);
        }
        Map<String, Object> val = new HashMap<>();
        val.put(AcctClientDto.PUSH_TOKEN, StringUtils.EMPTY);
        val.put(AcctClientDto.VOIP_TOKEN, StringUtils.EMPTY);
        MongoDBUtil.updateSet(m, val, dbName, AcctClientDto.TBL_ACCT_CLIENT);
    }

	/**
	 * 根据ID 查询
	 * @param id
	 * @return
	 */
	public AcctClientDto findById(String id) throws Exception
    {
        AcctClientDto dto = null;
		if (StringUtils.isNotBlank(id))
		{
            DBObject obj = MongoDBUtil.findById(id, dbName, AcctClientDto.TBL_ACCT_CLIENT);
            if (obj != null) {
                dto = BeanMapDozer.map(obj, AcctClientDto.class);
            }
		}
		return dto;
	}

    /**
     * 根据accountId 查询
     * @param accountId
     * @param clientId
     * @return
     */
	public AcctClientDto findByAccountId(Long accountId, String clientId) throws Exception
    {
        AcctClientDto dto = null;
		if (accountId != null)
		{
            Map<String, Object> map = new HashMap<>();
            map.put(AcctClientDto.ACCOUNT_ID, accountId);
            map.put(AcctClientDto.CLIENT_ID, clientId);
            DBObject obj = MongoDBUtil.findOne(map, dbName, AcctClientDto.TBL_ACCT_CLIENT);
            if (obj != null)
            {
                dto = BeanMapDozer.map(obj, AcctClientDto.class);
            }
		}
		return dto;
	}

    /**
     * 根据uuid查询
     * @param uuid
     * @param clientId
     * @return
     */
	public AcctClientDto findByUuid(String uuid, String clientId) throws Exception
    {
        AcctClientDto dto = null;
		if (StringUtils.isNotBlank(uuid))
		{
            Map<String, Object> map = new HashMap<>();
            map.put(AcctClientDto.UUID, uuid);
            map.put(AcctClientDto.CLIENT_ID, clientId);
            DBObject obj = MongoDBUtil.findOne(map, dbName, AcctClientDto.TBL_ACCT_CLIENT);
            if (obj != null) {
                dto = BeanMapDozer.map(obj, AcctClientDto.class);
            }
		}
		return dto;
	}

    /**
     * 查询所有
     * @return
     */
    public List<AcctClientDto> findAll() throws Exception
    {
        List<AcctClientDto> list = new ArrayList<>();
        List<DBObject> dblist = MongoDBUtil.findAll(null, dbName, AcctClientDto.TBL_ACCT_CLIENT);
        if (dblist != null && dblist.size() > 0)
        {
//            dblist.parallelStream().forEach(obj -> list.add(BeanMapDozer.map(obj, AcctClientDto.class)));
            list = dblist.parallelStream().map(obj -> BeanMapDozer.map(obj, AcctClientDto.class)).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 查询分页
     * @param dto
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<AcctClientDto> pageQuery(AcctClientDto dto, int pageNo, int pageSize) throws Exception
    {
        Page<AcctClientDto> page = new Page<>();
        Map<String, Object> param = new HashMap<String, Object>();
        if (dto != null) {
            param = BeanUtil.transBean2Map(dto);
        }
        Page<DBObject> pageObj = MongoDBUtil.pageQuery(dbName, AcctClientDto.TBL_ACCT_CLIENT, param, pageNo, pageSize);
        if (pageObj != null && pageObj.getTotalCount() > 0)
        {
//            List<AcctClientDto> list = new ArrayList<>();
//            pageObj.getResultList().parallelStream().forEach(obj -> list.add(BeanMapDozer.map(obj, AcctClientDto.class)));
            List<AcctClientDto> list = pageObj.getResultList().parallelStream().map(obj -> BeanMapDozer.map(obj, AcctClientDto.class)).collect(Collectors.toList());
            page.setResultList(list);
            page.setTotalCount(pageObj.getTotalCount());
        }
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        return page;
    }

    /**
     * 根据UUID或AccountID查询
     * @param uuid
     * @param accountId
     * @return
     */
    public List<AcctClientDto> findByUuidOrAccountId(String uuid, Long accountId) throws Exception
    {
        List<AcctClientDto> list = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        if (accountId != null)
        {
            param.put(AcctClientDto.ACCOUNT_ID, accountId);
        }
        if (StringUtils.isNotBlank(uuid))
        {
            param.put(AcctClientDto.UUID, uuid);
        }
        List<DBObject> dblist = MongoDBUtil.find(param, null, dbName, AcctClientDto.TBL_ACCT_CLIENT);
        if (dblist != null && dblist.size() > 0) {
//            dblist.parallelStream().forEach(obj -> list.add(BeanMapDozer.map(obj, AcctClientDto.class)));
            list = dblist.parallelStream().map(obj -> BeanMapDozer.map(obj, AcctClientDto.class)).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 根据用户ID查询推送Token
     * @param clientId
     * @param uids
     * @return
     * @throws Exception
     */
    public ArrayList<AcctClientDto> findPushIdsByAccountId(String clientId, List<Long> uids) throws Exception
    {
        List<AcctClientDto> list = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> where = new HashMap<>();
        where.put(Constants.MG_SQ_IN, uids);
        param.put(AcctClientDto.ACCOUNT_ID, where);
        param.put(AcctClientDto.CLIENT_ID, clientId);
        List<DBObject> dblist = MongoDBUtil.find(param, null, dbName, AcctClientDto.TBL_ACCT_CLIENT);
        if (dblist != null) {
//            dblist.parallelStream().forEach(obj -> {
//                obj.removeField(AcctClientDto.CLIENT_ID);
//                obj.removeField(AcctClientDto.CHANNEL_ID);
//                obj.removeField(AcctClientDto.TOKEN);
//                obj.removeField(AcctClientDto.LOC);
//                list.add(BeanMapDozer.map(obj, AcctClientDto.class));
//            });
            list = dblist.parallelStream().map(obj -> {
                obj.removeField(AcctClientDto.CLIENT_ID);
                obj.removeField(AcctClientDto.CHANNEL_ID);
                obj.removeField(AcctClientDto.TOKEN);
                obj.removeField(AcctClientDto.LOC);
                obj.removeField(AcctClientDto.ID);
                obj.removeField(AcctClientDto.ACTIVE);
                obj.removeField(AcctClientDto.AREA);
                obj.removeField(AcctClientDto.LAST_ACTIVE_TIME);
                obj.removeField(AcctClientDto.LAST_TOKEN_TIME);
                obj.removeField(AcctClientDto.OS_VERSION);
                obj.removeField(AcctClientDto.UUID);
                obj.removeField(AcctClientDto.VERSION);
                return BeanMapDozer.map(obj, AcctClientDto.class);
            }).collect(Collectors.toList());
        }
        return (ArrayList) list;
    }

    /**
     * 根据位置查询距离内的用户列表（根据距离排序，但无距离信息）
     * @param dto
     * @param distance 单位km
     * @param limit
     * @return
     * @throws Exception
     */
    public List<AcctClientDto> findByNear(AcctClientDto dto, Integer distance, Integer limit) throws Exception
    {
        List<AcctClientDto> list = new ArrayList<>();
        if (dto != null && StringUtils.isNotBlank(dto.getClient_id()) && dto.getLoc() != null) {
            Map<String, Object> where = new HashMap<>();
//        where.put(Constants.MG_SQ_NEAR, loc);
//        where.put(Constants.MG_SQ_MAXDISTANCE, distance/Constants.MG_GEO_UNITS);
            where.put(Constants.MG_SQ_NEARSPHERE, dto.getLoc());
            where.put(Constants.MG_SQ_MAXDISTANCE, distance/Constants.MG_GEO_RADIANS);
            Map<String, Object> param = BeanUtil.transBean2Map(dto);
//            param.put(AcctClientDto.CLIENT_ID, dto.getClient_id());
            param.put(AcctClientDto.LOC, where);
            if (dto.getAccount_id() != null && dto.getAccount_id().longValue() > 0)
            {
                Map<String, Object> notMap = new HashMap<>();
                notMap.put(Constants.MG_SQ_NE, dto.getAccount_id());
                param.remove(AcctClientDto.ACCOUNT_ID);
                param.put(AcctClientDto.ACCOUNT_ID, notMap);
            }
//        Sort orderBy = new Sort();
            List<DBObject> dblist = MongoDBUtil.findList(dbName, AcctClientDto.TBL_ACCT_CLIENT, param, null, 1, limit);
            if (dblist != null) {
//                dblist.stream().forEach(obj -> {
//                    obj.removeField(AcctClientDto.CLIENT_ID);
//                    obj.removeField(AcctClientDto.CHANNEL_ID);
//                    obj.removeField(AcctClientDto.TOKEN);
//                    obj.removeField(AcctClientDto.PUSH_TOKEN);
//                    obj.removeField(AcctClientDto.LAST_TOKEN_TIME);
//                    list.add(BeanMapDozer.map(obj, AcctClientDto.class));
//                });
                list = dblist.stream().map(obj -> {
                    obj.removeField(AcctClientDto.CLIENT_ID);
                    obj.removeField(AcctClientDto.CHANNEL_ID);
                    obj.removeField(AcctClientDto.TOKEN);
                    obj.removeField(AcctClientDto.PUSH_TOKEN);
                    obj.removeField(AcctClientDto.LAST_TOKEN_TIME);
                    return BeanMapDozer.map(obj, AcctClientDto.class);
                }).collect(Collectors.toList());
            }
        }
        return list;
    }

    /**
     * 根据位置查询距离内的用户列表（不根据距离排序）
     * @param dto
     * @param distance 单位km
     * @param limit
     * @return
     * @throws Exception
     */
    public List<AcctClientDto> findByWithin(AcctClientDto dto, Integer distance, Integer limit) throws Exception
    {
        List<AcctClientDto> list = new ArrayList<>();
        if (dto != null && StringUtils.isNotBlank(dto.getClient_id()) && dto.getLoc() != null) {
            Map<String, Object> param = BeanUtil.transBean2Map(dto);
            param.put(AcctClientDto.LOC, new BasicDBObject(Constants.MG_SQ_GEOWITHIN, new BasicDBObject(Constants.MG_SQ_CENTERSPHERE, Arrays.asList(dto.getLoc(), distance/Constants.MG_GEO_RADIANS))));
//        Sort orderBy = new Sort();
            List<DBObject> dblist = MongoDBUtil.findList(dbName, AcctClientDto.TBL_ACCT_CLIENT, param, null, 1, limit);
            if (dblist != null) {
//                dblist.parallelStream().forEach(obj -> {
//                    obj.removeField(AcctClientDto.CLIENT_ID);
//                    obj.removeField(AcctClientDto.CHANNEL_ID);
//                    obj.removeField(AcctClientDto.TOKEN);
//                    obj.removeField(AcctClientDto.PUSH_TOKEN);
//                    obj.removeField(AcctClientDto.LAST_TOKEN_TIME);
//                    list.add(BeanMapDozer.map(obj, AcctClientDto.class));
//                });
                list = dblist.parallelStream().map(obj -> {
                    obj.removeField(AcctClientDto.CLIENT_ID);
                    obj.removeField(AcctClientDto.CHANNEL_ID);
                    obj.removeField(AcctClientDto.TOKEN);
                    obj.removeField(AcctClientDto.PUSH_TOKEN);
                    obj.removeField(AcctClientDto.LAST_TOKEN_TIME);
                    return BeanMapDozer.map(obj, AcctClientDto.class);
                }).collect(Collectors.toList());
            }
        }
        return list;
    }

    public List<NearByResDto> convertGeoResult(List<AcctClientDto> acctClientDtos) throws Exception
    {
        List<NearByResDto> list = new ArrayList<>();
        if (acctClientDtos != null && acctClientDtos.size()>0)
        {
//            List<Long> uids = new ArrayList<>();
//            acctClientDtos.parallelStream().forEach(obj -> uids.add(obj.getAccount_id()));
            List<Long> uids = acctClientDtos.parallelStream().map(AcctClientDto::getAccount_id).collect(Collectors.toList());
//            List<Long> uids = acctClientDtos.parallelStream().mapToLong(AcctClientDto::getAccount_id).collect(ArrayList::new, List::add, List::addAll);
            List<AccountDto> alist = AccountDtoUtil.getInstance().findByUids(uids);
            acctClientDtos.stream().forEach(obj -> {
                Optional<AccountDto> dto = alist.parallelStream().filter(u -> u.getAccount_id().equals(obj.getAccount_id())).findFirst();
                if (dto.isPresent())
                {
                    list.add(new NearByResDto(obj.get_id(), obj.getAccount_id(), obj.getOs_type(), obj.getLoc(), obj.getActive(), dto.get().getNickname(), dto.get().getAvatar(), dto.get().getGender(), dto.get().getLevel(), dto.get().getIs_verified(), dto.get().getVip_end_time(), obj.getLast_active_time(), obj.getUpdate_time()));
                }
            });
        }
        return list;
    }
}
