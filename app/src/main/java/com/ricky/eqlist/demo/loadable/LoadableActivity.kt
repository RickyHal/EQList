package com.ricky.eqlist.demo.loadable

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.ricky.eqlist.demo.R
import com.ricky.eqlist.demo.databinding.ItemArticleBinding
import com.ricky.eqlist.demo.databinding.ItemLoadBinding
import com.ricky.eqlist.demo.databinding.PageListBinding
import com.ricky.eqlist.demo.entity.Article
import com.ricky.eqlist.demo.entity.FullSpan
import com.ricky.eqlist.entity.LoadState
import com.ricky.eqlist.entity.StateEntity
import com.ricky.eqlist.entity.isLoading
import com.ricky.eqlist.initializer.*

class LoadableActivity : AppCompatActivity() {
    private val viewModel: LoadableViewModel by lazy {
        ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(LoadableViewModel::class.java)
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
        binding.recyclerView.initWithLoader(viewModel.dataSource) {
            withDebug(true)
            item<FullSpan>(R.layout.item_full_span) {
                withFullSpan(true)
            }
            bindingItem<Article, ItemArticleBinding>(R.layout.item_article) {
                onCreate {
                    ItemArticleBinding.bind(it)
                }
                onBind { _, data, binding ->
                    binding.tvId.text = data.id.toString()
                    binding.tvAuthor.text = data.author
                    binding.tvTitle.text = data.title
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
                    binding.animateView.visibility = if (data.state.isLoading()) View.VISIBLE else View.GONE
                    if (binding.animateView.isVisible) {
                        binding.animateView.setAnimation("loading.json")
                        binding.animateView.playAnimation()
                    } else {
                        binding.animateView.cancelAnimation()
                    }
                    when (data.state) {
                        LoadState.STATE_EMPTY -> {
                            binding.tvNoMore.visibility = View.VISIBLE
                            binding.tvNoMore.text = "没有更多啦"
                        }
                        LoadState.STATE_SUCCESS -> {
                            binding.tvNoMore.visibility = View.VISIBLE
                            binding.tvNoMore.text = "加载成功"
                        }
                        LoadState.STATE_ERROR -> {
                            binding.tvNoMore.visibility = View.VISIBLE
                            binding.tvNoMore.text = "加载失败"
                        }
                        else -> {
                            binding.tvNoMore.visibility = View.GONE
                        }
                    }
                }
            }
        }
        viewModel.dataSource.refresh()
    }
}
