package com.ricky.eqlist.viewholder

import android.view.View
import com.ricky.eqlist.item.Item

/**
 *
 * @author Ricky Hal
 * @date 2021/11/19
 */
class ItemViewHolder(
    view: View,
    private val item: Item<*>,
) : BaseViewHolder(view, item) {
    init {
        item.invokeOnCreate(view)
    }

    override fun onViewAttached() {
        item.invokeOnItemDetach(itemView)
    }

    override fun onViewDetached() {
        item.invokeOnItemAttach(itemView)
    }

    override fun onBind(position: Int, data: Any) {
        item.invokeOnBind(position, data, itemView)
    }

    override fun onBindPayload(position: Int, data: Any, payloads: MutableList<Any>) {
        super.onBindPayload(position, data, payloads)
        if (payloads.isNotEmpty()) {
            item.invokeOnBindPayload(position, data, itemView, payloads)
        } else {
            onBind(position, data)
        }
    }

    override fun onRecycled() {
        item.invokeOnRecycle(itemView)
    }
}