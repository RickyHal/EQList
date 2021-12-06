package com.ricky.eqlist.item

/**
 * @author Ricky Hal
 * @date 2021/11/18
 */
abstract class BaseItem {
    internal open var isFullSpan: Boolean = false

    open fun withFullSpan(isFullSpan: Boolean) {
        this.isFullSpan = isFullSpan
    }
}