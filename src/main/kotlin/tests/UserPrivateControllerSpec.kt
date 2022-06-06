package tests

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import kz.btsd.helpers.HttpSender
import okhttp3.Response
import org.json.JSONObject

class UserPrivateControllerSpec : FeatureSpec({

    feature("user-private-controller") {

        scenario("GET /api/private/v1/users/me Возвращает текущего пользователя") {
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

            val authResponseBody = JSONObject(authResponse.body?.string())
            val accessToken = authResponseBody.getString("accessToken")

            val getMeResponse: Response

            try {
                getMeResponse = HttpSender.sendGetWithAccessToken("/api/private/v1/users/me", accessToken)
            } catch (e: Exception) {
                throw AssertionError("Exception occurred during sending request: ${e.cause}")
            }

            val body = JSONObject(getMeResponse.body?.string())
            val login = body.getString("login")

            login shouldBe Environment.user
        }
    }
}
)


