package com.repsy.fullstackdevassignment.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "packages", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "version"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String version;

    private String author;

    @Type(JsonBinaryType.class)
    @Column(name = "meta_json", columnDefinition = "jsonb")
    private String metaJson;

    @Column(name = "uploadedAt", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime uploadedAt;
}
