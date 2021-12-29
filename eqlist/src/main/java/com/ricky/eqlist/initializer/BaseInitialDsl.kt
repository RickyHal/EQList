package com.ricky.eqlist.initializer

import androidx.recyclerview.widget.RecyclerView
import com.ricky.eqlist.LogUtil
import com.ricky.eqlist.adapter.ItemDefinition

/**
 *
 * @author Ricky Hal
 * @date 2021/11/18
 */
abstract class BaseInitialDsl(val recyclerView: RecyclerView) {
    val itemDefinitions = mutableListOf<ItemDefinition>()
    var onAttachBlock: () -> Unit = {}
    var onDetachBlock: () -> Unit = {}
    internal fun getViewTypeForClass(className: String): Int {
        return itemDefinitions.firstOrNull { it.entityName == className }?.viewType ?: ItemDefinition.TYPE_EMPTY
    }

    internal fun getItemDefinition(viewType: Int): ItemDefinition {
        return itemDefinitions.firstOrNull { it.viewType == viewType } ?: ItemDefinition.default()
    }

    fun withDebug(isDebug: Boolean) {
        LogUtil.isDebug = isDebug
    }

    fun onAttach(block: () -> Unit) {
        onAttachBlock = block
    }

    fun onDetach(block: () -> Unit) {
        onDetachBlock = block
    }
}