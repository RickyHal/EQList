package com.ricky.eqlist.datasource

import com.ricky.eqlist.entity.BaseEntity

/**
 *
 * @author Ricky Hal
 * @date 2021/11/30
 */
interface IHeader {
    fun isHeader(index: Int): Boolean
    fun getHeader(index: Int): BaseEntity
    fun getHeaders(): List<BaseEntity>
    fun setHeader(list: List<BaseEntity>)
    fun addHeader(header: BaseEntity, index: Int = -1)
    fun addHeaders(list: List<BaseEntity>)
    fun invalidateHeader(header: BaseEntity)
    fun invalidateHeaders()
    fun removeHeaderAt(index: Int)
    fun clearHeaders()
}