package kz.btsd.helpers

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

object HttpSender {
    val okHttpClient = OkHttpClient()

    fun sendPost(
        endpoint: String,
        payload: String
    ): Response {
        val request = Request.Builder()
            .header("Content-Type", " application/json")
            .url("https://${ConfigHelper.BACKEND_HOST}$endpoint")
            .method("POST", payload.toRequestBody())
            .build()

        return okHttpClient.newCall(request).execute()
    }
}