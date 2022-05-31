package dev.alexengrig.sample.dbsium.event;

import io.debezium.data.Envelope.Operation;
import io.debezium.engine.RecordChangeEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.debezium.data.Envelope.FieldName.AFTER;
import static io.debezium.data.Envelope.FieldName.BEFORE;
import static io.debezium.data.Envelope.FieldName.OPERATION;

@RequiredArgsConstructor
public class DebeziumEventHandler {

    private final DebeziumEventPayloadHandler payloadHandler;

    public void handleEvent(RecordChangeEvent<SourceRecord> event) {
        SourceRecord record = event.record();
        Struct struct = (Struct) record.value();
        if (struct != null) {
            Operation operation = Operation.forCode((String) struct.get(OPERATION));
            if (operation != Operation.READ) {
                String name = operation == Operation.DELETE ? BEFORE : AFTER;
                Struct value = (Struct) struct.get(name);
                Map<String, Object> payload = value.schema().fields()
                        .stream()
                        .map(Field::name)
                        .filter(fieldName -> value.get(fieldName) != null)
                        .collect(Collectors.toMap(Function.identity(), value::get));
                if (operation == Operation.DELETE) {
                    payloadHandler.handleDeletedPayload(payload);
                } else {
                    payloadHandler.handleSavedPayload(payload);
                }
            }
        }
    }
}
