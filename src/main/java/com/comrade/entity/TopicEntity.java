package com.comrade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalTime;

@Entity
@Table(name = "TBL_TOPIC")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicUpdate
public class TopicEntity implements Serializable, Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String category;
    private String title;
    private String shortDescription;
    private LocalTime createdDate;
    private String status;

    @Override
    public boolean isNew() {
        return this.getId() != null ? true: false;
    }
}
