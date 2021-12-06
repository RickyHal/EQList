package com.ricky.eqlist.item

import com.ricky.eqlist.entity.StateEntity

/**
 * @author Ricky Hal
 * @date 2021/11/18
 */
class StateBindingItem<Binding> : BindingItem<StateEntity, Binding>() {
    private var onRetryBlock: () -> Unit = {}
    override var isFullSpan: Boolean = true
    override fun withFullSpan(isFullSpan: Boolean) {}

    fun retry() {
        onRetryBlock.invoke()
    }

    fun onRetry(block: () -> Unit) {
        onRetryBlock = block
    }
}