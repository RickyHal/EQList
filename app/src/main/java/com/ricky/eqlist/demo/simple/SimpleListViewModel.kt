package com.ricky.eqlist.demo.simple

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ricky.eqlist.datasource.DataSource
import com.ricky.eqlist.datasource.forEachOf
import com.ricky.eqlist.demo.entity.User
import com.ricky.eqlist.entity.StateEntity
import java.util.*

/**
 *
 * @author Ricky Hal
 * @date 2021/11/19
 */
class SimpleListViewModel : ViewModel() {
    companion object {
        const val TAG = "EQList"
    }

    val dataSource: DataSource = DataSource(viewModelScope)
    fun add() {
        dataSource.add(User(dataSource.size() + 1, "张三"))
    }

    fun addAll() {
        val users = listOf(User(Random().nextInt(), "张三"), User(Random().nextInt(), "李四"))
        dataSource.addAll(users)
    }

    fun addAllToIndex() {
        val users = listOf(User(Random().nextInt(), "vaojvnao"))
        dataSource.addAll((dataSource.size() - 1).coerceAtLeast(0), users)
    }

    fun set() {
        val users = listOf(User(Random().nextInt(), "张三"), User(Random().nextInt(), "李四"))
        dataSource.set(users)
    }

    fun setByIndex() {
//        dataSource[0] = User(Random().nextInt(), "张三")
        dataSource.set(0, User(Random().nextInt(), "张三"))
    }

    fun setByIndexWithPayload() {
        dataSource.set(0, User(Random().nextInt(), "张三"), "Index")
    }

    fun removeByIndex() {
        dataSource.remove(0)
    }

    fun removeByItem() {
        dataSource.remove(dataSource[0])
    }

    fun removeFirst() {
        dataSource.removeFirst()
    }

    fun removeLast() {
        dataSource.removeLast()
    }

    fun removeFirstWhen() {
        dataSource.removeFirstWhen {
            it is User && it.name == "张三"
        }
    }

    fun removeLastWhen() {
        dataSource.removeLastWhen {
            it is User && it.name == "张三"
        }
    }

    fun removeAll() {
        dataSource.removeAll()
    }

    fun removeWhen() {
        dataSource.removeWhen {
            it is User && it.name == "张三"
        }
    }

    fun find() {
        dataSource.find {
            it is User && it.name == "张三"
        }?.let {
            dataSource.set(listOf(it))
        }
    }

    fun findFirst() {
        dataSource.findFirst {
            it is User && it.name == "张三"
        }?.let {
            dataSource.set(listOf(it))
        }
    }

    fun findLast() {
        dataSource.findLast {
            it is User && it.name == "张三"
        }?.let {
            dataSource.set(listOf(it))
        }
    }

    fun findAll() {
        dataSource.findAll {
            it is User && it.name == "张三"
        }.let {
            dataSource.set(it)
        }
    }

    fun forEach() {
        dataSource.forEach {
            it as User
            Log.d(TAG, "id=${it.id}  name=${it.name}")
        }
    }

    fun forEachOf() {
        dataSource.forEachOf<User> {
            Log.d(TAG, "id=${it.id}  name=${it.name}")
        }
    }

    fun forEachIndexed() {
        dataSource.forEachIndexed { index, user ->
            user as User
            Log.d(TAG, "index=$index id=${user.id}  name=${user.name}")
        }
    }

    fun contain() {
        Log.d(TAG, "contain User(id=0,name=\"张三\") ：${dataSource.contain(User(id = 0, name = "张三"))}")
    }

    fun get() {
        val user = dataSource[0] as User
        Log.d(TAG, "id=${user.id}  name=${user.name}")
    }

    fun getAll() {
        dataSource.getAll().forEach {
            it as User
            Log.d(TAG, "id=${it.id}  name=${it.name}")
        }
    }

    fun setErrorState() {
        dataSource.setState(StateEntity.STATE_ERROR)
    }

    fun setEmptyState() {
        dataSource.setState(StateEntity.STATE_EMPTY)
    }

    fun setHideState() {
        dataSource.setState(StateEntity.STATE_HIDE)
    }
}