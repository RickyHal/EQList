package com.ricky.eqlist.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ricky.eqlist.entity.LoadEntity
import com.ricky.eqlist.entity.LoadState
import com.ricky.eqlist.item.BaseItem

/**
 *
 * @author Ricky Hal
 * @date 2021/11/25
 */
abstract class BaseViewHolder(
    view: View,
    private val item: BaseItem
) : RecyclerView.ViewHolder(view) {
    abstract fun onViewAttached()
    abstract fun onViewDetached()
    abstract fun onBind(position: Int, data: Any)
    open fun onBindPayload(position: Int, data: Any, payloads: MutableList<Any>) {
        if (data is LoadEntity) {
            itemView.visibility = if (data.state != LoadState.STATE_HIDE) View.VISIBLE else View.GONE
        }
    }

    abstract fun onRecycled()
}