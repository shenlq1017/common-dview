package com.sucsoft.jt.acjtdview.service.builder

import java.util.LinkedHashMap
import org.apache.commons.beanutils.BeanMap
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

interface SqlExecuteHandler {
    fun execute(exeParameter: ExeParameter): Any

    fun <T> query(exeParameter: ExeParameter, classz: Class<T>): List<T>

    fun count(exeParameter: ExeParameter): Long?

    fun <T> paginate(exeParameter: ExeParameter, classz: Class<T>): PageImpl<T>

    fun script(exeParameter: ExeParameter): List<List<Map<String, Any>>>

    fun voidR(exeParameter: ExeParameter)

    fun <T> beanTra(reuslt:List<*>,classz: Class<T>): List<T>

    fun builder(callName: String): ExeParameterBuilder {
        return ExeParameterBuilder(this).callName(callName)
    }

    class ExeParameterBuilder(private val sqlExecuteHandler: SqlExecuteHandler) {
        private var type: ExeParameter.Type? = null
        private var callName: String? = null
        private val params = LinkedHashMap<String,Any?>()
        private var queryTimeOut: Int? = null
        private var pageNum: Int? = null
        private var pageSize: Int? = null
        private var returnClass: Class<*>? = null
        private var projectName: String = ""
//        private var jsonSerialization: JsonSerialization? = null

        fun type(type: ExeParameter.Type): ExeParameterBuilder {
            this.type = type
            return this
        }

        fun callName(callName: String): ExeParameterBuilder {
            this.callName = callName
            return this
        }

        fun param(k: String, v: Any): ExeParameterBuilder {
            this.params.put(k, v)
            return this
        }

        fun paramNotNull(k: String, v: Any?): ExeParameterBuilder {
            if (v != null ) {
                this.params.put(k, v)
            }
            return this
        }

        fun paramStringNotBlank(k: String, v: String): ExeParameterBuilder {
            if (v.isEmpty()) {
                this.params.put(k, v)
            }

            return this
        }

        fun paramPOJO(obj: Any): ExeParameterBuilder {
            BeanMap(obj).forEach { k, v ->
                if (k != null && v != null) {
                    this.params.put(k.toString(), v)
                }

            }
            return this
        }

        fun paramMap(obj: Map<String,Any?>): ExeParameterBuilder {
            obj.forEach { k, v ->
                if (v != null) {
                    this.params.put(k, v)
                }
            }
            return this
        }

        fun paramLike(k: String, v: String?, likeType: LikeType): ExeParameterBuilder {
            this.params.put(k, this.getLikeRenderString(v, likeType))
            return this
        }

        fun getLikeRenderString(v: String?, likeType: LikeType): String? {
            return when (likeType) {
                LikeType.Left -> if (v == null) null else "%$v"
                LikeType.Right -> if (v == null) null else "$v%"
                LikeType.Around -> if (v == null) null else "%$v%"
                else -> v
            }
        }

        fun paramLike(k: String, v: String?): ExeParameterBuilder {
            if(v != null ) {
                this.params.put(k, "%$v%")
            }
            return this
        }

        fun queryTimeOut(queryTimeOut: Int?): ExeParameterBuilder {
            this.queryTimeOut = queryTimeOut
            return this
        }

        fun pageNum(pageNum: Int?): ExeParameterBuilder {
            this.pageNum = pageNum
            return this
        }

        fun pageSize(pageSize: Int?): ExeParameterBuilder {
            this.pageSize = pageSize
            return this
        }

        fun page(pageNum: Int?, pageSize: Int?): ExeParameterBuilder {
            this.pageNum = pageNum
            this.pageSize = pageSize
            return this
        }

        private fun returnClass(returnClass: Class<*>): ExeParameterBuilder {
            this.returnClass = returnClass
            return this
        }

        fun projectName(projectName: String): ExeParameterBuilder {
            this.projectName = projectName
            return this
        }

//        fun jsonSerialization(jsonSerialization: JsonSerialization): ExeParameterBuilder {
//            this.jsonSerialization = jsonSerialization
//            return this
//        }

        internal fun build(): ExeParameter {
            return ExeParameter(this.type!!, this.callName!!, this.params, this.queryTimeOut, this.pageNum, this.pageSize, this.returnClass!!, this.projectName)
        }

        fun queryExecute(): List<Map<*, *>> {
            this.type(ExeParameter.Type.Query)
            this.returnClass = Map::class.java
            val exeParameter = this.build()
            return this.sqlExecuteHandler.query(exeParameter, Map::class.java)
        }

        fun <T> queryExecute(returnClass: Class<T>): List<T> {
            this.returnClass(returnClass)
            this.type(ExeParameter.Type.Query)
            val exeParameter = this.build()
            return this.sqlExecuteHandler.query(exeParameter, returnClass)
        }

        /**
         * 查询vo对象列表
         */
        fun <T> queryExecuteObj(backClass: Class<T>): List<T> {
            this.type(ExeParameter.Type.Query)
            this.returnClass = Map::class.java
            val exeParameter = this.build()
            val result= this.sqlExecuteHandler.query(exeParameter, exeParameter.returnClass)
            return this.sqlExecuteHandler.beanTra(result,backClass)
        }


        @Deprecated("")
        fun countExecute(): Long? {
            this.type(ExeParameter.Type.Count)
            val exeParameter = this.build()
            return this.sqlExecuteHandler.count(exeParameter)
        }

        /**
         * 统计总数
         */
        fun countOperation(): Long? {
            this.type(ExeParameter.Type.Count)
            val exeParameter = this.build()
            return this.sqlExecuteHandler.count(exeParameter)
        }

        fun singleNumExecute(): Long? {
            this.type(ExeParameter.Type.SingleNum)
            val exeParameter = this.build()
            return this.sqlExecuteHandler.count(exeParameter)
        }


        @Deprecated("")
        fun paginateExecute(): PageImpl<Map<*, *>> {
            this.type(ExeParameter.Type.Paginate)
            val exeParameter = this.build()
            return this.sqlExecuteHandler.paginate(exeParameter, Map::class.java)
        }

        /**
         * map分页
         */
        fun paginateOperation(): PageImpl<Map<*, *>> {
            this.type(ExeParameter.Type.Paginate)
            val exeParameter = this.build()
            return this.sqlExecuteHandler.paginate(exeParameter, Map::class.java)
        }

        /**
         * 对象分页
         */
        fun <T> paginateExecute(returnClass: Class<T>): PageImpl<T> {
            this.returnClass(returnClass)
            this.type(ExeParameter.Type.Paginate)
            val exeParameter = this.build()
            return this.sqlExecuteHandler.paginate(exeParameter, returnClass)
        }

        /**
         * 转vo分页
         */
        fun <T> paginateExecuteObj(backClass: Class<T>): PageImpl<T> {
            return if (this.pageNum == null && this.pageSize == null) {
                PageImpl(queryExecuteObj(backClass))
            }else {
                PageImpl(queryExecuteObj(backClass), PageRequest(this.pageNum!!-1,this.pageSize!!),countOperation()!!)
            }

        }

        fun scriptExecute(): List<List<Map<String, Any>>> {
            this.type(ExeParameter.Type.Script)
            val exeParameter = this.build()
            return this.sqlExecuteHandler.script(exeParameter)
        }

        fun voidExecute() {
            this.type(ExeParameter.Type.Void)
            val exeParameter = this.build()
            this.sqlExecuteHandler.voidR(exeParameter)
        }


        fun execute(): Any {
            val exeParameter = this.build()
            return this.sqlExecuteHandler.execute(exeParameter)
        }

        enum class LikeType private constructor() {
            Left,
            Right,
            Around
        }
    }
}
