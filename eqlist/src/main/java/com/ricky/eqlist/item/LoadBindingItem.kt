@file:Suppress("unused")

package com.ricky.eqlist.item

import com.ricky.eqlist.datasource.LoadableDataSource
import com.ricky.eqlist.entity.LoadEntity

/**
 * @author Ricky Hal
 * @date 2021/11/18
 */
class LoadBindingItem<Binding>(private val dataSource: LoadableDataSource<*>) : BindingItem<LoadEntity, Binding>() {
    override var isFullSpan: Boolean = true
    override fun withFullSpan(isFullSpan: Boolean) {}
    fun withDelay(delay: Long) {
        dataSource.setLoaderDelay(delay)
    }
}