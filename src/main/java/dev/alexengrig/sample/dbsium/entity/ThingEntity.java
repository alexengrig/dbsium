package dev.alexengrig.sample.dbsium.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "thing")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThingEntity {
    @Id
    private String id;
    private String name;
}
