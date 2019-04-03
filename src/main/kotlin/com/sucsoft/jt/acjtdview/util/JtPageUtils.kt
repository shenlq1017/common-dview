package com.sucsoft.jt.acjtdview.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JtPageUtils {

    @Value("\${ac.dc.default-pageNo:1}")
    fun setDefault_pageNo(default_pageNo: Int?) {
        JtPageUtils.default_pageNo = default_pageNo
    }

    @Value("\${ac.dc.default-pageSize:10}")
    fun setDefault_pageSize(default_pageSize: Int?) {
        JtPageUtils.default_pageSize = default_pageSize
    }

    companion object {
        private var default_pageNo: Int? = 0
        private var default_pageSize: Int? = 0

        fun getDefault_pageNo(): Int? {
            return default_pageNo
        }

        fun getDefault_pageSize(): Int? {
            return default_pageSize
        }

        /**
         * 得到第一个值,(dc是从0开始，总觉得很chun)
         * @param pageNo
         * @param pageSize
         * @return
         */
        fun getFirst(pageNo: Int?, pageSize: Int?): Int {
            var pageNo = pageNo
            var pageSize = pageSize
            pageNo = pageNo ?: default_pageNo
            pageSize = pageSize ?: default_pageSize
            var first: Int = pageSize!! * (pageNo!! - 1)
            first = if (first < 0) 0 else first
            return first
        }

        fun getPageNo(pageNo: Int?): Int? {
            var pageNo = pageNo
            pageNo = pageNo ?: default_pageNo
            return pageNo
        }

        fun getPageSize(pageSize: Int?): Int? {
            var pageSize = pageSize
            pageSize = pageSize ?: default_pageSize
            return pageSize
        }
    }
}
