package kz.btsd.containers

import kz.btsd.helpers.ConfigHelper
import org.testcontainers.containers.Network
import org.testcontainers.containers.PostgreSQLContainer

class SpecifiedPostgreSQLContainer() : PostgreSQLContainer<SpecifiedPostgreSQLContainer>()

class TestPostgresContainer(
        val network: Network,
        val postfix: String,
        private var isCreated: Boolean = false,
        private var pgContainer: SpecifiedPostgreSQLContainer? = null
) : iContainer {

    override fun isCreated(): Boolean {
        return isCreated
    }

    override fun start() {
        pgContainer = SpecifiedPostgreSQLContainer()
                .withDatabaseName(ConfigHelper.DB_NAME)
                .withNetwork(network)
                .withNetworkAliases("pg-db")
                .withCreateContainerCmdModifier {
                    it.withName("pg-db-$postfix")
                    it.withHostName("pg-db")
                }
        pgContainer!!.start()
        isCreated = true
    }

    override fun stop() {
        pgContainer!!.stop()
        isCreated = false
    }

    override fun getPort(): Int {
        return pgContainer!!.getMappedPort(ConfigHelper.DB_PORT.toInt())
    }
}
