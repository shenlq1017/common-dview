package com.sucsoft.jt.acjtdview.service.builder

interface JsonSerialization {
    fun <T> deserialize(var1: String, var2: Class<T>): T

    fun serialize(var1: Any): String

    fun <T> deserializeList(var1: List<Any>, var2: Class<T>): List<T>
}