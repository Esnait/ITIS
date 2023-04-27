package com.itis.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "prediction")
public class Prediction {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "prediction_id", length = 36)
    private String predictionId;

    @Column(name = "date")
    private Instant date;

    @Column(name = "patient_id", length = 36)
    private String patientId;

    @Column(name = "prediction", length = 255)
    private String prediction;
}

