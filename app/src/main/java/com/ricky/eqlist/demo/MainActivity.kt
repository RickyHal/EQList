package com.ricky.eqlist.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ricky.eqlist.demo.databinding.ActivityMainBinding
import com.ricky.eqlist.demo.grid.GridDemoActivity
import com.ricky.eqlist.demo.loadable.LoadableActivity
import com.ricky.eqlist.demo.simple.SimpleListActivity
import com.ricky.eqlist.demo.waterfall.WaterfallDemoActivity

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.toSimpleList.setOnClickListener {
            startActivity(Intent(this, SimpleListActivity::class.java))
        }
        binding.toGridList.setOnClickListener {
            startActivity(Intent(this, GridDemoActivity::class.java))
        }
        binding.toStaggeredGridList.setOnClickListener {
            startActivity(Intent(this, WaterfallDemoActivity::class.java))
        }
        binding.toLoadableList.setOnClickListener {
            startActivity(Intent(this, LoadableActivity::class.java))
        }
    }
}