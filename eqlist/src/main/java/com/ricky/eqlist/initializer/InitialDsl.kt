@file:Suppress("unused")

package com.ricky.eqlist.initializer

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.ricky.eqlist.adapter.ItemDefinition
import com.ricky.eqlist.entity.BaseEntity
import com.ricky.eqlist.entity.StateEntity
import com.ricky.eqlist.item.BindingItem
import com.ricky.eqlist.item.Item
import com.ricky.eqlist.item.StateBindingItem
import com.ricky.eqlist.item.StateItem

/**
 *
 * @author Ricky Hal
 * @date 2021/11/18
 */

open class InitialDsl(recyclerView: RecyclerView) : BaseInitialDsl(recyclerView)

inline fun <reified Entity : BaseEntity> InitialDsl.item(@LayoutRes layoutId: Int, dsl: Item<Entity>.() -> Unit = {}) {
    val itemDsl = Item<Entity>().apply(dsl)
    itemDefinitions.add(
        ItemDefinition(
            entityName = Entity::class.java.name,
            layoutId = layoutId,
            viewType = itemDefinitions.size + 1,
            type = ItemDefinition.TYPE_ITEM,
            dsl = itemDsl
        )
    )
}

inline fun <reified Entity : BaseEntity, Binding> InitialDsl.bindingItem(
    @LayoutRes layoutId: Int,
    dsl: BindingItem<Entity, Binding>.() -> Unit = {}
) {
    val bindingItemDsl = BindingItem<Entity, Binding>().apply(dsl)
    itemDefinitions.add(
        ItemDefinition(
            entityName = Entity::class.java.name,
            layoutId = layoutId,
            viewType = itemDefinitions.size + 1,
            type = ItemDefinition.TYPE_BINDING_ITEM,
            dsl = bindingItemDsl
        )
    )
}

inline fun InitialDsl.stateItem(@LayoutRes layoutId: Int, dsl: StateItem.() -> Unit = {}) {
    val stateItemDsl = StateItem().apply(dsl)
    itemDefinitions.add(
        ItemDefinition(
            entityName = StateEntity::class.java.name,
            layoutId = layoutId,
            viewType = itemDefinitions.size + 1,
            type = ItemDefinition.TYPE_STATE_ITEM,
            dsl = stateItemDsl
        )
    )
}

inline fun <Binding> InitialDsl.stateBindingItem(@LayoutRes layoutId: Int, dsl: StateBindingItem<Binding>.() -> Unit = {}) {
    val stateBindingItemDsl = StateBindingItem<Binding>().apply(dsl)
    itemDefinitions.add(
        ItemDefinition(
            entityName = StateEntity::class.java.name,
            layoutId = layoutId,
            viewType = itemDefinitions.size + 1,
            type = ItemDefinition.TYPE_STATE_BINDING_ITEM,
            dsl = stateBindingItemDsl
        )
    )
}
