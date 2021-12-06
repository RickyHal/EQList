package com.ricky.eqlist.demo.waterfall

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import com.ricky.eqlist.demo.R
import com.ricky.eqlist.demo.databinding.ItemLoadBinding
import com.ricky.eqlist.demo.databinding.ItemWaterfallBinding
import com.ricky.eqlist.demo.databinding.PageListBinding
import com.ricky.eqlist.demo.entity.FullSpan
import com.ricky.eqlist.entity.LoadState
import com.ricky.eqlist.entity.StateEntity
import com.ricky.eqlist.initializer.*

class WaterfallDemoActivity : AppCompatActivity() {
    private val viewModel: WaterfallDemoViewModel by lazy {
        ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(WaterfallDemoViewModel::class.java)
    }
    private val binding: PageListBinding by lazy { PageListBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.refreshLayout.setOnRefreshListener {
            viewModel.dataSource.refresh()
        }
        viewModel.refreshCompleteSignal.observe(this) {
            binding.refreshLayout.isRefreshing = !it
        }
        binding.recyclerView.itemAnimator = null
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.initWithLoader(viewModel.dataSource, layoutManager) {
            withDebug(true)
            item<FullSpan>(R.layout.item_full_span) {
                withFullSpan(true)
            }
            bindingItem<ImageItem, ItemWaterfallBinding>(R.layout.item_waterfall) {
                onCreate { ItemWaterfallBinding.bind(it) }
                onBind { _, data, binding ->
                    binding.tvName.text = data.name
                    binding.ivImage.layoutParams.height = data.height
                    binding.ivImage.load(data.url) {
                        transformations(RoundedCornersTransformation(10f))
                    }
                }
                onRecycle { binding ->
                    binding.tvName.text = ""
                    binding.ivImage.background = null
                }
            }
            stateItem(R.layout.item_state) {
                onCreate { view ->
                    val btnRetry = view.findViewById<View>(R.id.btn_retry)
                    btnRetry.setOnClickListener { retry() }
                }
                onBind { _, data, view ->
                    val btnRetry = view.findViewById<View>(R.id.btn_retry)
                    view.findViewById<TextView>(R.id.tv_state).text = when (data.state) {
                        StateEntity.STATE_ERROR -> {
                            btnRetry.visibility = View.VISIBLE
                            "ERROR"
                        }
                        StateEntity.STATE_EMPTY -> {
                            btnRetry.visibility = View.VISIBLE
                            "EMPTY"
                        }
                        else -> {
                            btnRetry.visibility = View.GONE
                            ""
                        }
                    }
                }
                onRetry { viewModel.dataSource.refresh() }
            }
            loadBindingItem<ItemLoadBinding>(R.layout.item_load) {
                onCreate { view ->
                    ItemLoadBinding.bind(view)
                }
                onBind { _, data, binding ->
                    binding.root.visibility = if (data.state != LoadState.STATE_HIDE) View.VISIBLE else View.GONE
                    binding.animateView.visibility = if (data.state != LoadState.STATE_ERROR && data.state != LoadState.STATE_EMPTY) View.VISIBLE else View.GONE
                    when (data.state) {
                        LoadState.STATE_EMPTY -> {
                            binding.tvNoMore.visibility = View.VISIBLE
                            binding.tvNoMore.text = "没有更多啦"
                        }
                        LoadState.STATE_ERROR -> {
                            binding.tvNoMore.visibility = View.VISIBLE
                            binding.tvNoMore.text = "加载失败"
                        }
                        else -> {
                            binding.tvNoMore.visibility = View.GONE
                        }
                    }

//                    when (data.state) {
//                        LoadState.STATE_START -> "加载中..."
//                        LoadState.STATE_SUCCESS -> "加载成功"
//                        LoadState.STATE_EMPTY -> "没有更多啦"
//                        LoadState.STATE_ERROR -> "加载失败"
//                        LoadState.STATE_COMPLETE -> "加载完成"
//                        LoadState.STATE_HIDE -> {
//                            binding.root.visibility = View.GONE
//                            ""
//                        }
//                        LoadState.STATE_SHOW -> {
//                            binding.root.visibility = View.VISIBLE
//                            "11111"
//                        }
//                    }
                }
            }
        }
        viewModel.dataSource.refresh()
    }
}