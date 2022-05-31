package dev.alexengrig.sample.dbsium.config.prop;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConfigurationProperties("debezium")
@RequiredArgsConstructor(onConstructor_ = @ConstructorBinding)
public class DebeziumProperties {

    private final DatabaseProperties database;

    @Getter
    @RequiredArgsConstructor(onConstructor_ = @ConstructorBinding)
    public static class DatabaseProperties {
        private final String hostname;
        private final String port;
        private final String user;
        private final String password;
        private final String name;
        private final String table;

        public String getServerName() {
            return hostname + "-" + name;
        }
    }

}
