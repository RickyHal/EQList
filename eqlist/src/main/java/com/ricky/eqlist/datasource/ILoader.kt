package com.ricky.eqlist.datasource

import com.ricky.eqlist.entity.BaseEntity
import com.ricky.eqlist.entity.LoadEntity
import com.ricky.eqlist.entity.LoadState

/**
 *
 * @author Ricky Hal
 * @date 2021/11/18
 */
interface ILoader {
    /**
     * 判断是否正在刷新
     */
    fun isRefreshing(): Boolean

    /**
     * 判断是否正在加载更多
     */
    fun isLoadingMore(): Boolean

    /**
     * 判断当前是否可触发加载更多
     */
    fun isLoadable(): Boolean

    /**
     * 持久化数据恢复
     */
    fun restore()

    fun loadMore()
    fun refresh()

    /**
     * 刷新之前调用一次，可用于准备加载需要的参数
     * 刷新将前置索引置空，也可继承后自己处理
     */
    fun onRefresh() {}

    /**
     * 加载更多之前调用一次，可用于准备加载需要的参数
     * 基本不需要做什么处理
     */
    fun onLoadMore() {}

    /**
     * 数据持久化保存，需要自己继承重写
     */
    suspend fun onSaveInstance() {}

    /**
     * 持久化数据恢复，需要自己继承重写
     */
    suspend fun onRestore(): List<BaseEntity> = emptyList()

    /**
     * 加载过程中触发异常
     */
    fun onError(isRefresh: Boolean, e: Exception)
    fun getLoader(): LoadEntity
    fun onLoadMoreStateChanged(state: LoadState)
    fun onRefreshStateChanged(state: LoadState)
}
