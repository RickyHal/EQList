package com.ricky.eqlist.datasource

/**
 *
 * @author Ricky Hal
 * @date 2021/11/19
 */
interface IDiff {
    fun areItemsTheSame(other: IDiff): Boolean {
        return this == other
    }

    fun areContentsTheSame(other: IDiff): Boolean {
        return this == other
    }
}