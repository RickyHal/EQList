package com.ricky.eqlist.item

import android.view.View

/**
 *
 * @author Ricky Hal
 * @date 2021/11/18
 */

open class Item<Entity> : BaseItem() {
    private var onCreateBlock: (view: View) -> Unit = {}
    private var onBindBlock: (position: Int, data: Entity, view: View) -> Unit = { _, _, _ -> }
    private var onBindPayloadBlock: (position: Int, data: Entity, view: View, payloads: MutableList<Any>) -> Unit = { _, _, _, _ -> }
    private var onRecycleBlock: (view: View) -> Unit = { _ -> }
    private var onItemAttachBlock: (view: View) -> Unit = { _ -> }
    private var onItemDetachBlock: (view: View) -> Unit = { _ -> }

    fun onCreate(block: (view: View) -> Unit) {
        onCreateBlock = block
    }

    fun onBind(block: (position: Int, data: Entity, view: View) -> Unit) {
        onBindBlock = block
    }

    fun onBindPayload(block: (position: Int, data: Entity, view: View, payloads: MutableList<Any>) -> Unit) {
        onBindPayloadBlock = block
    }

    fun onRecycle(block: (view: View) -> Unit) {
        onRecycleBlock = block
    }

    fun onItemAttach(block: (view: View) -> Unit) {
        onItemAttachBlock = block
    }

    fun onItemDetach(block: (view: View) -> Unit) {
        onItemDetachBlock = block
    }

    internal fun invokeOnCreate(view: View) {
        onCreateBlock.invoke(view)
    }

    internal fun invokeOnBind(position: Int, data: Any, view: View) {
        onBindBlock.invoke(position, data as Entity, view)
    }

    internal fun invokeOnBindPayload(position: Int, data: Any, view: View, payloads: MutableList<Any>) {
        onBindPayloadBlock.invoke(position, data as Entity, view, payloads)
    }

    internal fun invokeOnRecycle(view: View) {
        onRecycleBlock.invoke(view)
    }

    internal fun invokeOnItemAttach(view: View) {
        onItemAttachBlock.invoke(view)
    }

    internal fun invokeOnItemDetach(view: View) {
        onItemDetachBlock.invoke(view)
    }
}