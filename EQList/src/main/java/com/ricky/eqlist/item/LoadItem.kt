package com.ricky.eqlist.item

import com.ricky.eqlist.datasource.LoadableDataSource
import com.ricky.eqlist.entity.LoadEntity

/**
 * @author Ricky Hal
 * @date 2021/11/18
 */
class LoadItem(private val dataSource: LoadableDataSource<*>) : Item<LoadEntity>() {
    override var isFullSpan: Boolean = true
    override fun withFullSpan(isFullSpan: Boolean) {}

    fun withDelay(delay: Long) {
        dataSource.setLoaderDelay(delay)
    }
}