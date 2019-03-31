package com.sucsoft.jt.acjtdview.simulate

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * description 模拟数据
 *
 * @author shenlq
 * @date 2019/01/03 16:40
 */
@ApiModel("模拟数据")
class SimulateData {

    @ApiModelProperty("需要监听的url")
    var url: String = ""

    @ApiModelProperty("返回的模拟数据")
    var context: String? = null

    @ApiModelProperty("接口描述")
    var description: String? = null

    @ApiModelProperty("是否启用")
    var isRepEnable: Boolean = false

    @ApiModelProperty("数据标签")
    var tags: List<String>? = null
}
