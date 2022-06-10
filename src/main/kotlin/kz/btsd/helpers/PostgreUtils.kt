package kz.btsd.helpers

import okhttp3.Response
import org.springframework.jdbc.BadSqlGrammarException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowCallbackHandler

class PostgreUtils(private val jdbcTemplate: JdbcTemplate) {

    fun initializeDB(sqlString: String) {
        try {
            jdbcTemplate.update(sqlString)
        } catch (e: BadSqlGrammarException) {
            throw e
        }
    }

    fun initializeDB1(sqlString: String) {
        try {
            jdbcTemplate.query(sqlString) {
//                TODO: Получить ответ из БД с нужными данными

            }
        } catch (e: BadSqlGrammarException) {
            throw e
        }
    }

    fun initializeDB2(sqlString: String) : Int {
        try {
            return jdbcTemplate.queryForObject(sqlString, Int::class.java)
        } catch (e: BadSqlGrammarException) {
            throw e
        }
    }
}


