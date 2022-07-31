package com.ricky.eqlist.datasource

import android.os.Looper
import androidx.recyclerview.widget.RecyclerView
import com.ricky.eqlist.LogUtil
import com.ricky.eqlist.adapter.EQListAdapter
import com.ricky.eqlist.entity.BaseEntity
import kotlinx.coroutines.*

/**
 *
 * @author Ricky Hal
 * @date 2021/11/18
 */
abstract class BaseDataSource(protected var scope: CoroutineScope? = null) {
    protected val _items = ArrayList<BaseEntity>()
    protected var adapter: EQListAdapter? = null
    private var diffUtil: DiffUtil? = null

    abstract fun getAll(): List<BaseEntity>
    operator fun get(index: Int): BaseEntity = getAll()[index]
    abstract fun add(index: Int, item: BaseEntity)
    abstract fun add(item: BaseEntity)
    abstract fun add(vararg item: BaseEntity)
    abstract fun addAll(list: List<BaseEntity>)
    abstract fun addAll(index: Int, list: List<BaseEntity>)
    abstract fun set(list: List<BaseEntity>)
    abstract operator fun set(index: Int, item: BaseEntity)
    abstract fun set(index: Int, item: BaseEntity, payload: Any)
    abstract fun invalidate(item: BaseEntity, payload: Any? = null)
    abstract fun invalidate(from: Int, to: Int)
    abstract fun invalidateAt(index: Int, payload: Any? = null)
    abstract fun invalidateAll()
    abstract fun remove(index: Int)
    abstract fun remove(item: BaseEntity)
    abstract fun removeFirst()
    abstract fun removeLast()
    abstract fun removeFirstWhen(block: (BaseEntity) -> Boolean)
    abstract fun removeLastWhen(block: (BaseEntity) -> Boolean)
    abstract fun removeAll()
    abstract fun removeWhen(block: (BaseEntity) -> Boolean)
    abstract fun swap(left: Int, right: Int)
    abstract fun move(from: Int, to: Int)
    abstract fun setState(state: Int)
    abstract fun getCurrentState(): Int

    fun getItems() = _items
    fun find(block: (BaseEntity) -> Boolean): BaseEntity? = getAll().find(block)
    fun findFirst(block: (BaseEntity) -> Boolean): BaseEntity? = getAll().firstOrNull(block)
    fun findLast(block: (BaseEntity) -> Boolean): BaseEntity? = getAll().lastOrNull(block)
    fun findAll(block: (BaseEntity) -> Boolean): List<BaseEntity> = getAll().filter(block)
    fun iterator() = getAll().iterator()
    fun forEach(block: (BaseEntity) -> Unit) = getAll().forEach(block)
    fun forEachIndexed(block: (Int, BaseEntity) -> Unit) = getAll().forEachIndexed(block)
    fun contain(item: BaseEntity): Boolean = getAll().contains(item)
    fun count(block: (BaseEntity) -> Boolean): Int = getAll().count(block)
    fun size() = getAll().size
    fun isNotEmpty(): Boolean = getAll().isNotEmpty()
    fun isEmpty(): Boolean = getAll().isEmpty()

    internal open fun bindAdapter(adapter: EQListAdapter) {
        this.adapter = adapter
        diffUtil = DiffUtil(adapter)
    }

    internal fun checkScope(recyclerView: RecyclerView) {
        if (scope == null) {
            scope = recyclerView.viewScope
        }
    }

    internal open fun unBindAdapter() {
        scope?.cancel()
        diffUtil = null
    }

    protected fun doOnMainThread(block: () -> Unit) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            block()
        } else {
            scope?.launch(Dispatchers.Main) {
                block()
            }
        }
    }

    protected fun CoroutineScope.safeLaunch(block: suspend () -> Unit): Job {
        return launch(CoroutineExceptionHandler { _, e ->
            if (e is CancellationException) {
                LogUtil.e("网络请求被取消：")
            } else {
                LogUtil.e("加载出错")
            }
            LogUtil.e(e)
        }) {
            block()
        }
    }

    protected fun update(from: Int = -1, to: Int = -1, payload: Any? = null) {
        when {
            from == -1 && to == -1 -> adapter?.notifyItemRangeChanged(0, size(), payload)
            from != -1 && to == -1 -> adapter?.notifyItemChanged(from, payload)
            from != -1 && to != -1 -> adapter?.notifyItemRangeChanged(from, to - from, payload)
            from == -1 && to != -1 -> adapter?.notifyItemRangeChanged(0, to, payload)
        }
    }

    protected fun submit(isInitial: Boolean = false, payload: Any? = null) {
        diffUtil?.submit(getAll(), isInitial, payload)
    }
}

inline fun <reified Entity> BaseDataSource.countOf(): Int {
    return getAll().count { it is Entity }
}

inline fun <reified Entity> BaseDataSource.getAllOf(): List<Entity> {
    return getAll().filter { it is Entity }.map { it as Entity }
}

inline fun <reified Entity> BaseDataSource.forEachOf(block: (Entity) -> Unit) {
    getAllOf<Entity>().forEach(block)
}
