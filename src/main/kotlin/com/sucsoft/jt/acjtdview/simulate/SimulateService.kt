package com.sucsoft.jt.acjtdview.simulate

import com.sucsoft.jt.acjtdview.util.FileQuery
import com.sucsoft.jt.acjtdview.util.JtDviewBeanUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.yaml.snakeyaml.Yaml

import java.io.InputStream
import java.util.*

/**
 * description 模拟数据处理流程
 *
 * @author shenlq
 * @date 2019/01/03 16:46
 */
@Service
class SimulateService {

    @Value("\${jt.response.simulate.ymlpaths:/simulate/**}")
    private val ymlpaths: Array<String>? = null
    /**
     * 是否开启模拟数据
     */
    @Value("\${jt.response.simulate.enable:false}")
    private val simulateEnable: Boolean = false
    /**
     * 是否仅项目启动时读取一次
     */
    @Value("\${jt.response.simulate.cache:false}")
    private val simulateCache: Boolean = false

    /**
     * 模拟数据对象
     */
    private val simulateDataMap = HashMap<String, SimulateData>()

    fun getSimulateDataMap(): Map<String, SimulateData> {
        return simulateDataMap
    }

    /**
     * 判断是否需要替换为模拟数据
     * @param url 接口url
     * @return
     */
    fun isInSimulate(url: String): Boolean {
        var flag = simulateEnable
        if (flag) {
            if (!simulateCache) {
                reloadSimulateMap()
            }
            flag = simulateDataMap.containsKey(url)
        }
        return flag
    }

    /**
     * 获取模拟数据json字符串
     * @param url
     * @return
     */
    fun getSimulateByUrl(url: String): String? {
        var result: String? = "{}"
        val simulateData = simulateDataMap[url]
        if (simulateData != null) {
            result = simulateData.context
        }
        return result
    }

    /**
     * 重新载入，并放到内存
     */
    fun reloadSimulateMap() {
        val simulateDataList = loadSimulateData()
        for (simulateData in simulateDataList) {
            simulateDataMap[simulateData.url] = simulateData
        }
    }

    /**
     * 获取所有的模拟数据yml，并转成对象数组
     * @return
     */
    fun loadSimulateData(): List<SimulateData> {
        val ins = HashSet<InputStream>()
        val fileQuery = FileQuery()
        for (ymlpath in ymlpaths!!) {
            val inputStreams = fileQuery.getNamesByDir(ymlpath, "yml", "yaml")
            if (!inputStreams.isEmpty()) {
                ins.addAll(inputStreams)
            }
        }
        return getSimulateByYml(ins)
    }

    /**
     * 将流转为模拟对象
     * @param ins
     * @return
     */
    fun getSimulateByYml(ins: Set<InputStream>): List<SimulateData> {
        val yaml = Yaml()
        val datas = ArrayList<SimulateData>()
        for (`in` in ins) {
            val obj = yaml.load<Any>(`in`)
            if (obj != null) {
                datas.addAll(JtDviewBeanUtils.listMapToBeanSimple(obj as List<Map<*, *>>, SimulateData::class.java))
            }
        }
        return datas
    }

}
