package com.ricky.eqlist.demo.entity

import com.google.gson.annotations.SerializedName
import com.ricky.eqlist.datasource.IDiff
import com.ricky.eqlist.entity.BaseEntity

/**
 *
 * @author Ricky Hal
 * @date 2021/11/23
 */
data class Article(
    @SerializedName("id")
    var id: Long,
    @SerializedName("title")
    var title: String,
    @SerializedName("shareUser")
    var author: String
) : BaseEntity() {
    override fun areContentsTheSame(other: IDiff): Boolean {
        if (other !is Article) return false
        return this.id == other.id
    }

    override fun areItemsTheSame(other: IDiff): Boolean {
        if (other !is Article) return false
        return this.id == other.id
    }
}
