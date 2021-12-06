package com.ricky.eqlist.demo.grid

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ricky.eqlist.demo.R
import com.ricky.eqlist.demo.databinding.ItemGridDemoBinding
import com.ricky.eqlist.demo.databinding.PageListBinding
import com.ricky.eqlist.demo.entity.Footer
import com.ricky.eqlist.demo.entity.FullSpan
import com.ricky.eqlist.demo.entity.Header
import com.ricky.eqlist.demo.entity.Num
import com.ricky.eqlist.initializer.bindingItem
import com.ricky.eqlist.initializer.init
import com.ricky.eqlist.initializer.item

class GridDemoActivity : AppCompatActivity() {
    private val viewModel: GridDemoViewModel by lazy {
        ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(GridDemoViewModel::class.java)
    }
    private val binding: PageListBinding by lazy { PageListBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.refreshLayout.isEnabled = false
        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.init(viewModel.dataSource, layoutManager) {
            item<FullSpan>(R.layout.item_full_span) {
                withFullSpan(true)
            }
            item<Header>(R.layout.item_full_span) {
                onBind { _, data, view ->
                    view as TextView
                    view.text = "Header"
                }
            }
            item<Footer>(R.layout.item_full_span) {
                onBind { _, _, view ->
                    view as TextView
                    view.text = "Footer"
                }
            }
            bindingItem<Num, ItemGridDemoBinding>(R.layout.item_grid_demo) {
                onCreate { ItemGridDemoBinding.bind(it) }
                onBind { _, data, binding ->
                    binding.root.text = data.num
                }
            }
        }
    }
}