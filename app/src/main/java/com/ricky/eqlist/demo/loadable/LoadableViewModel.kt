package com.ricky.eqlist.demo.loadable

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ricky.eqlist.datasource.LoadParams
import com.ricky.eqlist.datasource.LoadResult
import com.ricky.eqlist.datasource.LoadableDataSource
import com.ricky.eqlist.datasource.getAllOf
import com.ricky.eqlist.demo.entity.Article
import com.ricky.eqlist.demo.entity.FullSpan
import com.ricky.eqlist.entity.BaseEntity
import com.ricky.eqlist.entity.LoadState
import com.ricky.eqlist.entity.StateEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


var articles = listOf<Article>()

/**
 *
 * @author Ricky Hal
 * @date 2021/11/23
 */
class LoadableViewModel : ViewModel() {
    private val api = ApiService.create()
    val dataSource: ArticleDataSource = ArticleDataSource()
    val refreshCompleteSignal = MutableLiveData<Boolean>()

    init {
        dataSource.addHeader(FullSpan())
    }

    inner class ArticleDataSource : LoadableDataSource<Int>(viewModelScope) {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int> {
            return try {
                val pageSize = params.pageLimit
                val cursor = params.index ?: 0
                val result = api.getArticle(cursor, pageSize)
                if (result.errorCode != 0) {
                    LoadResult.Error(Exception("Invalid code"))
                } else {
                    LoadResult.Success(result.data?.datas ?: listOf(), result.data?.curPage, true)
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                LoadResult.Error(e)
            }
        }

        override fun onRefreshStateChanged(state: LoadState) {
            super.onRefreshStateChanged(state)
            refreshCompleteSignal.value = state == LoadState.STATE_HIDE
        }

        override fun onError(isRefresh: Boolean, e: Exception) {
            super.onError(isRefresh, e)
            if (isRefresh) dataSource.setState(StateEntity.STATE_ERROR)
        }

        override suspend fun onSaveInstance() {
            withContext(Dispatchers.IO) {
                articles = getAllOf()
            }
        }

        override suspend fun onRestore(): List<BaseEntity> {
            return withContext(Dispatchers.IO) {
                articles
            }
        }
    }
}