package dev.alexengrig.sample.dbsium.event;

import java.util.Map;

public interface DebeziumEventPayloadHandler {
    void handleDeletedPayload(Map<String, Object> payload);

    void handleSavedPayload(Map<String, Object> payload);
}
