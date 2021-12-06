package com.ricky.eqlist.adapter

import androidx.recyclerview.widget.RecyclerView

/**
 * 如果使用的是自定义的LayoutManager,请继承此接口并重写相应方法
 * @author Ricky Hal
 * @date 2021/12/1
 */
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