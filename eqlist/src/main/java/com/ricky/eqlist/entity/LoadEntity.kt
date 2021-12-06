package com.ricky.eqlist.entity

/**
 *
 * @author Ricky Hal
 * @date 2021/11/18
 */
data class LoadEntity(var state: LoadState) : BaseEntity()

/**
 *
 *              ---------->ERROR // 触发异常的情况下，也不走消失流程，但是可以继续加载
 *             |
 * SHOW----->START-------->SUCCESS---(delay)---->HIDE
 *             |
 *              ---------->EMPTY // 当没有更多数据时，不走消失流程，且不能继续加载
 */
enum class LoadState {
    STATE_SHOW,
    STATE_START,
    STATE_SUCCESS,
    STATE_ERROR,
    STATE_EMPTY,
    STATE_HIDE
}

fun LoadState.loadAble(): Boolean {
    // 当Loader处于隐藏状态或者刚刚显示出来还没开始加载的状态下/加载错误的状态下，可以触发加载更多
    return this == LoadState.STATE_HIDE || this == LoadState.STATE_SHOW || this == LoadState.STATE_ERROR
}

fun LoadState.isLoading(): Boolean {
    return this == LoadState.STATE_START
}