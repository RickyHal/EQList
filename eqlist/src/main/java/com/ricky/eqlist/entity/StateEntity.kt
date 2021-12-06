package com.ricky.eqlist.entity

/**
 *
 * @author Ricky Hal
 * @date 2021/11/18
 */
data class StateEntity(val state: Int = STATE_HIDE) : BaseEntity() {
    companion object {
        const val STATE_HIDE = 0
        const val STATE_EMPTY = 1
        const val STATE_ERROR = 2
    }
}