package com.ricky.eqlist.demo.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *
 * @author Ricky Hal
 * @date 2021/11/23
 */
class HttpResult<Response> : Serializable {
    @SerializedName("data")
    var data: Response? = null

    @SerializedName("errorCode")
    var errorCode: Int = 0

    @SerializedName("errorMsg")
    var errorMsg: String = ""
}