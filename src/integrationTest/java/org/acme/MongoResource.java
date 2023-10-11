package org.acme;

import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MongoResource implements QuarkusTestResourceLifecycleManager, DevServicesContext.ContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoResource.class);
    private static final String CONTAINER_IMAGE = "mongo:7.0.2";
    private static final int DB_PORT = 27017;
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "secret";

    private File storage;
    private Optional<String> containerNetworkId;
    private GenericContainer<?> container;

    @Override
    public void setIntegrationTestContext(DevServicesContext context) {
        containerNetworkId = context.containerNetworkId();
    }

    @Override
    public Map<String, String> start() {
        storage = new File(System.getProperty("java.io.tmpdir"), "mongo-" + UUID.randomUUID());
        File mongoStorage = new File(storage, "mongo");
        container = new GenericContainer<>(DockerImageName.parse(CONTAINER_IMAGE))
                .withFileSystemBind(mongoStorage.getAbsolutePath(), "/data/db", BindMode.READ_WRITE)
                .withExposedPorts(DB_PORT)
                .withEnv("MONGO_INITDB_ROOT_USERNAME", USERNAME)
                .withEnv("MONGO_INITDB_ROOT_PASSWORD", PASSWORD)
                .withEnv("MONGO_INITDB_DATABASE", "acme")
                .withLogConsumer(new Slf4jLogConsumer(LOGGER));
        containerNetworkId.ifPresent(container::withNetworkMode);
        container.start();

        String connectionUrl = String.format("mongodb://%s:%s@%s:%d", USERNAME, PASSWORD, container.getContainerName().substring(1), DB_PORT);
        return Map.of(
                "quarkus.mongodb.acme.connection-string", connectionUrl
        );
    }

    @Override
    public void stop() {
        container.stop();
    }
}
