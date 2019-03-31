package com.sucsoft.jt.acjtdview.bean

import com.cgs.dc.PojoDataset
import com.cgs.dc.client.impl.PojoDatasetImpl
import com.cgs.dc.starter.services.UnsafeCrudService
import com.cgs.dc.starter.services.UnsafeQueryService
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

import java.io.Serializable
import java.util.Arrays

/**
 * @author shenlq
 * @description
 * @date 2019-03-30 15:17
 */
open class B {
    protected var logger = Logger.getLogger(javaClass)

    /**
     * pojo 处理类
     */
    @Autowired
    private val dataset: PojoDataset? = null
    @Autowired
    private val crudService: UnsafeCrudService? = null

    /**
     * ----------------------增删改查 ----------------------------
     */

    @Transactional(rollbackFor = [Exception::class])
    fun save(o: Any) {
        dataset!!.save(o, o.javaClass.name)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun save(map: Map<String, Any>, entityName: String): Serializable {
        return crudService!!.save(map, entityName)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun update(o: Any) {
        dataset!!.update(o, o.javaClass.name)
    }

    @Transactional(rollbackFor = [Exception::class])
    open fun update(id: Serializable, map: Map<String, Any>, entityName: String) {
        crudService!!.update(id, map, entityName)
    }


    @Transactional(rollbackFor = [Exception::class])
    fun <R> delete(id: Serializable, class_z: Class<R>) {
        dataset!!.remove(id, class_z.name)
    }

    @Transactional(readOnly = true, rollbackFor = [Exception::class])
    operator fun <R> get(id: Serializable, class_z: Class<R>): R {
        return dataset!!.get(id, class_z.name, class_z)
    }

    @Transactional(readOnly = true, rollbackFor = [Exception::class])
    fun <R> getMap(id: Serializable, class_z: Class<R>, vararg fetches: String): Map<String, Any> {
        return crudService!!.get(id, class_z.name, Arrays.asList(*fetches))
    }
}
