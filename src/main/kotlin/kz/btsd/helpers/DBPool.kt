package kz.btsd.helpers

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

class DBConnectionPool : ObjectPool<JdbcTemplate> {
    private val DRIVER = "org.postgresql.Driver"
    private val USERS_COUNT = 2

    fun createDataSource(jdbcUrl: String): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(DRIVER)
        dataSource.url = jdbcUrl

        return dataSource
    }

    private var freeInstances: MutableList<JdbcTemplate>
    private var lockedInstances: MutableList<JdbcTemplate>

    @Synchronized
    override fun getResource(): JdbcTemplate {
        val resource = if (freeInstances.size != 0)
                            freeInstances.removeAt(freeInstances.size-1)
                        else
                            throw Exception("Threre is no free JdbcTemplate in a pool")

        lockedInstances.add(resource)

        return resource
    }

    @Synchronized
    override fun releaseResource(resource: JdbcTemplate) {
        freeInstances.add(resource)
        lockedInstances.remove(resource)
    }

    constructor(dbPort: String) {
        val pool = mutableListOf<JdbcTemplate>()

        repeat (USERS_COUNT) {
            pool.add(JdbcTemplate(createDataSource("jdbc:postgresql://${ConfigHelper.DB_HOST}:${dbPort}/${ConfigHelper.DB_NAME}?user=${ConfigHelper.DB_USER}&password=${ConfigHelper.DB_PASS}")))
        }

        this.freeInstances = pool
        this.lockedInstances = emptyList<JdbcTemplate>().toMutableList()
    }
}

interface ObjectPool<T> {
    fun getResource(): T
    fun releaseResource(resource: T)
}