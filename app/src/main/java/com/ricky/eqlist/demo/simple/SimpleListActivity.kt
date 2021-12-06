package com.ricky.eqlist.demo.simple

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ricky.eqlist.demo.R
import com.ricky.eqlist.demo.databinding.ActivitySimpleListBinding
import com.ricky.eqlist.demo.entity.User
import com.ricky.eqlist.entity.StateEntity
import com.ricky.eqlist.initializer.*


class SimpleListActivity : AppCompatActivity() {
    private val binding: ActivitySimpleListBinding by lazy { ActivitySimpleListBinding.inflate(layoutInflater) }
    private val viewModel: SimpleListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.radioGroup
        val width = 30
        val drawable = GradientDrawable()
        drawable.setSize(width, 1)
        binding.radioGroup.dividerDrawable = drawable
        binding.radioGroup.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        binding.btnDo.setOnClickListener {
            when (binding.radioGroup.checkedRadioButtonId) {
                R.id.rb_size -> Toast.makeText(this, viewModel.dataSource.size().toString(), Toast.LENGTH_SHORT).show()
                R.id.rb_add -> viewModel.add()
                R.id.rb_addAll -> viewModel.addAll()
                R.id.rb_addAll1 -> viewModel.addAllToIndex()
                R.id.rb_set1 -> viewModel.set()
                R.id.rb_set2 -> viewModel.setByIndex()
                R.id.rb_set3 -> viewModel.setByIndexWithPayload()
                R.id.rb_remove1 -> viewModel.removeByIndex()
                R.id.rb_remove2 -> viewModel.removeByItem()
                R.id.rb_remove3 -> viewModel.removeFirst()
                R.id.rb_remove4 -> viewModel.removeLast()
                R.id.rb_remove5 -> viewModel.removeFirstWhen()
                R.id.rb_remove6 -> viewModel.removeLastWhen()
                R.id.rb_remove7 -> viewModel.removeAll()
                R.id.rb_remove8 -> viewModel.removeWhen()
                R.id.rb_find1 -> viewModel.find()
                R.id.rb_find2 -> viewModel.findFirst()
                R.id.rb_find3 -> viewModel.findLast()
                R.id.rb_find4 -> viewModel.findAll()
                R.id.rb_forEach -> viewModel.forEach()
                R.id.rb_forEachOf -> viewModel.forEachOf()
                R.id.rb_forEachIndexed -> viewModel.forEachIndexed()
                R.id.rb_contain -> viewModel.contain()
                R.id.rb_get -> viewModel.get()
                R.id.rb_getall -> viewModel.getAll()
                R.id.rb_state_error -> viewModel.setErrorState()
                R.id.rb_state_empty -> viewModel.setEmptyState()
                R.id.rb_state_hide -> viewModel.setHideState()
            }
        }
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.init(viewModel.dataSource) {
            item<User>(R.layout.item_user) {
                onBind { _, data, view ->
                    view.findViewById<TextView>(R.id.tv_id).text = data.id.toString()
                    view.findViewById<TextView>(R.id.tv_name).text = data.name
                }
                onBindPayload { _, data, view, payloads ->
                    Toast.makeText(this@SimpleListActivity, "payload=$payloads", Toast.LENGTH_SHORT).show()
                    if (payloads[0] == "Index") {
                        view.findViewById<TextView>(R.id.tv_id).text = data.id.toString()
                    }
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
                onRetry { viewModel.addAll() }
            }
        }
    }
}