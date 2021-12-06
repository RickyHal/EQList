package com.ricky.eqlist.datasource

import com.ricky.eqlist.LogUtil
import com.ricky.eqlist.adapter.EQListAdapter
import com.ricky.eqlist.entity.LoadEntity
import com.ricky.eqlist.entity.LoadState
import com.ricky.eqlist.entity.loadAble
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 * @author Ricky Hal
 * @date 2021/11/18
 */
abstract class LoadableDataSource<Cursor : Any>(scope: CoroutineScope) : DataSource(scope), ILoader {
    protected val loadParams: LoadParams<Cursor> by lazy { createLoadParams() }
    private var isLoaderIdle: Boolean = true
    private val loader = LoadEntity(LoadState.STATE_HIDE)
    private var refreshingJob: Job? = null
    private var loadingMoreJob: Job? = null
    private var readCacheJob: Job? = null
    private var loaderDelay: Long = 300L

    init {
        restore()
    }

    final override fun isRefreshing() = refreshingJob?.isActive == true
    final override fun isLoadingMore() = loadingMoreJob?.isActive == true
    final override fun isLoadable() = !isRefreshing() && !isLoadingMore() && loader.state.loadAble()

    /**
     * 设置loader加载成功到消失之间显示的时间
     * @param delay 单位ms
     */
    internal fun setLoaderDelay(delay: Long) {
        loaderDelay = delay
    }

    /**
     * 初始化加载参数，只会调用一次
     */
    open fun createLoadParams(): LoadParams<Cursor> {
        return LoadParams()
    }

    /**
     * 持久化数据恢复
     */
    final override fun restore() {
        readCacheJob = scope.launch {
            val cachedData = onRestore()
            set(cachedData)
        }
    }

    final override fun refresh() {
        // 正在刷新或加载更多，跳过
        if (!isLoadable()) return
        refreshingJob = scope.safeLaunch {
            LogUtil.d("开始下拉刷新")
            updateLoadState(true, LoadState.STATE_SHOW)
            onRefresh()
            updateLoadState(true, LoadState.STATE_START)
            val result = load()
            proceed(true, result)
            LogUtil.d("刷新完成")
            refreshingJob = null
        }
    }

    final override fun loadMore() {
        if (!isLoadable()) return
        loadingMoreJob = scope.safeLaunch {
            LogUtil.d("开始加载更多")
            updateLoadState(false, LoadState.STATE_SHOW)
            onLoadMore()
            updateLoadState(false, LoadState.STATE_START)
            val result = load()
            proceed(false, result)
            LogUtil.d("加载完成")
            loadingMoreJob = null
        }
    }

    /**
     * 请求数据的方法，需要重写
     * @param params 加载参数，@see[LoadParams]
     * @return 加载结果以及下次请求时使用的索引值，@see[LoadResult]
     */
    abstract suspend fun load(params: LoadParams<Cursor> = loadParams): LoadResult<Cursor>

    private suspend fun proceed(isRefresh: Boolean, result: LoadResult<Cursor>) {
        var delayBlock: suspend () -> Unit = {}
        readCacheJob?.cancel()
        when (result) {
            is LoadResult.Success<Cursor> -> {
                if (isRefresh) {
                    LogUtil.d("刷新成功\nindex=${loadParams.index}\nsize=${result.items.size}\nresult=${result.items}")
                } else {
                    LogUtil.d("加载更多成功\nindex=${loadParams.index}\nsize=${result.items.size}\nresult=${result.items}")
                }
                loadParams.index = result.nextIndex
                if (result.items.isNotEmpty()) {
                    delayBlock = if (isRefresh) {
                        suspend {
                            set(result.items)
                            if (result.saveInstance) onSaveInstance()
                        }
                    } else {
                        suspend {
                            addAll(result.items)
                            if (result.saveInstance) onSaveInstance()
                        }
                    }
                } else {
                    updateLoadState(isRefresh, LoadState.STATE_EMPTY)
                    return
                }

            }
            is LoadResult.Error -> {
                updateLoadState(isRefresh, LoadState.STATE_ERROR)
                onError(isRefresh, result.e)
                if (isRefresh) updateLoadState(isRefresh, LoadState.STATE_HIDE)
                return
            }
        }
        updateLoadState(isRefresh, LoadState.STATE_SUCCESS)
        delay(loaderDelay)
        delayBlock.invoke()
        updateLoadState(isRefresh, LoadState.STATE_HIDE)
    }

    override fun onError(isRefresh: Boolean, e: Exception) {
        if (isRefresh) LogUtil.e("刷新失败") else LogUtil.e("加载更多失败")
        LogUtil.e(e)
    }

    final override fun bindAdapter(adapter: EQListAdapter) {
        super.bindAdapter(adapter)
        if (!contain(loader)) addFooter(loader)
    }

    final override fun getLoader(): LoadEntity = loader

    private fun updateLoadState(isRefresh: Boolean, state: LoadState) {
        if (isRefresh) onRefreshStateChanged(state) else onLoadMoreStateChanged(state)
    }

    override fun onLoadMoreStateChanged(state: LoadState) {
        loader.state = state
        invalidateFooter(loader)
    }

    override fun onRefreshStateChanged(state: LoadState) {
        // 外部自己处理
    }
}