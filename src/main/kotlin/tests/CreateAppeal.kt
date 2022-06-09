package tests

import Environment.currentDir
import kz.btsd.helpers.DBConnectionPool
import kz.btsd.helpers.PostgreUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


    fun createAppeal() : String {
        val pool = Environment.pool

            val appealId = getRandomAppealId()
            val randomUUID = getRandomUUID()
            val fileName = "$currentDir/src/main/resources/init.sql"
            val sqlString = File(fileName).readText(Charsets.UTF_8)
                .replace("#ID_AA#", appealId)
                .replace("#APPLICANT_ID_AA#", randomUUID)
                .replace("#ID_AH#", randomUUID)
                .replace("#APPEAL_ID_AH#",appealId)
                .replace("#ID_A#", appealId)
                .replace("#REG_NUMBER_A#", "ЖТ-2052-${appealId.substring(6)}")
                .replace("#SID_A#", randomUUID)
                .replace("#ID_APPLICANTS#", randomUUID)

            val resource = pool!!.getResource()
            PostgreUtils(resource).initializeDB(sqlString)
            pool.releaseResource(resource)
        return appealId
    }

    private fun getRandomUUID() : String  = UUID.randomUUID().toString()

    private fun getRandomAppealId() : String {
        val calendar = Calendar.getInstance()
        val date = SimpleDateFormat("yyMMdd").format(calendar.time).replaceRange(0..1,"52")
        val random = (0..99999999).random().toString()
        return "$date$random"
    }
