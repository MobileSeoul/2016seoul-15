package kr.edcan.buspolis.util

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams

/**
 * Created by LNTCS on 2016-01-26.
 */

object HttpClient {
    val BASE_URL = "http://ws.bus.go.kr/api/rest"

    private val client = AsyncHttpClient()

    operator fun get(url: String, params: RequestParams, responseHandler: AsyncHttpResponseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler)
    }

    operator fun get(base: String, url: String, params: RequestParams, responseHandler: AsyncHttpResponseHandler) {
        client.get(base + url, params, responseHandler)
    }

    fun post(url: String, params: RequestParams, responseHandler: AsyncHttpResponseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler)
    }

    fun put(url: String, params: RequestParams, responseHandler: AsyncHttpResponseHandler) {
        client.put(getAbsoluteUrl(url), params, responseHandler)
    }

    private fun getAbsoluteUrl(relativeUrl: String): String {
        return BASE_URL + relativeUrl
    }
}
