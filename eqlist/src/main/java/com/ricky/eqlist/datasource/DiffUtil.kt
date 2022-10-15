package com.ricky.eqlist.datasource

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ricky.eqlist.entity.BaseEntity

/**
 *
 * @author Ricky Hal
 * @date 2021/11/30
 */
class DiffUtil(
    private val adapter: RecyclerView.Adapter<*>
) {
    private var oldList = listOf<BaseEntity>()

    @SuppressLint("NotifyDataSetChanged")
    fun submit(newList: List<BaseEntity>, isInitial: Boolean = false, payload: Any? = null) {
        // 没有任何改变，直接返回
        if (newList == oldList) return
        when {
            isInitial -> {
                adapter.notifyDataSetChanged()
                oldList = newList
            }
            else -> {
                val result = DiffUtil.calculateDiff(DiffCallback(oldList, newList, payload))
                oldList = ArrayList(newList)
                result.dispatchUpdatesTo(adapter)
            }
        }

    }
}