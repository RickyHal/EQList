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

/**
 *
 *
 * data class User(val id : Int) : EItem
 * binding.recyclerView.init(dataSource, layoutManager) {
 *      item<User> {
 *          onCreate { position -> return view }
 *          onBind { position, data, view -> }
 *          onBindPayload { position, data, view, payloads -> }
 *          onRecycle { view -> }
 *          onItemAttach { view -> }
 *          onItemDetach { view -> }
 *          onAttach { }
 *          onDetach { }
 *      }
 *      bindingItem<User, ItemUserBinding> {
 *          onCreate { position -> return binding }
 *          onBind { position, data, binding ->}
 *          onBindPayload { position, data, payload, binding-> }
 *          onRecycle { binding -> }
 *          onItemAttach { binding -> }
 *          onItemDetach { binding -> }
 *          onAttach { }
 *          onDetach { }
 *      }
 *      emptyItem {
 *          onCreate { position -> return view }
 *          onBind { position, data, view -> view.click { retry() } }
 *          onBindPayload { position, data, view, payloads -> view.click { retry() }  }
 *          onRecycle { view -> }
 *          onItemAttach { view -> }
 *          onItemDetach { view -> }
 *          onAttach { }
 *          onDetach { }
 *      }
 *      emptyBindingItem<ItemUserEmptyBinding> {
 *          onCreate { position -> return binding }
 *          onBind { position, data, binding -> binding.root.click { retry() }}
 *          onBindPayload { position, data, payload, binding-> -> binding.root.click { retry() }}
 *          onRecycle { binding -> }
 *          onItemAttach { binding -> }
 *          onItemDetach { binding -> }
 *          onAttach { }
 *          onDetach { }
 *      }
 *      errorItem {
 *          onCreate { position -> return view }
 *          onBind { position, data, view -> view.click { retry() } }
 *          onBindPayload { position, data, view, payloads -> view.click { retry() }  }
 *          onRecycle { view -> }
 *          onItemAttach { view -> }
 *          onItemDetach { view -> }
 *          onAttach { }
 *          onDetach { }
 *      }
 *      errorBindingItem<ItemUserEmptyBinding> {
 *          onCreate { position -> return binding }
 *          onBind { position, data, binding -> binding.root.click { retry() }}
 *          onBindPayload { position, data, payload, binding-> -> binding.root.click { retry() }}
 *          onRecycle { binding -> }
 *          onItemAttach { binding -> }
 *          onItemDetach { binding -> }
 *          onAttach { }
 *          onDetach { }
 *      }
 * }
 *
 * binding.recyclerView.initWithLoader(dataSource, layoutManager) {
 *      item<User> {
 *          onCreate { position -> return view }
 *          onBind { position, data, view -> }
 *          onBindPayload { position, data, view, payloads -> }
 *          onRecycle { view -> }
 *          onItemAttach { view -> }
 *          onItemDetach { view -> }
 *          onAttach { }
 *          onDetach { }
 *      }
 *      bindingItem<User, ItemUserBinding> {
 *          onCreate { position -> return binding }
 *          onBind { position, data, binding ->}
 *          onBindPayload { position, data, payload, binding-> }
 *          onRecycle { binding -> }
 *          onItemAttach { binding -> }
 *          onItemDetach { binding -> }
 *          onAttach { }
 *          onDetach { }
 *      }
 *      emptyItem {
 *          onCreate { position ->
 *              binding.root.click { retry() }
 *              return binding
 *          }
 *          onBind { position, data, view -> view.click { retry() } }
 *          onBindPayload { position, data, view, payloads -> view.click { retry() }  }
 *          onRecycle { view -> }
 *          onItemAttach { view -> }
 *          onItemDetach { view -> }
 *          onAttach { }
 *          onDetach { }
 *          onRetryStart { view -> }
 *          onRetryEnd { view -> }
 *      }
 *      emptyBindingItem<ItemUserEmptyBinding> {
 *          onCreate { position ->
 *              view.click { loadMore() }
 *              return view
 *          }
 *          onBind { position, data, binding -> binding.root.click { retry() }}
 *          onBindPayload { position, data, payload, binding-> -> binding.root.click { retry() }}
 *          onRecycle { binding -> }
 *          onItemAttach { binding -> }
 *          onItemDetach { binding -> }
 *          onAttach { }
 *          onDetach { }
 *          onRetryStart { binding-> }
 *          onRetryEnd { binding -> }
 *      }
 *      errorItem {
 *          onCreate { position -> return view }
 *          onBind { position, data, view -> view.click { retry() } }
 *          onBindPayload { position, data, view, payloads -> view.click { retry() }  }
 *          onRecycle { view -> }
 *          onItemAttach { view -> }
 *          onItemDetach { view -> }
 *          onAttach { }
 *          onDetach { }
 *          onRetryStart { view -> }
 *          onRetryEnd { view -> }
 *      }
 *      errorBindingItem<ItemUserEmptyBinding> {
 *          onCreate { position ->
 *              binding.root.click { retry() }
 *              return binding
 *          }
 *          onBind { position, data, binding -> }
 *          onBindPayload { position, data, payload, binding-> -> }
 *          onRecycle { binding -> }
 *          onItemAttach { binding -> }
 *          onItemDetach { binding -> }
 *          onAttach { }
 *          onDetach { }
 *          onRetryStart { binding-> }
 *          onRetryEnd { binding -> }
 *      }
 *      loadMoreItem {
 *          onCreate { position ->
 *              view.click { loadMore() }
 *              return view
 *          }
 *          onBind { position, data, view -> }
 *          onBindPayload { position, data, view, payloads -> }
 *          onRecycle { view -> }
 *          onItemAttach { view -> }
 *          onItemDetach { view -> }
 *          onAttach { }
 *          onDetach { }
 *          onLoadMoreStart { view -> }
 *          onLoadMoreEnd { view -> }
 *      }
 *      loadMoreBindingItem<ItemUserLoadMoreBinding> {
 *          onCreate { position ->
 *              binding.root.click { loadMore() }
 *              return binding
 *          }
 *          onBind { position, data, binding -> }
 *          onBindPayload { position, data, payload, binding-> -> }
 *          onRecycle { binding -> }
 *          onItemAttach { binding -> }
 *          onItemDetach { binding -> }
 *          onAttach { }
 *          onDetach { }
 *          onLoadMoreStart { binding-> }
 *          onLoadMoreEnd { binding -> }
 *      }
 *      refreshItem {
 *          onCreate { position ->
 *              view.click { refresh() }
 *              return view
 *          }
 *          onBind { position, data, view -> }
 *          onBindPayload { position, data, view, payloads -> }
 *          onRecycle { view -> }
 *          onItemAttach { view -> }
 *          onItemDetach { view -> }
 *          onAttach { }
 *          onDetach { }
 *          onRefreshStart { view -> }
 *          onRefreshEnd { view -> }
 *      }
 *      refreshBindingItem<ItemUserRefreshBinding> {
 *          onCreate { position ->
 *              binding.root.click { loadMore() }
 *              return binding
 *          }
 *          onBind { position, data, binding -> }
 *          onBindPayload { position, data, payload, binding-> -> }
 *          onRecycle { binding -> }
 *          onItemAttach { binding -> }
 *          onItemDetach { binding -> }
 *          onAttach { }
 *          onDetach { }
 *          onRefreshStart { binding-> }
 *          onRefreshEnd { binding -> }
 *      }
 * }
 *
 * object ApiService {
 *      suspend fun getData():<List<User>>
 * }
 * class DataSource:EDataSource() {
 *     abstract fun OnSuccess(data : List<EItem>) {}
 *
 *     override suspend fun onRequest () {
 *          try{
 *              val result = ApiService.getData()
 *              if(result.isEmpty()) {
 *                  onEmpty()
 *                  return
 *              }
 *              onSuccess(result)
 *          }catch(e : Exception){
 *              onError(e)
 *          }
 *     }
 * }
 *
 * class LoadableDataSource: ELoadableDataSource(){
 *     abstract fun OnSuccess(data : List<EItem>) {}
 *
 *     override suspend fun onRequest (index : Long, isRefresh : Boolean) {
 *          try{
 *              val result = ApiService.getData(index)
 *              if(result.isEmpty()) {
 *                  onEmpty()
 *                  return
 *              }
 *              onSuccess(result)
 *          }catch(e : Exception){
 *              onError()
 *          }
 *     }
 * }
 */
