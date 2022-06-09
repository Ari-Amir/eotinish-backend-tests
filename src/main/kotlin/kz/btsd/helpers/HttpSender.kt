package kz.btsd.helpers

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject

object HttpSender {
    private val okHttpClient = OkHttpClient()
    fun sendPost(endpoint: String, payload: String): Response {
        val request = Request.Builder()
            .header("Content-Type", "application/json")
            .url("https://${ConfigHelper.BACKEND_HOST}$endpoint")
            .method("POST", payload.toRequestBody())
            .build()

        return okHttpClient.newCall(request).execute()
    }

    fun sendGet(endpoint: String): Response {
        val request = Request.Builder()
            .url("https://${ConfigHelper.BACKEND_HOST}$endpoint")
            .method("GET", null)
            .build()

        return okHttpClient.newCall(request).execute()
    }

    fun sendGetWithAccessToken(endpoint: String, accessToken: String) : Response {
        val request = Request.Builder()
            .header("Authorization", "Bearer $accessToken")
            .url("https://${ConfigHelper.BACKEND_HOST}$endpoint")
            .method("GET", body = null)
            .build()

        return okHttpClient.newCall(request).execute()
    }

    fun sendPostWithAccessToken(endpoint: String, accessToken: String, payload: String) : Response {
       val request = Request.Builder()
           .url("https://${ConfigHelper.BACKEND_HOST}$endpoint")
           .method("POST", payload.toRequestBody())
           .header("Content-Type", " application/json")
           .header("Authorization", "Bearer $accessToken")
           .header("Referer", "https://backoffice.stg.eotinish.btsdapps.net/")
           .header("Host","backoffice.stg.eotinish.btsdapps.net")
           .build()
        return okHttpClient.newCall(request).execute()
    }

    fun getAccessToken(): String {
        val response: Response
        val payload = """{
                |"username": "${Environment.user}",
                |"password": "${Environment.password}",
                |"confirmation2Fa": false,
                |"token2Fa": "string" }""".trimMargin()

        response = sendPost("/api/public/v1/login", payload)

        val body = JSONObject(response.body?.string())
        return body.getString("accessToken")
    }
}

