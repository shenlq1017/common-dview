package com.sucsoft.jt.acjtdview;

import com.cgs.dc.PojoDataset;
import com.cgs.dc.client.impl.PojoDatasetImpl;
import com.cgs.dc.client.utils.BeanAssign;
import com.cgs.dc.starter.model.CountObject;
import com.cgs.dc.starter.model.GetOptions;
import com.cgs.dc.starter.services.UnsafeCrudService;
import com.cgs.dc.starter.services.UnsafeQueryService;
import com.google.common.collect.Maps;
import com.sucsoft.ep.centerresponse.bean.PageVo;
import com.sucsoft.ep.centerresponse.enums.ExceptionMsg;
import com.sucsoft.ep.centerresponse.exception.server.DataDealError;
import com.sucsoft.ep.centerresponse.exception.server.QueryError;
import com.sucsoft.ep.centerresponse.util.JtBeanUtils;
import com.sucsoft.ep.centerresponse.util.JtPageUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * dc通用的类，增删改查类
 */
@Service
public class BasicQueryService {
    protected Logger logger = Logger.getLogger(getClass());
    /**
     * 查询
     */
    @Autowired
    private UnsafeQueryService queryService;
    /**
     * pojo 处理类
     */
    @Autowired
    private PojoDataset dataset;
    @Autowired
    private UnsafeCrudService crudService;

    @Autowired
    private PojoDatasetImpl datasetImpl;

    public PojoDataset getDataset() {
        return dataset;
    }


    public void setDataset(PojoDataset dataset) {
        this.dataset = dataset;
    }


    public UnsafeCrudService getCrudService() {
        return crudService;
    }


    public void setCrudService(UnsafeCrudService crudService) {
        this.crudService = crudService;
    }


    public UnsafeQueryService getQueryService() {
        return queryService;
    }


    public void setQueryService(UnsafeQueryService queryService) {
        this.queryService = queryService;
    }

    /**
     * ----------------------增删改查 ----------------------------
     */

    @Transactional(rollbackFor = Exception.class)
    public void save(Object o) {
        dataset.save(o, o.getClass().getName());
    }

    @Transactional(rollbackFor = Exception.class)
    public Serializable save(Map<String, Object> map, String entityName) {
        return crudService.save(map, entityName);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Object o) {
        dataset.update(o, o.getClass().getName());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Serializable id, Map<String, Object> map, String entityName) {
        crudService.update(id, map, entityName);
    }


    @Transactional(rollbackFor = Exception.class)
    public <R> void delete(Serializable id, Class<R> class_z) {
        dataset.remove(id, class_z.getName());
    }

    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public <R> R get(Serializable id, Class<R> class_z) {
        return dataset.get(id, class_z.getName(), class_z);
    }

    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public <R> Map<String, Object> getMap(Serializable id, Class<R> class_z, String... fetches) {
        return crudService.get(id, class_z.getName(), Arrays.asList(fetches));
    }

    /**
     * -----------------------查询-----------------------------
     */

    /**
     * 查询某个类所有数据，加上关联关系
     *
     * @param class_z
     * @param fetches
     * @return
     */
    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public <R> List<R> list(Class<R> class_z, String... fetches) {
        try {
            GetOptions options = new GetOptions();
            if (fetches.length > 0) {
                options.setExpands(Arrays.asList(fetches));
            }
            return crudService.list(class_z.getName(), options, class_z);
        } catch (Exception e) {
            throw new QueryError(String.format("{0}: 列举{1}",ExceptionMsg.QUERYERROR_MSG.value(),class_z.getName()),e);
        }
    }

    /**
     * 普通分页查询
     *
     * @param queryName hql或者sql名称
     * @param resultType 返回类型
     * @param pageNo 起始页码，从1开始
     * @param pageSize 每页数量
     * @return
     */
    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public <R> PageVo<R> queryForPage(String queryName, Map<String, Object> params, Class<R> resultType, Integer pageNo, Integer pageSize){
        List<R> list = queryForList(queryName, params, resultType, pageNo, pageSize);
        CountObject totalCount = countQuery(queryName, params);
        PageVo<R> page = new PageVo<R>(pageNo, pageSize, totalCount.getCount(), list);
        return page;
    }

    /**
     * 返回一个vo对象
     * @param queryName hql或者sql名称
     * @param params 参数
     * @param resultType 对象类型
     * @param pageNo 起始页码，从1开始
     * @param pageSize 每页数量
     * @return
     */
    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public <R> PageVo<R> queryForObjectPage(String queryName, Map<String, Object> params, Class<R> resultType, Integer pageNo, Integer pageSize){
        List<R> list = queryForObjectList(queryName, params, resultType, pageNo, pageSize);
        CountObject totalCount = countQuery(queryName, params);
        PageVo<R> page = new PageVo<R>(pageNo, pageSize, totalCount.getCount(), list);
        return page;
    }

    /**
     * 查询所有，不分页，不取总数
     * @param queryName  hql.sql的名称
     * @param params 参数
     * @param resultType 返回类型
     * @param <R>
     * @return
     */
    public <R> List<R> queryForList(String queryName, Map<String, Object> params, Class<R> resultType) {
        return queryForList(queryName, params, resultType, 0, 0);
    }

    /**
     * 查询一个sql的总量
     * @param queryName hql.sql的名称
     * @param params 参数
     * @return
     */
    public CountObject countQuery(String queryName, Map<String, Object> params) {
        try {
            CountObject totalCount = queryService.restQueryCount(queryName, params);
            return totalCount;
        } catch (Exception e) {
            throw new QueryError(String.format("{0}: queryName:{1}",ExceptionMsg.QUERYERROR_MSG.value(),queryName),e);
        }
    }

    /**
     * 普通查询,不带总数，但可以指定查询第几页到第几页
     *
     * @param queryName
     * @param params
     * @param resultType
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public <R> List<R> queryForList(String queryName, Map<String, Object> params, Class<R> resultType, Integer pageNo, Integer pageSize) {
        try {
            Integer first = JtPageUtils.getFirst(pageNo, pageSize);
            List<R> list = queryService.query(queryName, params, resultType, first, pageSize);
            return list;
        } catch (Exception e) {
            throw new QueryError(String.format("{0}: queryName:{1}",ExceptionMsg.QUERYERROR_MSG.value(),queryName),e);
        }
    }


    /**
     * 带fetches的分页，返回map
     *
     * @param queryName  方法名
     * @param params     参数
     * @param resultType 返回类型（调用方法后 的返回类型，最终返回的是map）
     * @param pageNo     第几页
     * @param pageSize   每页数量
     * @param fetches    关联表
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public <R> PageVo<Map<String, Object>> queryForPageAsMap
    (String queryName, Map<String, Object> params, Class<R> resultType, Integer pageNo, Integer pageSize, String... fetches){

        List<R> list = queryForList(queryName, params, resultType, pageNo, pageSize);
        CountObject totalCount = countQuery(queryName, params);
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            for (R r : list) {
                result.add(BeanAssign.beanToMap(r, resultType.getName(), dataset, "", Arrays.asList(fetches)));
            }
        } catch (Exception e) {
            throw new DataDealError(String.format("{0}: queryName:{1}",ExceptionMsg.DATADEALERROR_MSG.value(),queryName), e);
        }
        PageVo<Map<String, Object>> page = new PageVo<Map<String, Object>>(pageNo, pageSize, totalCount.getCount(), result);
        return page;
    }

    /**
     * 通用方法，参数统一用condition表示，用于单个参数或者无参数，返回所有
     *
     * @param queryName  方法名
     * @param resultType 返回类型
     * @param condition  参数
     * @return
     * @throws Exception
     */
    public <R> List<R> findByCondition(String queryName, Class<R> resultType, Object... condition) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        if (condition.length > 0) {
            for (int i = 0; i < condition.length; i++) {
                if (condition[i] != null && !StringUtils.isEmpty(condition[i].toString())) {
                    params.put("condition" + i, condition[i]);
                }
            }
        }
        List<R> list = queryForList(queryName, params, resultType, 0, 0);
        return list;
    }


    /**
     * 抽取unsafeQueryService.query()方法 调用预定义查询，方便异常处理
     *
     * @param queryName        查询名
     * @param params           参数
     * @param resultType       泛型类型
     * @param pageNo           第几页
     * @param pageSize         每页数量
     * @param <T>              泛型
     * @return 结果集
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> queryForObjectList(String queryName, Map<String, Object> params, Class<T> resultType, Integer pageNo, Integer pageSize) {
        List<T> queryDataList = queryForList(queryName, params, resultType, pageNo, pageSize);
        //如果没有返回记录，或者期待类型是Map，或者已经转化为其他类型，那就不要再转型了
        if (queryDataList.size() == 0 || Map.class.isAssignableFrom(resultType) || !(queryDataList.get(0) instanceof Map)) {
            return queryDataList;
        }
        try {
            List<Map> mapList = (List<Map>) queryDataList;
            List<T> dataList = JtBeanUtils.listMapToBeanSimple(mapList, resultType,new HashMap<>());
            return dataList;
        } catch (Exception e) {
            throw new QueryError(String.format("{0}: queryName:{1}",ExceptionMsg.QUERYERROR_MSG.value(),queryName),e);
        }
    }

    /**
     * *返回List<T>的query方法
     *
     * @param queryName        查询名
     * @param params           参数
     * @param resultType       返回类型
     * @return List<T>结果集
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> queryForObjectList(String queryName, Map<String, Object> params, Class<T> resultType) {
        return queryForObjectList(queryName, params, resultType, 0, 0);
    }

    /**
     * 不带参数，并返回List<T>的query方法
     *
     * @param <T>              泛型
     * @param queryName        查询名
     * @return List<T>结果集
     */
    public <T> List<T> queryForObjectList(String queryName, Class<T> resultType) {
        return queryForObjectList(queryName, Maps.newHashMap(), resultType);
    }

    /**
     * 返回List<Map>的query方法
     *
     * @param queryName        查询名
     * @param params           参数
     * @return List<Map>结果集
     */
    public List<Map> queryForObjectList(String queryName, Map<String, Object> params) {
        return queryForObjectList(queryName, params, Map.class, 0, 0);
    }

    /**
     * 不带参数，并返回List<Map>的query方法
     *
     * @param queryName        查询名
     * @return List<Map>结果集
     */
    public List<Map> queryForObjectList(String queryName) {
        return queryForObjectList(queryName, Maps.newHashMap());
    }

}
