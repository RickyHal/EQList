@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.ricky.eqlist.datasource

import com.ricky.eqlist.entity.BaseEntity
import com.ricky.eqlist.entity.StateEntity
import kotlinx.coroutines.CoroutineScope
import java.util.*

/**
 *
 * @author Ricky Hal
 * @date 2021/11/18
 */
open class DataSource(scope: CoroutineScope? = null) : BaseDataSource(scope), IHeader, IFooter {
    private val _headers = mutableListOf<BaseEntity>()
    private val _footers = mutableListOf<BaseEntity>()
    private var cacheHeaders = mutableListOf<BaseEntity>()
    private var cacheFooters = mutableListOf<BaseEntity>()
    private var _currentState: Int = StateEntity.STATE_HIDE

    fun getHeaderSize() = _headers.size
    fun getFooterSize() = _footers.size
    fun getItemSize() = _items.size

    final override fun add(index: Int, item: BaseEntity) {
        if (index <= getItemSize()) {
            doOnMainThread {
                _items.add(index, item)
                submit()
            }
        }
    }

    final override fun add(item: BaseEntity) {
        doOnMainThread {
            _items.add(item)
            submit()
            if (item !is StateEntity) {
                setState(StateEntity.STATE_HIDE)
            }
        }
    }

    final override fun add(vararg item: BaseEntity) = addAll(item.toList())

    final override fun addAll(list: List<BaseEntity>) {
        val start = getItemSize()
        addAll(start, list)
    }

    final override fun addAll(index: Int, list: List<BaseEntity>) {
        doOnMainThread {
            _items.addAll(index, list)
            submit()
            if (list.isNotEmpty() && !list.all { it is StateEntity }) {
                setState(StateEntity.STATE_HIDE)
            }
        }
    }

    final override fun set(list: List<BaseEntity>) {
        doOnMainThread {
            _items.clear()
            _items.addAll(list)
            submit(isInitial = true)
            if (list.isNotEmpty() && !list.all { it is StateEntity }) {
                setState(StateEntity.STATE_HIDE)
            }
        }
    }

    final override fun set(index: Int, item: BaseEntity) {
        doOnMainThread {
            if (getItemSize() == 0) {
                _items.add(item)
            } else {
                _items[index] = item
            }
            submit()
            if (item !is StateEntity) {
                setState(StateEntity.STATE_HIDE)
            }
        }
    }

    final override fun set(index: Int, item: BaseEntity, payload: Any) {
        doOnMainThread {
            if (getItemSize() == 0) {
                _items.add(item)
            } else {
                _items[index] = item
            }
            submit(payload = payload)
            if (item !is StateEntity) {
                setState(StateEntity.STATE_HIDE)
            }
        }
    }

    final override fun invalidate(item: BaseEntity, payload: Any?) {
        doOnMainThread {
            val index = getAll().indexOf(item)
            if (index != -1) {
                update(index, payload = payload)
            }
        }
    }

    final override fun invalidate(from: Int, to: Int) {
        doOnMainThread {
            update(from, to)
        }
    }

    final override fun invalidateAt(index: Int, payload: Any?) {
        doOnMainThread {
            update(index, payload = payload)
        }
    }

    final override fun invalidateAll() {
        doOnMainThread {
            update()
        }
    }

    final override fun remove(index: Int) {
        if (index < getItemSize()) {
            doOnMainThread {
                _items.removeAt(index)
                submit()
            }
        }
    }

    final override fun remove(item: BaseEntity) {
        doOnMainThread {
            val result = _items.remove(item)
            if (result) submit()
        }
    }

    final override fun removeFirst() {
        if (_items.isEmpty()) return
        doOnMainThread {
            _items.removeFirst()
            submit()
        }
    }

    final override fun removeFirstWhen(block: (BaseEntity) -> Boolean) {
        doOnMainThread {
            val item = findFirst(block)
            if (item != null) {
                _items.remove(item)
                submit()
            }
        }
    }

    final override fun removeLast() {
        if (_items.isEmpty()) return
        doOnMainThread {
            _items.removeLast()
            submit()
        }
    }

    final override fun removeLastWhen(block: (BaseEntity) -> Boolean) {
        doOnMainThread {
            val item = findLast(block)
            if (item != null) {
                _items.remove(item)
                submit()
            }
        }
    }

    final override fun removeAll() {
        doOnMainThread {
            _headers.clear()
            _items.clear()
            _footers.clear()
            submit()
        }
    }

    final override fun removeWhen(block: (BaseEntity) -> Boolean) {
        if (_items.isEmpty()) return
        doOnMainThread {
            _items.removeAll(block)
            submit()
        }
    }

    final override fun swap(left: Int, right: Int) {
        doOnMainThread {
            val leftItem = _items[left]
            val rightItem = _items[right]
            _items[left] = rightItem
            _items[right] = leftItem
            submit()
        }
    }

    final override fun move(from: Int, to: Int) {
        doOnMainThread {
            val item = _items[from]
            _items.removeAt(from)
            _items[to] = item
            submit()
        }
    }


    final override fun getAll(): List<BaseEntity> = _headers + _items + _footers
    final override fun getCurrentState(): Int = _currentState
    final override fun setState(state: Int) {
        if (state == getCurrentState()) return
        doOnMainThread {
            when (state) {
                StateEntity.STATE_EMPTY -> {
                    cacheHeaders = ArrayList(_headers)
                    cacheFooters = ArrayList(_footers)
                    clearHeaders()
                    clearFooters()
                    set(listOf(StateEntity(StateEntity.STATE_EMPTY)))
                }
                StateEntity.STATE_ERROR -> {
                    cacheHeaders = ArrayList(_headers)
                    cacheFooters = ArrayList(_footers)
                    clearHeaders()
                    clearFooters()
                    set(listOf(StateEntity(StateEntity.STATE_ERROR)))
                }
                StateEntity.STATE_HIDE -> {
                    removeWhen { it is StateEntity }
                    setHeader(cacheHeaders)
                    setFooter(cacheFooters)
                    cacheHeaders.clear()
                    cacheFooters.clear()
                }
            }
            _currentState = state
        }
    }

    final override fun unBindAdapter() {
        super.unBindAdapter()
    }

    /************************************ Header & Footer ***********************/

    final override fun getFooter(index: Int): BaseEntity = _footers[index]

    final override fun getFooters(): List<BaseEntity> = _footers

    final override fun setFooter(list: List<BaseEntity>) {
        doOnMainThread {
            _footers.clear()
            _footers.addAll(list)
            submit()
        }
    }

    /**
     * 添加Footer
     * @param footer
     * @param index footer区域内的index
     */
    final override fun addFooter(footer: BaseEntity, index: Int) {
        doOnMainThread {
            if (index == -1) {
                _footers.add(footer)
            } else {
                _footers.add(index, footer)
            }
            submit()
        }
    }

    final override fun addFooters(list: List<BaseEntity>) {
        doOnMainThread {
            _footers.addAll(list)
            submit()
        }
    }

    final override fun invalidateFooter(footer: BaseEntity) = invalidate(footer)

    final override fun invalidateFooters() = invalidate(getHeaderSize() + getItemSize(), size())

    final override fun removeFooterAt(index: Int) {
        doOnMainThread {
            _footers.removeAt(index)
            submit()
        }
    }

    final override fun clearFooters() {
        doOnMainThread {
            _footers.clear()
            submit()
        }
    }

    override fun isFooter(index: Int): Boolean {
        return index >= getHeaderSize() + getItemSize()
    }

    override fun isHeader(index: Int): Boolean {
        return index < getHeaderSize()
    }

    final override fun getHeader(index: Int): BaseEntity = _headers[index]

    final override fun getHeaders(): List<BaseEntity> = _headers

    final override fun setHeader(list: List<BaseEntity>) {
        doOnMainThread {
            _headers.clear()
            _headers.addAll(list)
            submit()
        }
    }

    final override fun addHeader(header: BaseEntity, index: Int) {
        doOnMainThread {
            if (index == -1) {
                _headers.add(header)
            } else {
                _headers.add(index, header)
            }
            submit()
        }
    }

    final override fun addHeaders(list: List<BaseEntity>) {
        doOnMainThread {
            _headers.addAll(list)
            submit()
        }
    }

    final override fun invalidateHeader(header: BaseEntity) = invalidate(header)

    final override fun invalidateHeaders() = invalidate(0, getHeaderSize())

    final override fun removeHeaderAt(index: Int) {
        doOnMainThread {
            _headers.removeAt(index)
            submit()
        }
    }

    final override fun clearHeaders() {
        doOnMainThread {
            _headers.clear()
            submit()
        }
    }
}