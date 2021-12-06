package com.ricky.eqlist.demo.loadable

import com.ricky.eqlist.demo.response.ArticleResponse
import com.ricky.eqlist.demo.response.HttpResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *
 * @author Ricky Hal
 * @date 2021/11/23
 */
private const val BASE_URL = "https://www.wanandroid.com/"

interface ApiService {
    @GET("article/list/{cursor}/json")
    suspend fun getArticle(@Path("cursor") cursor: Int, @Query("page_size") pageSize: Int): HttpResult<ArticleResponse>

    companion object {
        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}