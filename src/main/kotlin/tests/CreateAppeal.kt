package tests

import Environment.currentDir
import kz.btsd.helpers.PostgreUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


fun createAppeal(): String {
    val pool = Environment.pool

    val appealId = getAppealId()
    val randomUUID = getRandomUUID()
    val fileName = "$currentDir/src/main/resources/init.sql"
    val sqlString = File(fileName).readText(Charsets.UTF_8)
        .replace("#ID_AA#", appealId)
        .replace("#APPLICANT_ID_AA#", randomUUID)
        .replace("#ID_AH#", randomUUID)
        .replace("#APPEA_ID_AH#", appealId)
        .replace("#ID_A#", appealId)
        .replace("#REG_NUMBER_A#", "ЖТ-2052-${appealId.substring(6)}")
        .replace("#SID_A#", randomUUID)
        .replace("#ID_APPLICANTS#", randomUUID)

    val resource = pool!!.getResource()
    PostgreUtils(resource).initializeDB(sqlString)
    pool.releaseResource(resource)
    return appealId
}

private fun getRandomUUID(): String = UUID.randomUUID().toString()

private fun getAppealId(): String {
    val calendar = Calendar.getInstance()
    val date = SimpleDateFormat("yyMMdd").format(calendar.time).replaceRange(0..1, "52")
    val counter = getAppealCounter()
    return "$date$counter"
}

private fun getAppealCounter(): String {
    val pool = Environment.pool
    val resource = pool!!.getResource()
    val counter = PostgreUtils(resource).initializeDB2("select nextval('registration_number_seq')")
    PostgreUtils(resource).initializeDB2("select nextval('appeal_serial_number_seq')")
    pool.releaseResource(resource)
    return counter.toString().padStart(8, '0')
}
