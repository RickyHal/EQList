package com.ricky.eqlist.adapter

import com.ricky.eqlist.R
import com.ricky.eqlist.entity.EmptyEntity
import com.ricky.eqlist.item.BaseItem
import com.ricky.eqlist.item.Item

/**
 *
 * @author Ricky Hal
 * @date 2021/11/19
 */
data class ItemDefinition(
    val entityName: String,
    val layoutId: Int,
    val viewType: Int,
    val type: Int,
    val dsl: BaseItem
) {
    companion object {
        const val TYPE_EMPTY = -1
        const val TYPE_ITEM = 0
        const val TYPE_BINDING_ITEM = 1
        const val TYPE_STATE_ITEM = 2
        const val TYPE_STATE_BINDING_ITEM = 3
        const val TYPE_LOAD_MORE_ITEM = 4
        const val TYPE_LOAD_MORE_BINDING_ITEM = 5

        fun default() = ItemDefinition(
            EmptyEntity::class.java.name,
            R.layout.item_empty,
            TYPE_EMPTY,
            TYPE_EMPTY,
            Item<EmptyEntity>()
        )
    }
}
