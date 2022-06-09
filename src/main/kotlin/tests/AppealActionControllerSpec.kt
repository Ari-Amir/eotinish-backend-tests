package tests

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import kz.btsd.helpers.ConfigHelper
import kz.btsd.helpers.DBConnectionPool
import kz.btsd.helpers.HttpSender
import kz.btsd.helpers.PostgreUtils
import kz.btsd.matchers.correctResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody


class AppealActionControllerSpec : FeatureSpec({
    feature("appeal-action-controller")
    {
        scenario("POST /api/private/v1/pa/appeals/actions/revoke Пользователь может отозвать обращение") {
            //Авторизоваться
            val refreshToken: String
            try {
                refreshToken = HttpSender.getAccessToken()
            } catch (e: Exception) {
                throw AssertionError("Exception occurred during sending request: ${e.cause}")
            }

            //Создать обращение и получить его ID
            val appealId = createAppeal()

            //Получить данные по обращению
            val payload = """{
                                "message": "Обращение отозвано AUTOTEST'ом",
                                "files": [
                                    {
                                        "fileName": "test.pdf",
                                        "fileId": "81c15393-6b62-4c21-92c4-f944d449d40e",
                                        "mimeType": "application/pdf",
                                        "description": "Документ",
                                        "thumbnailFileId": null,
                                        "displayFileId": null,
                                        "hash": "69906322f1b94210df688c22a1ee00543b3c613ddb411cc76b684dee16dd4846"
                                    }
                                ],
                                "appealId": "$appealId",
                                "sign": "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"
                            }""".trimMargin()


            //Сделать запрос в эндпоиннт
            val revokeResponse: Response
            try {
                revokeResponse = HttpSender.sendPostWithAccessToken(
                    "/api/private/v1/pa/appeals/actions/revoke",
                    refreshToken,
                    payload
                )
            } catch (e: Exception) {
                throw AssertionError("Exception occurred during sending request: ${e.cause}")
            }

            //Сравнить полученный ответ с ожидаемым
            val expectedResponse = Response.Builder()
                .code(200)
                .protocol(Protocol.HTTP_2)
                .request(
                    Request.Builder()
                    .url("https://" + ConfigHelper.BACKEND_HOST)
                    .build())
                .message("OK")
                .body(
                    ResponseBody.create(
                    "application/json".toMediaType(),
                    "($appealId)*(FINISHED)" //уточнить
                ))
                .build()

            revokeResponse shouldBe correctResponse(expectedResponse, true)

            //Проверить в базе данных что обращение в нужном статусе
            val pool = Environment.pool
            val resource = pool!!.getResource()
            PostgreUtils(resource).initializeDB1("SELECT * FROM public.appeals WHERE id = '$appealId'")
            pool.releaseResource(resource)
        }
    }
})