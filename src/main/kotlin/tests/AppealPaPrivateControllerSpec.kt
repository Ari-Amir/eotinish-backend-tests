package tests

import io.kotest.core.spec.style.FeatureSpec
import kz.btsd.helpers.HttpSender
import okhttp3.Response

class AppealPaPrivateControllerSpec : FeatureSpec({

    feature("appeal-pa-private-controller") {

        scenario("POST /api/private/v1/pa/appeals Пользователь может создать обращение").config(enabled = true) {
            val response: Response

            val payload = """{
                        "newAppeal": {
                            "applicant": {
                                "iinbin": "123123123123",
                                "applicantType": "PERSON",
                                "personResident": null,
                                "personFirstName": "AUTOTEST",
                                "personSecondName": "AUTOTEST",
                                "personMiddleName": null,
                                "personBirthDate": null,
                                "personGenderId": null,
                                "personCitizenshipId": null,
                                "personNationalityId": null,
                                "personSocialStatusId": null,
                                "personOccupationId": null,
                                "personFactAddress": "TEST ADDRESS",
                                "personLawAddress": null,
                                "companyName": null,
                                "companyRegDate": null,
                                "companyLawAddress": null,
                                "companyFactAddress": null,
                                "companyHeadFirstName": null,
                                "companyHeadSecondName": null,
                                "companyHeadMiddleName": null,
                                "companyBusinessCategory": null,
                                "companyAppealOutNumber": null,
                                "applicantsCount": null,
                                "attorney": null,
                                "mobilePhone": null,
                                "workHomePhone": null,
                                "email": null
                            },
                            "typeId": "4b288b14-2a1c-4ef1-988f-adcbf3839983",
                            "issueSubCategoryId": null,
                            "issueCategoryId": "26bc0c62-0bc4-4d2f-9f81-364956fccced",
                            "locationId": "35",
                            "organizationId": 932,
                            "data": "TEST DESCRIPTION",
                            "files": [
                                {
                                    "fileName": "test.pdf",
                                    "fileId": "4b2989cb-4336-4cd7-871f-2608fcb653a5",
                                    "mimeType": "application/pdf",
                                    "description": "TEST FILE",
                                    "thumbnailFileId": null,
                                    "displayFileId": null,
                                    "hash": "69906322f1b94210df688c22a1ee00543b3c613ddb411cc76b684dee16dd4846"
                                }
                            ],
                            "responseGetWay": "PCS",
                            "languageId": "14aafdaf-fb58-41d9-987a-8c33eba3d1a4",
                            "checkedByController": false,
                            "appealForm": "PAPER_ON_PURPOSE",
                            "appealCharacter": "INDIVIDUAL",
                            "complaintTypeId": null,
                            "complaintAppealId": null,
                            "complaintLocationId": null,
                            "complaintOrganizationId": null,
                            "complaintExecutiveUserId": null,
                            "complaintExecutiveName": null,
                            "personAge": null,
                            "anotherOne": false
                        },
                        "signature": "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"
                    }""".trimMargin()

            try {
                response = HttpSender.sendPostWithAccessToken("/api/private/v1/pa/appeals", HttpSender.getAccessToken(), payload)
            } catch (e: Exception) {
                throw AssertionError("Exception occurred during sending request: ${e.cause}")
            }

        }

        scenario("POST /api/private/v1/pa/appeals/executors Пользователь может назначить исполнителя по обращению").config(enabled = true) {
            val response: Response
            val appealId = createAppeal()

            val payload = """{
                    "appealId": "$appealId",
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

