package com.ricky.eqlist.demo.waterfall

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ricky.eqlist.datasource.LoadParams
import com.ricky.eqlist.datasource.LoadResult
import com.ricky.eqlist.datasource.LoadableDataSource
import com.ricky.eqlist.demo.entity.FullSpan
import com.ricky.eqlist.entity.LoadState
import com.ricky.eqlist.entity.StateEntity
import java.util.*

/**
 *
 * @author Ricky Hal
 * @date 2021/11/25
 */
class WaterfallDemoViewModel : ViewModel() {
    val refreshCompleteSignal = MutableLiveData<Boolean>()
    val dataSource: ImageDataSource by lazy { ImageDataSource() }
    private val images = listOf(
        "https://tva2.sinaimg.cn/large/87c01ec7gy1frmmihp11yj21hc0u0hdu.jpg",
        "https://tva2.sinaimg.cn/large/87c01ec7gy1frmmvgfmw3j21kw0w04r1.jpg",
        "https://tva2.sinaimg.cn/large/0060lm7Tly1ftg6otk3cqj31hc0u014s.jpg",
        "https://tva1.sinaimg.cn/large/0060lm7Tly1ftg6oquldpj318g0p04ak.jpg",
        "https://tva4.sinaimg.cn/large/87c01ec7gy1frmrtvbv2ij21hc0u07de.jpg",
        "https://tva3.sinaimg.cn/large/87c01ec7gy1frmrsj1jekj21hc0u0dox.jpg",
        "https://tva1.sinaimg.cn/large/87c01ec7gy1frmmz605z4j21kw0w0qvh.jpg",
        "https://tva1.sinaimg.cn/large/87c01ec7gy1frmmlajv8uj21hc0u0b2b.jpg",
        "https://tva3.sinaimg.cn/large/87c01ec7gy1frmmlso9rgj21hc0u0kjn.jpg",
        "https://tva1.sinaimg.cn/large/87c01ec7gy1frmry78dkhj21hc0u0n6b.jpg",
        "https://tva4.sinaimg.cn/large/87c01ec7gy1frmmvgfmw3j21kw0w04r1.jpg",
        "https://tva1.sinaimg.cn/large/87c01ec7gy1frmrrnjpf4j21hc0u0dot.jpg",
        "https://tva2.sinaimg.cn/large/87c01ec7gy1frmrqzc5woj21hc0u0k07.jpg"
    )

    init {
        dataSource.addHeader(FullSpan())
    }

    inner class ImageDataSource : LoadableDataSource<Int>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int> {
            val result = mutableListOf<ImageItem>()
            repeat(params.pageLimit) {
                result.add(ImageItem(it.toString(), 300 + Random().nextInt(300), images.random()))
            }
            return if (params.index ?: 0 < 5) {
                LoadResult.Success(result, (params.index ?: 0) + 1)
            } else {
                LoadResult.Success(listOf(), params.index)
            }
        }

        override fun onRefresh() {
            loadParams.index = 0
        }

        override fun onRefreshStateChanged(state: LoadState) {
            super.onRefreshStateChanged(state)
            refreshCompleteSignal.value = state == LoadState.STATE_HIDE
        }

        override fun onError(isRefresh: Boolean, e: Exception) {
            super.onError(isRefresh, e)
            if (isRefresh) dataSource.setState(StateEntity.STATE_ERROR)
        }
    }
}