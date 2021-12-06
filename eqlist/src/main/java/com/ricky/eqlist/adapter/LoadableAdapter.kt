package com.ricky.eqlist.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ricky.eqlist.datasource.DataSource
import com.ricky.eqlist.datasource.LoadableDataSource
import com.ricky.eqlist.entity.LoadState
import com.ricky.eqlist.entity.StateEntity
import com.ricky.eqlist.initializer.InitialDsl

/**
 *
 * @author Ricky Hal
 * @date 2021/11/26
 */
class LoadableAdapter(
    private val preloadOffset: Int,
    override val dataSource: DataSource,
    initialDsl: InitialDsl,
    layoutManager: RecyclerView.LayoutManager
) : EQListAdapter(dataSource, initialDsl, layoutManager) {
    private val onScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (dataSource is LoadableDataSource<*>) {
                val isVertical = layoutManager is LinearLayoutManager && layoutManager.orientation == LinearLayoutManager.VERTICAL
                    || layoutManager is StaggeredGridLayoutManager && layoutManager.orientation == StaggeredGridLayoutManager.VERTICAL
                    || layoutManager is ICustomLayoutManager && layoutManager.getCustomOrientation() == RecyclerView.VERTICAL
                val scrollable = if (isVertical) {
                    recyclerView.canScrollVertically(1) || recyclerView.canScrollVertically(-1)
                } else {
                    recyclerView.canScrollHorizontally(1) || recyclerView.canScrollHorizontally(-1)
                }
                if (scrollable
                    && dataSource.isLoadable()
                    && dataSource.getCurrentState() == StateEntity.STATE_HIDE
                ) {
                    dataSource.onLoadMoreStateChanged(LoadState.STATE_SHOW)
                }
                // 滚动结束
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 滚动距离
                    val scrollOffset = if (isVertical) recyclerView.computeVerticalScrollOffset() else recyclerView.computeHorizontalScrollOffset()
                    // 如果不为0
                    if (scrollOffset != 0 && dataSource.isLoadable() && dataSource.getCurrentState() == StateEntity.STATE_HIDE) {
                        // 找到最后显示的item的索引位置
                        val lastVisibleIndex = when (val manager = recyclerView.layoutManager) {
                            is LinearLayoutManager -> manager.findLastVisibleItemPosition()
                            is StaggeredGridLayoutManager -> if (manager.childCount > 0) manager.findLastVisibleItemPositions(null)[0] else 0
                            is ICustomLayoutManager -> manager.findCustomLastVisibleItemPosition()
                            else -> throw IllegalStateException("If you use custom LayoutManger, please implements ICustomLayoutManager.")
                        }
                        // 离最后一个item差n个时触发加载更多
                        val preloadIndex = (dataSource.size() - preloadOffset).coerceAtLeast(0)
                        if (lastVisibleIndex >= preloadIndex) {
                            dataSource.loadMore()
                        }
                    }
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(onScrollListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        recyclerView.removeOnScrollListener(onScrollListener)
        super.onDetachedFromRecyclerView(recyclerView)
    }
}