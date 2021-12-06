package com.ricky.eqlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ricky.eqlist.datasource.DataSource
import com.ricky.eqlist.initializer.InitialDsl
import com.ricky.eqlist.item.BindingItem
import com.ricky.eqlist.item.Item
import com.ricky.eqlist.viewholder.BaseViewHolder
import com.ricky.eqlist.viewholder.BindingItemViewHolder
import com.ricky.eqlist.viewholder.ItemViewHolder

/**
 *
 * @author Ricky Hal
 * @date 2021/11/19
 */
open class EQListAdapter(
    open val
    dataSource: DataSource,
    protected val initialDsl: InitialDsl,
    protected val layoutManager: RecyclerView.LayoutManager
) : RecyclerView.Adapter<BaseViewHolder>() {
    private var holder: BaseViewHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val item = initialDsl.getItemDefinition(viewType)
        val view = LayoutInflater.from(parent.context).inflate(item.layoutId, parent, false)
        return when (item.dsl) {
            is Item<*> -> ItemViewHolder(view, item.dsl)
            is BindingItem<*, *> -> BindingItemViewHolder(view, item.dsl)
            else -> throw IllegalStateException("Create view failed")
        }.also { holder = it }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        initialDsl.onAttachBlock.invoke()
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val item = initialDsl.itemDefinitions.find {
                        it.entityName == dataSource[position]::class.java.name
                    } ?: return 1
                    return if (item.dsl.isFullSpan
                        || dataSource.isHeader(position)
                        || dataSource.isFooter(position)
                    ) layoutManager.spanCount else 1
                }
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        initialDsl.onDetachBlock.invoke()
        holder = null
        dataSource.unBindAdapter()
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        holder.onViewAttached()
        if (layoutManager is StaggeredGridLayoutManager) {
            val headers = dataSource.getHeaders()
            val footers = dataSource.getHeaders()
            val itemDefinition = initialDsl.getItemDefinition(holder.itemViewType)
            val item = itemDefinition.dsl
            val lp = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            lp.isFullSpan = item.isFullSpan
                || headers.any { it::class.java.name == itemDefinition.entityName }
                || footers.any { it::class.java.name == itemDefinition.entityName }
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        holder.onViewDetached()
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position, dataSource[position])
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, payloads: MutableList<Any>) {
        holder.onBindPayload(position, dataSource[position], payloads)
    }

    override fun onViewRecycled(holder: BaseViewHolder) {
        holder.onRecycled()
    }

    override fun getItemViewType(position: Int): Int {
        val data = dataSource[position]
        return data.getItemType()
    }

    override fun getItemCount(): Int {
        return dataSource.size()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun Any.getItemType(): Int {
        val itemClassName = this::class.java.name
        return initialDsl.getViewTypeForClass(itemClassName)
    }
}