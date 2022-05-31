package dev.alexengrig.sample.dbsium.repository;

import dev.alexengrig.sample.dbsium.entity.ThingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThingRepository extends JpaRepository<ThingEntity, String> {
}
