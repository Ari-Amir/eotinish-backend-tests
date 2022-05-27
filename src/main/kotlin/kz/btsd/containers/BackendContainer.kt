package kz.btsd.containers

import kz.btsd.helpers.ConfigHelper
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.wait.strategy.WaitAllStrategy
import java.time.Duration

class BackendContainer(network: Network, postfix: String, image: String = ConfigHelper.IMAGES_BACKEND) : iContainer,
    GenericContainer<BackendContainer>(image) {
    init {
        withNetwork(network)
        withNetworkAliases("sb-backend")
        withCreateContainerCmdModifier {
            it.withName("sb-backend-$postfix")
            it.withHostName("sb-backend")
        }
        withExposedPorts(ConfigHelper.BACKEND_PORT)
        withEnv("ACTIVE_PROFILE", "local")
        withEnv("POSTGRESQL_HOST", "${ConfigHelper.DB_HOST}")
        withEnv("POSTGRESQL_PORT", "${ConfigHelper.DB_PORT}")
        withEnv("POSTGRESQL_USER", "${ConfigHelper.DB_USER}")
        withEnv("POSTGRESQL_PASSWORD", "${ConfigHelper.DB_PASS}")
        withEnv("S3_ACCESS_KEY", "")
        withEnv("S3_SECRET_KEY", "")
        withEnv("S3_ENDPOINT", "")
        withEnv("JWT_SECRET_KEY", "")
        withEnv("JWT_EXTERNAL_SECRET_KEY", "")
        withEnv("GMAIL_SERVICE_FROM", "1")
        withEnv("GMAIL_CLIENT_ID", "1")
        withEnv("GMAIL_CLIENT_SECRET", "1")
        withEnv("GMAIL_ACCESS_TOKEN", "1")
        withEnv("GMAIL_REFRESH_TOKEN", "1")
        withEnv("SCHEDULING_ENABLE", "true")
        withEnv("RABBIT_USERNAME", "1")
        withEnv("RABBIT_PASSWORD", "1")
        waitingFor(WaitAllStrategy().withStrategy(Wait.forLogMessage(".*Started LifeCasesApplicationKt in .*\\s", 1)).withStartupTimeout(Duration.ofSeconds(100)))
    }

    override fun getPort(): Int {
        return this.getMappedPort(ConfigHelper.BACKEND_PORT)
    }
}
