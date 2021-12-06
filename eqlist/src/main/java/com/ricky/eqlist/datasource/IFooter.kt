package com.ricky.eqlist.datasource

import com.ricky.eqlist.entity.BaseEntity

/**
 *
 * @author Ricky Hal
 * @date 2021/11/30
 */
interface IFooter {
    fun isFooter(index: Int): Boolean
    fun getFooter(index: Int): BaseEntity
    fun getFooters(): List<BaseEntity>
    fun setFooter(list: List<BaseEntity>)
    fun addFooter(footer: BaseEntity, index: Int = -1)
    fun addFooters(list: List<BaseEntity>)
    fun invalidateFooter(footer: BaseEntity)
    fun invalidateFooters()
    fun removeFooterAt(index: Int)
    fun clearFooters()
}