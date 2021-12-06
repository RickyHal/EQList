package com.ricky.eqlist.item

import android.view.View

/**
 * @author Ricky Hal
 * @date 2021/11/18
 */
open class BindingItem<Entity, Binding> : BaseItem() {
    private lateinit var onCreateBlock: (view: View) -> Binding
    private var onBindBlock: (position: Int, data: Entity, binding: Binding) -> Unit = { _, _, _ -> }
    private var onBindPayloadBlock: (position: Int, data: Entity, binding: Binding, payloads: MutableList<Any>) -> Unit = { _, _, _, _ -> }
    private var onRecycleBlock: (binding: Binding) -> Unit = { _ -> }
    private var onItemAttachBlock: (binding: Binding) -> Unit = { _ -> }
    private var onItemDetachBlock: (binding: Binding) -> Unit = { _ -> }

    fun onCreate(block: (view: View) -> Binding) {
        onCreateBlock = block
    }

    fun onBind(block: (position: Int, data: Entity, binding: Binding) -> Unit) {
        onBindBlock = block
    }

    fun onBindPayload(block: (position: Int, data: Entity, binding: Binding, payloads: MutableList<Any>) -> Unit) {
        onBindPayloadBlock = block
    }

    fun onRecycle(block: (binding: Binding) -> Unit) {
        onRecycleBlock = block
    }

    fun onItemAttach(block: (binding: Binding) -> Unit) {
        onItemAttachBlock = block
    }

    fun onItemDetach(block: (binding: Binding) -> Unit) {
        onItemDetachBlock = block
    }

    internal fun invokeOnCreate(view: View): Binding {
        if (!::onCreateBlock.isInitialized) {
            throw IllegalStateException("You must define the onCreate block when use bindingItem.")
        }
        return onCreateBlock.invoke(view)
    }

    internal fun invokeOnBind(position: Int, data: Any, binding: Any) {
        onBindBlock.invoke(position, data as Entity, binding as Binding)
    }

    internal fun invokeOnBindPayload(position: Int, data: Any, payloads: MutableList<Any>, binding: Any) {
        onBindPayloadBlock.invoke(position, data as Entity, binding as Binding, payloads)
    }

    internal fun invokeOnRecycle(binding: Any) {
        onRecycleBlock.invoke(binding as Binding)
    }

    internal fun invokeOnItemAttach(binding: Any) {
        onItemAttachBlock.invoke(binding as Binding)
    }

    internal fun invokeOnItemDetach(binding: Any) {
        onItemDetachBlock.invoke(binding as Binding)
    }
}