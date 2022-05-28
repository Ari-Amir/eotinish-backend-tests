package tests

import Environment
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kz.btsd.helpers.ConfigHelper
import kz.btsd.helpers.HttpSender
import kz.btsd.matchers.correctResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject

class AuthPublicControllerSpec : FeatureSpec({

    feature("auth-public-controller") {

        scenario("POST /api/public/v1/login Пользователь может авторизоваться с помощью пары логин/пароль") {
            val response: Response

            val payload = """{
                |"username": "${Environment.user}",
                |"password": "${Environment.password}",
                |"confirmation2Fa": false,
                |"token2Fa": "string" }""".trimMargin()

            try {
                response = HttpSender.sendPost("/api/public/v1/login", payload)
            } catch (e: Exception) {
                throw AssertionError("Exception occurred during sending request: ${e.cause}")
            }

            val expectedResponse = Response.Builder()
                .code(200)
                .protocol(Protocol.HTTP_2)
                .request(Request.Builder()
                    .url("https://" + ConfigHelper.BACKEND_HOST)
                    .build())
                .message("OK")
                .body(ResponseBody.create(
                    "application/json".toMediaType(),
                    "(accessToken)*(refreshToken)"
                ))
                .build()

            response shouldBe correctResponse(expectedResponse, true)
        }

        scenario("POST /api/public/v1/refresh Пользователь может обновить свой accessToken, используя refreshToken") {
            val authResponse: Response

            val payload = """{
                |"username": "${Environment.user}",
                |"password": "${Environment.password}",
                |"confirmation2Fa": false,
                |"token2Fa": "string" }""".trimMargin()

            try {
                authResponse = HttpSender.sendPost("/api/public/v1/login", payload)
            } catch (e: Exception) {
                throw AssertionError("Exception occurred during sending request: ${e.cause}")
            }

            val body = JSONObject(authResponse.body?.string())
            val accessToken = body.getString("accessToken")
            val refreshToken = body.getString("refreshToken")

            val refreshResponse: Response

            try {
                refreshResponse = HttpSender.sendPost("/api/public/v1/refresh", "{ \"refreshToken\": \"$refreshToken\" }")
            } catch (e: Exception) {
                throw AssertionError("Exception occurred during sending request: ${e.cause}")
            }

            val newAccessToken = JSONObject(refreshResponse.body?.string()).getString("accessToken")

            accessToken shouldNotBe newAccessToken
        }
    }

})