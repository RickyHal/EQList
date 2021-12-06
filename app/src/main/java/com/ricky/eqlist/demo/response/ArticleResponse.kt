package com.ricky.eqlist.demo.response

import com.google.gson.annotations.SerializedName
import com.ricky.eqlist.demo.entity.Article
import java.io.Serializable

/**
 *
 * @author Ricky Hal
 * @date 2021/11/23
 */
class ArticleResponse : Serializable {
    @SerializedName("curPage")
    var curPage: Int = 0

    @SerializedName("size")
    var size: Int = 20

    @SerializedName("datas")
    var datas: List<Article> = mutableListOf()
}