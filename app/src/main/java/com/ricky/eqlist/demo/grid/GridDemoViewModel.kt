package com.ricky.eqlist.demo.grid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ricky.eqlist.datasource.DataSource
import com.ricky.eqlist.demo.entity.Footer
import com.ricky.eqlist.demo.entity.FullSpan
import com.ricky.eqlist.demo.entity.Header
import com.ricky.eqlist.demo.entity.Num

/**
 *
 * @author Ricky Hal
 * @date 2021/11/25
 */
class GridDemoViewModel : ViewModel() {
    val dataSource: DataSource by lazy { DataSource(viewModelScope) }

    init {
        dataSource.add(FullSpan())
        dataSource.addHeader(Header())
        repeat(100) {
            dataSource.add(Num(it.toString()))
        }
        dataSource.addFooter(Footer(), 0)
    }
}