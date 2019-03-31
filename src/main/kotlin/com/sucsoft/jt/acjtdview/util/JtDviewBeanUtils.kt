package com.sucsoft.jt.acjtdview.util


import com.google.common.collect.Maps
import org.springframework.util.StringUtils
import java.beans.Introspector

import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * init
 * @author sunjm
 * @version 1.0
 * Adding cache and Changing assignment value
 * @author Libin
 * @version 2.0
 */
class JtDviewBeanUtils {

    companion object {

        private val funcCache = Maps.newHashMapWithExpectedSize<Class<*>, Map<String, Method>>(32)

        /**
         * 初始化操作，懒加载模式，第一次调用时加载
         * @param clazz 类字节码对象
         */
        private fun initFuncCache(clazz: Class<*>) {
            synchronized(clazz) {
                val methodMap = Maps.newHashMapWithExpectedSize<String, Method>(64)
                for (declaredMethod in clazz.declaredMethods) {
                    val name = declaredMethod.name
                    if (name.startsWith("set")) {
                        val s = name.substring(3).toUpperCase()
                        methodMap[s] = declaredMethod
                    }
                }
                funcCache.put(clazz, methodMap)
            }
        }

        /**
         * 内部设值方法
         * @param instance 待赋值的对象
         * @param value 值
         * @param method 方法
         * @throws Exception 调用失败
         */
        @Throws(Exception::class)
        private operator fun setValue(instance: Any?, value: Any?, method: Method) {
            val parameterType = method.parameterTypes[0]
            if (parameterType == Date::class.java) {
                method.invoke(instance, getDate(value))
            } else if (parameterType == Double::class.java || parameterType == Double::class.javaPrimitiveType) {
                method.invoke(instance, getDouble(value))
            } else if (parameterType == Int::class.java || parameterType == Int::class.javaPrimitiveType) {
                method.invoke(instance, getInteger(value))
            } else if (parameterType == Long::class.java || parameterType == Long::class.javaPrimitiveType) {
                method.invoke(instance, gatLong(value))
            } else if (parameterType == Boolean::class.java || parameterType == Boolean::class.javaPrimitiveType) {
                method.invoke(instance, getBoolean(value))
            } else if (parameterType == Char::class.java || parameterType == Char::class.javaPrimitiveType) {
                method.invoke(instance, if (value == null) null else value.toString()[0])
            } else if (parameterType == BigDecimal::class.java) {
                method.invoke(instance, getBigDecimal(value))
            } else if (parameterType == String::class.java) {
                method.invoke(instance, getString(value))
            } else {
                method.invoke(instance, value)
            }
        }

        /**
         * 赋值方法，经测试在大量反射的情况下，效率高一倍
         * 若是考虑更高效率，考虑引入reflectasm进行缓存
         * @param map 数据
         * @param clazz 转换目标类
         * @param params 额外参数
         * @param <T> 目标泛型
         * @return 数据
        </T> */
        fun <T> map2Bean1(map: Map<String, Any?>?, clazz: Class<T>, params: Map<String, Any>?): T? {
            var params = params
            if (map == null) {
                return null
            }
            //初始化
            if (!funcCache.containsKey(clazz)) {
                initFuncCache(clazz)
            }
            //如果额外说明字段是空给定初始值
            if (null == params) {
                params = Maps.newHashMapWithExpectedSize(16)
            }
            val methodMap = funcCache[clazz]
            var obj: T? = null
            try {
                obj = clazz.newInstance()

                for (key in map.keys) {
                    if (methodMap != null) {
                        val method = methodMap[key.toUpperCase()]
                        setValue(obj, if (null == params!![key]) map[key] else params[key], method!!)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return obj
        }

        /**
         * Map转换Bean
         * @param <T>
         * @param map 需要转换的map
         * @param clazz 实体对象类型
         * @param params 特殊赋值Map,用来存放一些需要指定赋值的实体对象属性 key为对象属性名 value为值 例如：对象中有name属性，传入的需要转换的Map中的name属性值为1，但我想为对象的name属性赋值2，则可以定义params为内含属性name且值为2的Map
         * @return
        </T> */
        fun <T> map2Bean(map: Map<String, Any>?, clazz: Class<T>, params: Map<String, Any>?): T? {
            if (map == null) {
                return null
            }

            //Map Key转大写
            val bmap = hashMapOf<String, Any?>()
            for (set in map.keys) {
                //在循环将大写的KEY和VALUE 放入Map
                bmap.put(set.toUpperCase(), map[set])
            }

            var obj: T? = null
            try {
                obj = clazz.newInstance()

                val fields = clazz::class.java.declaredFields
                for (field in fields) {
                    val mod = field.modifiers
                    if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                        continue
                    }
                    field.isAccessible = true

                    val o: Any?
                    if (params != null && !params.isEmpty() && params.containsKey(field.name)) {
                        //特殊赋值
                        o = params[field.name]
                    } else {
                        o = bmap.get(field.name.toUpperCase())
                    }
                    val filedTypeName = field.type.name
                    if (filedTypeName.equals(Date::class.java.name, ignoreCase = true)) {
                        field.set(obj, getDate(o))
                    } else if (filedTypeName.equals(Double::class.java.name, ignoreCase = true) || filedTypeName.equals(Double::class.javaPrimitiveType!!.name, ignoreCase = true)) {
                        field.set(obj, getDouble(o))
                    } else if (filedTypeName.equals(Int::class.java.name, ignoreCase = true) || filedTypeName.equals(Int::class.javaPrimitiveType!!.name, ignoreCase = true)) {
                        field.set(obj, getInteger(o))
                    } else if (filedTypeName.equals(Long::class.java.name, ignoreCase = true) || filedTypeName.equals(Long::class.javaPrimitiveType!!.name, ignoreCase = true)) {
                        field.set(obj, gatLong(o))
                    } else if (filedTypeName.equals(Boolean::class.java.name, ignoreCase = true) || filedTypeName.equals(Boolean::class.javaPrimitiveType!!.name, ignoreCase = true)) {
                        field.set(obj, getBoolean(o))
                    } else if (filedTypeName.equals(Char::class.java.name, ignoreCase = true) || filedTypeName.equals(Char::class.javaPrimitiveType!!.name, ignoreCase = true)) {
                        field.set(obj, if (o == null) null else o.toString()[0])
                    } else if (filedTypeName.equals(BigDecimal::class.java.name, ignoreCase = true)) {
                        field.set(obj, getBigDecimal(o))
                    } else if (filedTypeName.equals(String::class.java.name, ignoreCase = true)) {
                        field.set(obj, getString(o))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return obj
        }

        private fun getString(o: Any?): String? {
            if (o != null && o is Date) {
                //data转string
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                return sdf.format(o as Date?)
            } else {
                return o?.toString()
            }
        }

        private fun getBoolean(o: Any?): Boolean? {
            var booleanstr = ""
            if (o != null) {
                booleanstr = o.toString()
            }
            return if (StringUtils.isEmpty(booleanstr) || booleanstr.equals("null", ignoreCase = true)) {
                null
            } else {
                java.lang.Boolean.parseBoolean(booleanstr)
            }
        }

        @Throws(Exception::class)
        private fun getDate(o: Any?): Date? {
            if (o != null && o is Date) {
                return o
            }
            //string 转 Date
            val datetimestamp = o.toString()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return if (StringUtils.isEmpty(datetimestamp) || datetimestamp.equals("null", ignoreCase = true)) {
                null
            } else {
                sdf.parse(datetimestamp)
            }
        }

        private fun getDouble(o: Any?): Double? {
            var doublestr = ""
            if (o != null) {
                doublestr = o.toString()
            }
            if (StringUtils.isEmpty(doublestr) || doublestr.equals("null", ignoreCase = true)) {
                return null
            } else {
                val b = BigDecimal(java.lang.Double.toString(java.lang.Double.parseDouble(doublestr)))
                val one = BigDecimal("1")
                return b.divide(one, 2, BigDecimal.ROUND_HALF_UP).toDouble()
            }
        }

        private fun getInteger(o: Any?): Int? {
            var intergerstr = ""
            if (o != null) {
                intergerstr = o.toString()
            }
            return if (StringUtils.isEmpty(intergerstr) || intergerstr.equals("null", ignoreCase = true)) {
                null
            } else {
                Integer.parseInt(intergerstr)
            }
        }

        private fun gatLong(o: Any?): Long? {
            var intergerstr = ""
            if (o != null) {
                intergerstr = o.toString()
            }
            return if (StringUtils.isEmpty(intergerstr) || intergerstr.equals("null", ignoreCase = true)) {
                null
            } else {
                java.lang.Long.parseLong(intergerstr)
            }
        }

        private fun getBigDecimal(o: Any?): BigDecimal? {
            var intergerstr = ""
            if (o != null) {
                intergerstr = o.toString()
            }
            return if (StringUtils.isEmpty(intergerstr) || intergerstr.equals("null", ignoreCase = true)) {
                null
            } else {
                BigDecimal(intergerstr)
            }
        }

        /**
         * Map转换Bean
         * @param <T>
         * @param map 需要转换的Map
         * @param clazz 实体对象类型
         * @return
        </T> */
        fun <T> map2Bean(map: Map<String, Any>, clazz: Class<T>): T? {
            return map2Bean(map, clazz, null)
        }


        /**
         * list Map转成list 实体对象
         *
         * @param list   需要转换的 list map
         * @param clazz 实体对象类型
         * @param params 特殊赋值Map,用来存放一些需要指定赋值的实体对象属性 key为对象属性名 value为值 例如：对象中有name属性，传入的需要转换的Map中的name属性值为1，但我想为对象的name属性赋值2，则可以定义params为内含属性name且值为2的Map
         * @return
         */
        fun <T> listMapToBeanSimple(list: List<*>, clazz: Class<T>, params: Map<String, Any>?): List<T> {
            val result = ArrayList<T>()
            for (map in list as List<Map<String, Any>>) {
                try {
                    result.add(map2Bean(map, clazz, params)!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            return result
        }

        /**
         * list Map转成list 实体对象
         *
         * @param list   需要转换的 list map
         * @param clazz 实体对象类型
         * @return
         */
        fun <T> listMapToBeanSimple(list: List<*>, clazz: Class<T>): List<T> {
            return listMapToBeanSimple(list, clazz, null)
        }

        /**
         * 普通 的javabean 转map
         */
        @Throws(Exception::class)
        fun objectToMapB(obj: Any?): Map<String, Any?> {
            if (obj == null) {
                return HashMap()
            }
            val map = HashMap<String, Any?>()

            val beanInfo = Introspector.getBeanInfo(obj::class.java)
            val propertyDescriptors = beanInfo.propertyDescriptors
            for (property in propertyDescriptors) {
                val key = property.name
                if (key.compareTo("class", ignoreCase = true) == 0) {
                    continue
                }
                val getter = property.readMethod
                val value = getter?.invoke(obj)
                map[key] = value
            }

            return map
        }


    }

}
