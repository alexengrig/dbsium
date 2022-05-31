package dev.alexengrig.sample.dbsium.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alexengrig.sample.dbsium.entity.ThingEntity;
import dev.alexengrig.sample.dbsium.repository.ThingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ThingDebeziumEventPayloadHandler implements DebeziumEventPayloadHandler {
    private final ObjectMapper objectMapper;
    private final ThingRepository repository;

    @Override
    public void handleDeletedPayload(Map<String, Object> payload) {
        ThingEntity entity = objectMapper.convertValue(payload, ThingEntity.class);
        repository.deleteById(entity.getId());
    }

    @Override
    public void handleSavedPayload(Map<String, Object> payload) {
        ThingEntity entity = objectMapper.convertValue(payload, ThingEntity.class);
        repository.save(entity);
    }
}
