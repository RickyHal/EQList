package com.ricky.eqlist.datasource

import com.ricky.eqlist.entity.BaseEntity


/**
 *
 * @author Ricky Hal
 * @date 2021/11/23
 */
sealed class LoadResult<Cursor : Any> {
    class Success<Cursor : Any>(
        val items: List<BaseEntity>,
        val nextIndex: Cursor? = null,
        val saveInstance: Boolean = false
    ) : LoadResult<Cursor>()

    class Error<Cursor : Any>(val e: Exception) : LoadResult<Cursor>()
}