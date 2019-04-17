package com.sucsoft.jt.acjtdview.service.dcrun

import com.cgs.dc.PojoDataset
import com.cgs.dc.starter.model.GetOptions
import com.cgs.dc.starter.services.UnsafeCrudService
import com.cgs.dc.starter.services.UnsafeQueryService
import com.sucsoft.jt.acjtdview.enums.ExceptionMsg
import com.sucsoft.jt.acjtdview.exception.server.QueryError
import com.sucsoft.jt.acjtdview.service.builder.ExeParameter
import com.sucsoft.jt.acjtdview.service.builder.SqlExecuteHandler
import com.sucsoft.jt.acjtdview.util.JtDviewBeanUtils
import com.sucsoft.jt.acjtdview.util.JtPageUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable
import java.util.*


/**
 * @author shenlq
 * @description dc的查询实现
 * @date 2019-03-30 11:55
 */
@Service
class DcSqlExcute : SqlExecuteHandler {

    @Autowired
    lateinit var uqs: UnsafeQueryService

    /**
     * pojo 处理类
     */
    @Autowired
    lateinit var dataset: PojoDataset
    @Autowired
    lateinit var crudService: UnsafeCrudService

    override fun execute(exeParameter: ExeParameter): Any {
        return uqs.query(exeParameter.callName, exeParameter.params,exeParameter.returnClass,paramPageFirst(exeParameter),exeParameter.pageSize)
    }

    /**
     * 查询
     */
    override fun <T> query(exeParameter: ExeParameter, classz: Class<T>): List<T> {
        return uqs.query(exeParameter.callName, exeParameter.params,classz,paramPageFirst(exeParameter),exeParameter.pageSize)
    }

    /**
     * 计算总量
     */
    override fun count(exeParameter: ExeParameter): Long? {
        return uqs.restQueryCount(exeParameter.callName,exeParameter.params).count
    }

    /**
     * 分页
     */
    override fun <T> paginate(exeParameter: ExeParameter, classz: Class<T>): PageImpl<T> {
        return if (exeParameter.pageNum == null || exeParameter.pageSize == null) {
            PageImpl(query(exeParameter, classz))
        }else {
            PageImpl(query(exeParameter, classz), PageRequest(exeParameter.pageNum!!-1, exeParameter.pageSize!!), count(exeParameter)!!)
        }
    }

    /**
     * 转vo
     */
    override fun <T> beanTra(result: List<*>,classz: Class<T>): List<T> {
        var resultBean = ArrayList<T>()
        result.forEach {
            var itMap = HashMap<String,Any?>()
            if(it !is Map<*,*>) {
                resultBean.add(JtDviewBeanUtils.map2Bean1(JtDviewBeanUtils.objectToMapB(it),classz,null)!!)
            }else {
                resultBean.add(JtDviewBeanUtils.map2Bean1(it as Map<String,Any?>?,classz,null)!!)
            }
        }
        return resultBean
    }

    override fun script(exeParameter: ExeParameter): List<List<Map<String, Any>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voidR(exeParameter: ExeParameter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun paramPageFirst(exeParameter: ExeParameter): Int {
        return JtPageUtils.getFirst(exeParameter.pageNum,exeParameter.pageSize)
    }


    /**
     * ----------------------增删改查 ----------------------------
     */

    @Transactional(rollbackFor = [Exception::class])
    fun save(o: Any) {
        dataset.save(o, o.javaClass.name)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun save(map: Map<String, Any>, entityName: String): Serializable {
        return crudService.save(map, entityName)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun update(o: Any) {
        dataset.update(o, o.javaClass.name)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun update(id: Serializable, map: Map<String, Any>, entityName: String) {
        crudService.update(id, map, entityName)
    }


    @Transactional(rollbackFor = [Exception::class])
    fun <R> delete(id: Serializable, class_z: Class<R>) {
        dataset.remove(id, class_z.name)
    }

    @Transactional(readOnly = true, rollbackFor = [Exception::class])
    operator fun <R> get(id: Serializable, class_z: Class<R>): R {
        return dataset.get(id, class_z.name, class_z)
    }

    @Transactional(readOnly = true, rollbackFor = [Exception::class])
    fun <R> getMap(id: Serializable, class_z: Class<R>, vararg fetches: String): Map<String, Any> {
        return crudService.get(id, class_z.name, Arrays.asList(*fetches))
    }

    /**
     * 查询某个类所有数据，加上关联关系
     *
     * @param class_z
     * @param fetches
     * @return
     */
    @Transactional(readOnly = true, rollbackFor = [Exception::class])
    fun <R> list(class_z: Class<R>, vararg fetches: String): List<R> {
        try {
            val options = GetOptions()
            if (fetches.isNotEmpty()) {
                options.expands = Arrays.asList(*fetches)
            }
            return crudService.list(class_z.name, options, class_z)
        } catch (e: Exception) {
            throw QueryError(String.format("{0}: 列举{1}", ExceptionMsg.QUERYERROR_MSG.value(), class_z.name), e)
        }

    }
}