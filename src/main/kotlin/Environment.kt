import kz.btsd.helpers.DBConnectionPool
import kz.btsd.helpers.PostgreUtils
import org.testcontainers.containers.Network
import java.io.File
import java.nio.file.Paths
import java.util.*

object Environment {
    val currentDir = Paths.get("").toAbsolutePath().toString()

    val user = "110000000005"
    val password = "110000000005Aa!"

    private val uniquePostfix = System.currentTimeMillis().toString()
    val network: Network = Network.builder()
        .enableIpv6(false)
        .id(uniquePostfix)
        .build()

//    val pgContainer = TestPostgresContainer(network, uniquePostfix)
//    val backendContainer = BackendContainer(network, uniquePostfix)

//    private val containers = mutableListOf(pgContainer, backendContainer)

    var pool: DBConnectionPool? = null

    fun start() {
        try {
//            pgContainer.start()
//            TODO: Это старт контейрета с бекендом бекофиса. Этот запуск нужно отлаживать
//            backendContainer.start()

//            val dbPort = pgContainer.getPort().toString()
            val dbPort = "8888"
            pool = DBConnectionPool(dbPort)
//            val initSqlPath = "$currentDir/src/main/resources/init.sql"

//            pool = DBConnectionPool(dbPort)
//            val resource = pool!!.getResource()
////              TODO: Необходимо попросить разработчиков добавить в код бекенда возможность конфигурировать db host через env var POSTGRESQL_HOST, если ACTIVE_PROFILE = local.
//            PostgreUtils(resource).initializeDB(sqlString)
//            pool!!.releaseResource(resource)
        } catch (e: Exception) {
            stop()
            throw e
        }
    }

    fun stop() {
//        containers.reversed().filter { it.isCreated() }.forEach { it.stop() }
        network.close()
    }
}