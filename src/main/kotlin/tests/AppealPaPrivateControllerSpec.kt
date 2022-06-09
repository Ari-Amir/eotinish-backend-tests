package tests

import io.kotest.core.spec.style.FeatureSpec
import kz.btsd.helpers.HttpSender
import okhttp3.Response

class AppealPaPrivateControllerSpec : FeatureSpec({

    feature("appeal-pa-private-controller") {

        scenario("POST /api/private/v1/pa/appeals/executors Пользователь может назначить исполнителя по обращению") {
            val response: Response

            val payload = """{
                    "appealId": "${createAppeal()}",
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
                    "message": "В работу",
                    "organizationId": 932,
                    "executorId": "4689c39d-c959-4a78-9706-77b2b622884d",
                    "usersIds": []
                }""".trimMargin()

            try {
                response = HttpSender.sendPostWithAccessToken("/api/private/v1/pa/appeals/executors", HttpSender.getAccessToken(), payload)
            } catch (e: Exception) {
                throw AssertionError("Exception occurred during sending request: ${e.cause}")
            }
        }
    }
})

