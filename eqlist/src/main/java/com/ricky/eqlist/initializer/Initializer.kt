package com.ricky.eqlist.initializer

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ricky.eqlist.adapter.EQListAdapter
import com.ricky.eqlist.adapter.LoadableAdapter
import com.ricky.eqlist.datasource.DataSource
import com.ricky.eqlist.datasource.LoadableDataSource

/**
 *
 * @author Ricky Hal
 * @date 2021/11/18
 */

private fun RecyclerView.setupLayoutManger(layoutManager: RecyclerView.LayoutManager?) {
    var lm = layoutManager
    if (lm == null) {
        lm = LinearLayoutManager(context).also {
            it.orientation = LinearLayoutManager.VERTICAL
        }
    }
    this.layoutManager = lm
}

fun RecyclerView.init(dataSource: DataSource, layoutManager: RecyclerView.LayoutManager? = null, initialDsl: InitialDsl.() -> Unit) {
    setupLayoutManger(layoutManager)
    val initializer = InitialDsl(this).apply(initialDsl)
    adapter = EQListAdapter(dataSource, initializer, this.layoutManager!!).also { dataSource.bindAdapter(it) }
    dataSource.checkScope(this)
}

fun RecyclerView.initWithLoader(
    dataSource: LoadableDataSource<*>,
    layoutManager: RecyclerView.LayoutManager? = null,
    preloadOffset: Int = 3,
    initialDsl: LoadableInitialDsl.() -> Unit
) {
    setupLayoutManger(layoutManager)
    val initializer = LoadableInitialDsl(dataSource, this).apply(initialDsl)
    adapter = LoadableAdapter(preloadOffset, dataSource, initializer, this.layoutManager!!).also { dataSource.bindAdapter(it) }
    dataSource.checkScope(this)
}