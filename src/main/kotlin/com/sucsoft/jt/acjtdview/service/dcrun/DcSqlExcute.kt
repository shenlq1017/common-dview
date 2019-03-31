package com.sucsoft.jt.acjtdview.service.dcrun

import com.cgs.dc.PojoDataset
import com.cgs.dc.starter.services.UnsafeCrudService
import com.cgs.dc.starter.services.UnsafeQueryService
import com.cgs.sscf.commons.domain.page.PageImpl
import com.sucsoft.jt.acjtdview.service.builder.ExeParameter
import com.sucsoft.jt.acjtdview.service.builder.SqlExecuteHandler
import com.sucsoft.jt.acjtdview.util.JtDviewBeanUtils
import com.sucsoft.jt.acjtdview.util.JtPageUtils
import org.springframework.stereotype.Service


/**
 * @author shenlq
 * @description dc的查询实现
 * @date 2019-03-30 11:55
 */
@Service
class DcSqlExcute : SqlExecuteHandler {

    lateinit var uqs: UnsafeQueryService

    lateinit var pojoDataset: PojoDataset

    lateinit var ucs: UnsafeCrudService

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
        return PageImpl(query(exeParameter, classz),null,count(exeParameter)!!)
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


}