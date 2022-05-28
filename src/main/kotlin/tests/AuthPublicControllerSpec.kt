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

        scenario("GET /api/public/v1/login/eds/token " +
                "POST /api/public/v1/login/eds Пользователь может авторизоваться с помощью ЭЦП").config(enabled = false) {
            val tokenResponse: Response

            try {
                tokenResponse = HttpSender.sendGet("/api/public/v1/login/eds/token")
            } catch (e: Exception) {
                throw AssertionError("Exception occurred during sending request: ${e.cause}")
            }

            val body = JSONObject( tokenResponse.body?.string() )
            val sessionId = body.getString("sessionId")
            val token = body.getString("token")
//          TODO: нужно узнать у разработчиков, фронта как они генерят sign, используя token
            val sign = ""

            val authResponse: Response

            val payload = """{
                |"sessionId": "$sessionId",
                |"sign": "$sign" }""".trimMargin()

            try {
                authResponse = HttpSender.sendPost("/api/public/v1/login/eds", payload)
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

            authResponse shouldBe correctResponse(expectedResponse, true)
        }

        // TODO: ссылка на баг
        scenario("POST /api/public/v1/refresh Пользователь может обновить свой accessToken, используя refreshToken").config(enabled = false) {
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

            val newAccessToken = JSONObject( refreshResponse.body?.string() ).getString("accessToken")

            accessToken shouldNotBe newAccessToken
        }


    }



})