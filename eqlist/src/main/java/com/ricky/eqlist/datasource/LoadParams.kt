package com.ricky.eqlist.datasource

import android.os.Bundle

/**
 *
 * @author Ricky Hal
 * @date 2021/11/23
 */
class LoadParams<Cursor> {
    companion object {
        const val DEFAULT_PAGE_LIMIT = 20
    }

    /** 本次加载使用的页码 */
    var index: Cursor? = null

    /** 每页加载数 */
    var pageLimit: Int = DEFAULT_PAGE_LIMIT

    /** 额外数据 */
    var extra: Bundle? = null
}