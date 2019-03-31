package com.sucsoft.jt.acjtdview.util

import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver
import org.springframework.stereotype.Component

import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList
import java.util.HashSet

/**
 * description 获取文件的一些信息
 *
 * @author shenlq
 * @date 2018/10/09 17:05
 */
class FileQuery {

    /**
     * 拿到所有以pattern结尾的文件
     * @param dir
     * @param pattern
     * @return
     */
    fun getNamesByDir(dir: String, vararg patterns: String): List<InputStream> {
        val `in` = HashSet<InputStream>()
        val resolver = PathMatchingResourcePatternResolver()
        try {
            if (dir.length == 0) {
                return ArrayList()
            }
            val resources = resolver.getResources("classpath*:$dir")
            for (r in resources) {
                if (patterns != null && patterns.size > 0) {
                    for (pattern in patterns) {
                        if (r.filename!!.endsWith(pattern)) {
                            `in`.add(r.inputStream)
                            break
                        }
                    }
                } else {
                    `in`.add(r.inputStream)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return ArrayList(`in`)
    }

    /**
     * 拿到所有以pattern结尾的文件
     * @param dir
     * @param pattern
     * @return
     */
    fun getFileNamesByDir(dir: String, vararg patterns: String): List<String> {
        val `in` = HashSet<String>()
        val resolver = PathMatchingResourcePatternResolver()
        try {
            if (dir.length == 0) {
                return ArrayList()
            }
            val resources = resolver.getResources("classpath*:$dir")
            for (r in resources) {
                if (patterns != null && patterns.size > 0) {
                    for (pattern in patterns) {
                        if (r.filename!!.endsWith(pattern)) {
                            `in`.add(r.filename!!)
                            break
                        }
                    }
                } else {
                    `in`.add(r.filename!!)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return ArrayList(`in`)
    }

    companion object {
        private val ALL_PATTERN = "*"
        /**
         * 获得文件路径所在目录
         * @param fileName 文件路径
         * @return 目录
         */
        fun getPath(fileName: String): String {
            val file = File(fileName)
            return getPath(file)
        }

        /**
         * 获得文件所在目录
         * @param file 文件
         * @return 目录
         */
        fun getPath(file: File): String {
            return file.path
        }

        /**
         * 获得文件路径下的文件名
         * @param fileName 文件路径
         * @return 文件名
         */
        fun getName(fileName: String): String {
            val file = File(fileName)
            return getName(file)
        }

        /**
         * 获得文件路径的文件名
         * @param file 文件
         * @return 文件名
         */
        fun getName(file: File): String {
            return file.name
        }
    }


}
