@file:Suppress("unused")

package com.ricky.eqlist.initializer

import androidx.annotation.LayoutRes
import com.ricky.eqlist.adapter.ItemDefinition
import com.ricky.eqlist.datasource.LoadableDataSource
import com.ricky.eqlist.entity.LoadEntity
import com.ricky.eqlist.item.LoadBindingItem
import com.ricky.eqlist.item.LoadItem

/**
 *
 * @author Ricky Hal
 * @date 2021/11/18
 */

class LoadableInitialDsl(val dataSource: LoadableDataSource<*>) : InitialDsl()

inline fun LoadableInitialDsl.loadItem(@LayoutRes layoutId: Int, dsl: LoadItem.() -> Unit = {}) {
    val loadItemDsl = LoadItem(dataSource).apply(dsl)
    itemDefinitions.add(
        ItemDefinition(
            entityName = LoadEntity::class.java.name,
            layoutId = layoutId,
            viewType = itemDefinitions.size + 1,
            type = ItemDefinition.TYPE_LOAD_MORE_ITEM,
            dsl = loadItemDsl
        )
    )
}

inline fun <Binding> LoadableInitialDsl.loadBindingItem(@LayoutRes layoutId: Int, dsl: LoadBindingItem<Binding>.() -> Unit = {}) {
    val loadBindingItemDsl = LoadBindingItem<Binding>(dataSource).apply(dsl)
    itemDefinitions.add(
        ItemDefinition(
            entityName = LoadEntity::class.java.name,
            layoutId = layoutId,
            viewType = itemDefinitions.size + 1,
            type = ItemDefinition.TYPE_LOAD_MORE_BINDING_ITEM,
            dsl = loadBindingItemDsl
        )
    )
}
