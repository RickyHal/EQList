# EQList

EQList（E-QuickList）[![](https://jitpack.io/v/RickyHal/EQList.svg)](https://jitpack.io/#RickyHal/EQList)是一款Kotlin实现的功能强大的RecyclerView渲染框架，使用DSL方式快速构建RecyclerView，无需实现Adapter和ViewHolder。[GitHub](https://github.com/RickyHal/EQList)，<a
href="https://github.com/RickyHal/EQList/tree/main/results/apk">Demo下载</a>

<img src="/results/guide.png">

目前支持的功能：

- [x] 支持MultiViewType
- [x] 支持Header和Footer
- [x] 支持自定义状态UI
- [x] 支持自定义加载更多UI
- [x] 支持自定义加载更多完成UI显示时间
- [x] 支持自定义页面数据离线缓存
- [x] 支持自动数据加载以及分页
- [x] 支持控制触发加载更多的条件
- [x] 支持DataBinding，ViewBinding
- [x] 支持自定义LayoutManager
- [x] 支持FullSpan
- [x] 支持DiffUtil
- [x] 支持Payload更新

# 依赖配置

项目 build.gradle

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://www.jitpack.io' }
    }
}
```

模块 build.gradle

```groovy
dependencies {
    implementation 'com.github.RickyHal:EQList:$latest_version'
}
```

# 使用方法

日常开发中我们常常会渲染两种类型的RecyclerView，一种是不需要加载更多的，一种是需要加载更多的，EQList分开处理了这两种情况。

## 基本概念

在使用之前你需要先了解本框架的几个基本概念。

### DataSource

你可以理解为一个ArrayList，用于存储你要渲染的数据，当你通过DataSource的增删改除方法修改数据时，DataSource内部会自动通知RecyclerView更新UI。DataSource内部有许多同ArrayList类似的方法，供操作数据使用，具体请查看源码。

DataSource用于不需要加载更多的情况。

### LoadableDataSource

同DataSource一样，但是多了一些方法，用于需要加载更多的情况。LoadableDataSource是一个抽象类，需要自己继承重写。

### LoadParam

网络请求时的参数，包含三个字段，pageLimit（每页加载数），index（索引值），extra（额外的数据，是一个Bundle）。

### LoadResult

加载结果，需要在LoadableDataSource的load方法中返回，有LoadResult.Success和LoadResult.Error两种类型。

### Item

可以理解为RecyclerView中的一项，如用户列表中的一个用户。

### BaseEntity

基础实体类，渲染数据列表时传入的实体类需要继承自此类

### StateEntity

加载状态实体类，继承自BaseEntity，包含STATE_HIDE、STATE_EMPTY、STATE_ERROR三个状态

### LoadState

加载状态，包含STATE_SHOW、STATE_START、STATE_SUCCESS、STATE_ERROR、STATE_EMPTY、STATE_HIDE等几个状态

### Header/Footer

数据列表顶部或底部的独立出来的item，当调用DataSource的removeAll()方法时，会清空列表中除Header和Footer的所有item，框架中的加载更多item就是一个自定义的Footer。

## 渲染不可加载更多的列表

举个🌰，先在ViewModel创建DataSource

```kotlin
data class User(val id: Int, val name: String) : BaseEntity()

class DemoViewModel : ViewModel() {
    // 可以传入协程scope，也可以不传，默认使用的是Activity的LifecycleScope
    val dataSource: DataSource = DataSource(viewModelScope)

    fun add() {
        dataSource.add(User(dataSource.size() + 1, "张三"))
    }
}
```

然后初始化RecyclerView：

```kotlin
binding.recyclerView.init(viewModel.dataSource, layoutManager) {
    // 是否打开Debug模式，方便调试
    withDebug(true)
    onAttach {
        // 相当Adapter中的onAttachedToRecyclerView方法，
    }
    onDetach {
        // 相当Adapter中的onDetachedFromRecyclerView方法，
    }

    // 定义一种类型的Item，不使用ViewBinding
    // User为Item对应的实体类，需要继承BaseEntity
    item<User>(R.layout.item_user) {
        // LayoutManager为GridLayoutManager或StaggeredGridLayoutManager时是否拉通为一行
        withFullSpan(true)
        onCreate { view ->
            // 相当Adapter中的onCreateViewHolder方法，view为创建的View
        }
        onBind { position, data, view ->
            // 相当Adapter中的onBindViewHolder方法，data为对应位置的User
            // 可在此更新Item UI
            view.findViewById<TextView>(R.id.tv_user_name).text = data.name
            val avatar = view.findViewById<ImageView>(R.id.iv_avatar)
            // 可以直接拿到recyclerView
            Glide.with(recyclerView.context).load(R.drawable.default_avatar).into(avatar)
        }
        onBindPayload { position, data, view, payloads ->
            // 相当Adapter中的onBindViewHolder方法，data为对应位置的User,payload为更新DataSource时传入的payload
            // 可在此局部更新Item UI
        }
        onRecycle { view ->
            // 相当Adapter中的onViewRecycled方法，
        }
        onItemAttach { view ->
            // 相当Adapter中的onViewAttachedToWindow方法，
        }
        onItemDetach { view ->
            // 相当Adapter中的onViewDetachedFromWindow方法，
        }
    }
    // 使用ViewBinding
    bindingItem<User, ItemUserBinding>(R.layout.item_user) {
        // 同item
        withFullSpan(true)
        onCreate { view ->
            // 同item，需要返回Binding
            ItemUserBinding.bind(view)
        }
        onBind { position, data, binding ->
            // 同item
        }
        onBindPayload { position, data, binding, payloads ->
            // 同item
        }
        onRecycle { binding ->
            // 同item
        }
        onItemAttach { binding ->
            // 同item
        }
        onItemDetach { binding ->
            // 同item
        }
    }
    // 当网络不可用或下拉刷新数据为空时显示，会覆盖在整个RecyclerView上
    stateItem(R.layout.item_state) {
        onCreate { view ->
            // 同item
        }
        onBind { position, data, view ->
            // 同item，data为对应的状态，有STATE_HIDE、STATE_EMPTY、STATE_ERROR三种状态
            // 通过data.state取状态
            // 可通过retry()方法重新发起请求
            view.findViewById<Button>(R.id.tv_retry).setOnClickListener {
                retry()
            }
        }
        onBindPayload { position, data, view, payloads ->
            //
        }
        onBindPayload { position, data, binding, payloads ->
            // 同item
        }
        onRecycle { binding ->
            // 同item
        }
        onItemAttach { binding ->
            // 同item
        }
        onItemDetach { binding ->
            // 同item
        }
        onRetry {
            // 当通过retry()方法重新发起请求时，会回调至此
            viewModel.refresh()
        }
    }
    stateBindingItem<ItemStateBinding>(R.layout.item_state) {
        onCreate { view ->
            // 同bindingItem
            ItemStateBinding.bind(view)
        }
        onBind { position, data, binding ->
            // 同bindingItem
        }
        onBindPayload { position, data, binding, payloads ->
            // 同bindingItem
        }
        onRecycle { binding ->
            // 同bindingItem
        }
        onItemAttach { binding ->
            // 同bindingItem
        }
        onItemDetach { binding ->
            // 同bindingItem
        }
        onRetry {
            // 同stateItem
            viewModel.refresh()
        }
    }
}
```

每个Item对应一个实体类，切记不要多个Item使用同一个实体类。

以上方法并不是一定要所有都实现，当使用item的方式时，最少只需实现onBind方法来更新UI，当使用bindingItem的方式时，需要实现onCreate方法返回Binding，然后在onBind方法中更新UI。

此时当我们调用ViewModel的add方法时，RecyclerView显示的列表就会自动添加一个用户。

## 渲染可加载更多的数据列表

在ViewModel中创建LoadableDataSource

```kotlin
class LoadableDemoViewModel : ViewModel() {
    private val api = ApiService.create()
    val dataSource: UserDataSource = UserDataSource()
    val refreshCompleteSignal = MutableLiveData<Boolean>()

    // 需要传入索引值类型
    inner class UserDataSource : LoadableDataSource<Int>(viewModelScope) {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int> {
            return try {
                val pageSize = params.pageLimit
                val cursor = params.index ?: 0
                val result = api.getUsers(cursor, pageSize)
                if (result.errorCode != 0) {
                    LoadResult.Error(Exception("Invalid code"))
                } else {
                    // 请求成功，返回用户列表，下次加载时使用的索引，是否缓存本次加载的数据
                    LoadResult.Success(result.data?.datas ?: listOf(), result.data?.curPage, true)
                }
            } catch (e: Exception) {
                // 协程取消是会触发异常的，这里不处理
                if (e is CancellationException) {
                    throw e
                }
                // 返回加载出错
                LoadResult.Error(e)
            }
        }

        override fun createLoadParams(): LoadParams<Int> {
            // 初始化网络请求参数
            return super.createLoadParams().apply {
                index = -1
                pageLimit = 20
                extra = Bundle().apply {
                    putString("***", "***")
                }
            }
        }

        override fun onError(isRefresh: Boolean, e: Exception) {
            super.onError(isRefresh, e)
            // 加载出错，显示State View
            if (isRefresh) dataSource.setState(StateEntity.STATE_ERROR)
        }

        override fun onLoadMore() {
            super.onLoadMore()
            // 加载更多之前的回调，基本不需要做啥处理
        }

        override fun onRefresh() {
            super.onRefresh()
            // 下拉刷新之前的回调，可以用于重置索引值
            loadParams.index = -1
        }

        override fun onRefreshStateChanged(state: LoadState) {
            super.onRefreshStateChanged(state)
            // 下拉刷新状态改变，可用于监听下拉刷新是否完成，完成后隐藏下拉刷新的UI
            refreshCompleteSignal.value = state == LoadState.STATE_HIDE
        }

        override fun onLoadMoreStateChanged(state: LoadState) {
            super.onLoadMoreStateChanged(state)
            // 加载更多状态改变，基本不需要处理
        }

        override suspend fun onSaveInstance() {
            super.onSaveInstance()
            // suspend方法，缓存本次加载的数据，只会在LoadResult.Success的saveInstance为true时才回调
            // getAllOf表示获取DataSource中的所有用户，不包括Header和Footer
            CacheHelper.saveUsers(getAllOf())
        }

        override suspend fun onRestore(): List<BaseEntity> {
            // 恢复缓存的数据，可从缓存中读取数据，并返回，用于离线状态下初次进入RecyclerView时渲染上次缓存的数据
            return withContext(Dispatchers.IO) {
                CacheHelper.getUsers()
            }
        }
    }
}
```

初始化RecyclerView

```kotlin
binding.refreshLayout.setOnRefreshListener {
    viewModel.dataSource.refresh()
}
viewModel.refreshCompleteSignal.observe(this) {
    binding.refreshLayout.isRefreshing = !it
}
// preloadOffset为离RecyclerView底部n个item时触发加载更多
binding.recyclerView.initWithLoader(viewModel.dataSource, layoutManager, preloadOffset = 5) {
    // 其它item渲染如上一样
    // ...
    // 加载更多BindingItem，也可使用loadItem
    loadBindingItem<ItemLoadBinding>(R.layout.item_load) {
        // 加载成功到消失延迟显示的时间
        withDelay(500)
        onCreate { view ->
            ItemLoadBinding.bind(view)
        }
        onBind { _, data, binding ->
            binding.animateView.visibility = if (data.state.isLoading()) View.VISIBLE else View.GONE
            // 根据不同状态处理需要显示的UI
            when (data.state) {
                LoadState.STATE_EMPTY -> {
                    binding.tvNoMore.visibility = View.VISIBLE
                    binding.tvNoMore.text = "没有更多啦"
                }
                LoadState.STATE_SUCCESS -> {
                    binding.tvNoMore.visibility = View.VISIBLE
                    binding.tvNoMore.text = "加载成功"
                }
                LoadState.STATE_ERROR -> {
                    binding.tvNoMore.visibility = View.VISIBLE
                    binding.tvNoMore.text = "加载失败"
                }
                else -> {
                    binding.tvNoMore.visibility = View.GONE
                }
            }
        }
    }
}
viewModel.dataSource.refresh()
```

## Item DSL复用

只需使用Kotlin的扩展方法即可，如

```kotlin
// 不可加载更多的item
fun InitialDsl.userItem() {
    item<User>(R.layout.item_user) {
        onBind { position, user, view ->

        }
    }
}

// 可加载更多的item
fun LoadableInitialDsl.loadItem() {
    loadItem(R.layout.item_load) {
        onBind { position, state, view ->

        }
    }
}

binding.recyclerView.init(viewModel.dataSource) {
    userItem()
}
binding.recyclerView.initWithLoader(viewModel.dataSource) {
    userItem()
    loadItem()
}
```

## Payload 更新

DataSource有三个方法支持传入payload，分别是set，invalidate，invalidateAt，只需要在item dsl的onBindPayload中处理payload即可

## DiffUtil

实体类继承自BaseEntity，重写areItemsTheSame方法和areContentsTheSame方法即可

## 自定义LayoutManager

自定义的LayoutManager需要继承ICustomLayoutManager接口，重写getCustomOrientation方法和findCustomLastVisibleItemPosition方法即可

```kotlin
interface ICustomLayoutManager {
    /**
     * 返回LayoutManager的滚动方向
     */
    @RecyclerView.Orientation
    fun getCustomOrientation(): Int

    /**
     * 返回最后一个可见的item的位置
     */
    fun findCustomLastVisibleItemPosition(): Int
}
```

## 更新日志

- V1.0.3 2021-12-29

修复Datasource不传入scope时的崩溃

- V1.0.3 2021-12-29

修复编译问题

- V1.0.2 2021-12-29

DataSource支持不传scope，可在DSL任意位置获取recyclerView

- V1.0.1 2021-12-06

初始版本

## License

> ```
> Copyright 2021 RickyHal
>
> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this file except in compliance with the License.
> You may obtain a copy of the License at
>
>    http://www.apache.org/licenses/LICENSE-2.0
>
> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.
> ```

