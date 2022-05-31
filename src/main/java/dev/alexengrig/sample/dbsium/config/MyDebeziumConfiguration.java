package dev.alexengrig.sample.dbsium.config;

import dev.alexengrig.sample.dbsium.config.prop.DebeziumProperties;
import dev.alexengrig.sample.dbsium.event.DebeziumEventHandler;
import dev.alexengrig.sample.dbsium.event.DebeziumEventPayloadHandler;
import io.debezium.config.Configuration;
import io.debezium.connector.postgresql.PostgresConnector;
import io.debezium.connector.postgresql.PostgresConnectorConfig;
import io.debezium.embedded.Connect;
import io.debezium.embedded.EmbeddedEngine;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import io.debezium.relational.RelationalDatabaseConnectorConfig;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.storage.FileOffsetBackingStore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@EnableConfigurationProperties(DebeziumProperties.class)
@org.springframework.context.annotation.Configuration
public class MyDebeziumConfiguration {

    private final DebeziumProperties properties;
    private final DebeziumEventPayloadHandler payloadHandler;

    @Bean
    public Configuration debeziumConfiguration() {
        return Configuration.create()
                .with("name", "dbsium-postgres-connector")
                .with(EmbeddedEngine.CONNECTOR_CLASS, PostgresConnector.class)
                .with(EmbeddedEngine.OFFSET_STORAGE, FileOffsetBackingStore.class)
                .with(EmbeddedEngine.OFFSET_STORAGE_FILE_FILENAME, "offset.dat")
                .with(EmbeddedEngine.OFFSET_FLUSH_INTERVAL_MS, 60000)
                .with(RelationalDatabaseConnectorConfig.SERVER_NAME, properties.getDatabase().getServerName())
                .with(RelationalDatabaseConnectorConfig.HOSTNAME, properties.getDatabase().getHostname())
                .with(RelationalDatabaseConnectorConfig.PORT, properties.getDatabase().getPort())
                .with(RelationalDatabaseConnectorConfig.USER, properties.getDatabase().getUser())
                .with(RelationalDatabaseConnectorConfig.PASSWORD, properties.getDatabase().getPassword())
                .with(RelationalDatabaseConnectorConfig.DATABASE_NAME, properties.getDatabase().getName())
                .with(RelationalDatabaseConnectorConfig.TABLE_INCLUDE_LIST, properties.getDatabase().getTable())
                .with(PostgresConnectorConfig.PLUGIN_NAME, PostgresConnectorConfig.LogicalDecoder.PGOUTPUT)
                .build();
    }

    @Bean
    public DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine(Configuration configuration) {
        DebeziumEventHandler eventHandler = debeziumEventHandler();
        return DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(configuration.asProperties())
                .notifying(eventHandler::handleEvent)
                .build();
    }

    @Bean
    public DebeziumEventHandler debeziumEventHandler() {
        return new DebeziumEventHandler(payloadHandler);
    }
}
