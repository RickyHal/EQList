package com.ricky.eqlist.viewholder

import android.view.View
import com.ricky.eqlist.item.BindingItem

/**
 *
 * @author Ricky Hal
 * @date 2021/11/19
 */
class BindingItemViewHolder(
    view: View,
    private val item: BindingItem<*, *>,
) : BaseViewHolder(view, item) {
    private var binding: Any = item.invokeOnCreate(itemView)!!

    override fun onViewAttached() {
        item.invokeOnItemAttach(binding)
    }

    override fun onViewDetached() {
        item.invokeOnItemDetach(binding)
    }

    override fun onBind(position: Int, data: Any) {
        item.invokeOnBind(position, data, binding)
    }

    override fun onBindPayload(position: Int, data: Any, payloads: MutableList<Any>) {
        super.onBindPayload(position, data, payloads)
        if (payloads.isNotEmpty()) {
            item.invokeOnBindPayload(position, data, payloads, binding)
        } else {
            onBind(position, data)
        }
    }

    override fun onRecycled() {
        item.invokeOnRecycle(binding)
    }
}