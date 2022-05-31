package dev.alexengrig.sample.dbsium.worker;

import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class DebeziumWorker implements InitializingBean, DisposableBean {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> engine;

    private boolean started;

    @Override
    public void afterPropertiesSet() {
        if (!started) {
            executorService.execute(engine);
            started = true;
        }
    }

    @Override
    public void destroy() throws Exception {
        engine.close();
        executorService.shutdownNow();
    }
}
