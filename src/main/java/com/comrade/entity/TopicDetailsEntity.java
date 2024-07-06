package com.comrade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;

@Entity
@Table(name = "TBL_TOPIC_DETAILS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicDetailsEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicDetailsId;
    private Long topicId;
    private String description;
    private LocalTime date;
}
