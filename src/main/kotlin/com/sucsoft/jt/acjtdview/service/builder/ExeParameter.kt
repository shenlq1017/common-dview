package com.sucsoft.jt.acjtdview.service.builder

import java.util.LinkedHashMap

class ExeParameter(val type: Type, val callName: String, val params: LinkedHashMap<String, Any?>, val queryTimeOut: Int?, val pageNum: Int?, val pageSize: Int?, var returnClass: Class<*>, val projectName: String) {

    enum class Type private constructor() {
        Query,
        Count,
        Paginate,
        Script,
        Void,
        SingleNum
    }
}
