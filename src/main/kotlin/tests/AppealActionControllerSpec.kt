package tests

import io.kotest.core.spec.style.FeatureSpec
import kz.btsd.helpers.HttpSender

class AppealActionControllerSpec : FeatureSpec({
    feature("appeal-action-controller")
    {
        scenario("POST /api/private/v1/pa/appeals/actions/revoke Пользователь может отозвать обращение") {
            //Авторизоваться
            val accessToken: String
            try {
                accessToken = HttpSender.getAccessToken()
            } catch (e: Exception) {
                throw AssertionError("Exception occurred during sending request: ${e.cause}")
            }

            //Получить данные по обращению
            val payload = """{
                  "appealId": "string",
                  "files": [
                    {
                      "description": "string",
                      "displayFileId": "string",
                      "fileId": "string",
                      "fileName": "string",
                      "hash": "string",
                      "mimeType": "string",
                      "thumbnailFileId": "string"
                    }
                  ],
                  "message": "string",
                  "sign": "string"
                }"""
            HttpSender.sendPostWithAccessToken(
                "/api/private/v1/pa/appeals/actions/revoke",
                accessToken,
                payload
            )
            //Добавить новый метод POST запроса с файлом
            //Сделать запрос в эндпоиннт
            //Сравнить полученный ответ с ожидаемым
            //Проверить в базе данных что обращение в нужном статусе


        }
    }
})