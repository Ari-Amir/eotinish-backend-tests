package tests

import Environment
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.string.shouldNotBeEmpty
import kz.btsd.helpers.ConfigHelper
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class AuthPublicControllerSpec : FeatureSpec({

    feature("auth-public-controller") {
        scenario("POST /api/public/v1/login Пользователь может авторизоваться с помощью пары логин/пароль") {
            //послать юзернейм/пассворд и получить респонс

            val endPoint = "/api/public/v1/login"
            val payload = "{ \"username\": \"${Environment.user}\", \"password\": \"${Environment.password}\", \"confirmation2Fa\": false, \"token2Fa\": \"string\" }"
            var result: Response

            val okHttpClient = OkHttpClient()
            val requestBody = payload.toRequestBody()


            val request = Request.Builder()
                .header("Content-Type", " application/json")
                .url("https://${ConfigHelper.BACKEND_HOST}$endPoint")
                .method("POST", requestBody)
                .build()


//            okHttpClient.newCall(request).enqueue(object : Callback {
//                    override fun onFailure(call: Call, e: IOException) {
//                        throw AssertionError("Exception occurred during sending request: ${e.printStackTrace()}")
//                    }
//
//                    override fun onResponse(call: Call, response: Response) {
//                        result = response
//                    }
//                })


            val response = okHttpClient.newCall(request).execute()
            val jsonDataString = response.body?.string()
            val json = JSONObject(jsonDataString)

            if (!response.isSuccessful) {
                throw Exception(json.toString())
            }

            //Здесь просто показал выборочно три строки из ответа
            println("ACCESS TOKEN ${json.getString("accessToken")}")
            println("REFRESH TOKEN ${json.getString("refreshToken")}")
            println("ROLES ${json.getJSONArray("roles")}")


            //проверить что в респонсе есть рефреш и  аксес токен
            json.getString("accessToken").shouldNotBeEmpty()
        }
    }

})