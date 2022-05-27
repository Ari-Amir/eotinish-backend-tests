package kz.btsd.helpers

import org.springframework.jdbc.BadSqlGrammarException
import org.springframework.jdbc.core.JdbcTemplate

class PostgreUtils(private val jdbcTemplate: JdbcTemplate) {

    fun initializeDB(sqlString: String) {
        try {
            jdbcTemplate.update(sqlString)
        } catch (e: BadSqlGrammarException) {
            throw e
        }
    }


}

