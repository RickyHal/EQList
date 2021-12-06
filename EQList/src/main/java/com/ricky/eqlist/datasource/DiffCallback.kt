package com.ricky.eqlist.datasource

import androidx.recyclerview.widget.DiffUtil
import com.ricky.eqlist.entity.BaseEntity

/**
 *
 * @author Ricky Hal
 * @date 2021/11/19
 */
class DiffCallback(
    private val oldItems: List<BaseEntity>,
    private val newItems: List<BaseEntity>,
    private val payload: Any? = null
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItems.size
    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].areItemsTheSame(newItems[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].areContentsTheSame(newItems[newItemPosition])
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return payload
    }
}