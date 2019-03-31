package com.sucsoft.jt.acjtdview.enums

/**
 * description 各类基本异常信息
 *
 * @author shenlq
 * @date 2018/12/29 17:09
 */
enum class ExceptionMsg {
    QUERYERROR_MSG("查询异常"), DATADEALERROR_MSG("返回数据处理异常"), CHECKERROR_MSG("数据检查异常");

    internal var value: String =""

    private constructor() {}

    private constructor(value: String) {
        this.value = value
    }

    fun value(): String {
        return this.value
    }

}
