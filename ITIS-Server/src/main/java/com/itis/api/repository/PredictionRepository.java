package com.itis.api.repository;

import com.itis.api.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, String> {

    List<Prediction> findAllByDateBetween(Instant startDate, Instant endDate);

    long countByPrediction(String prediction);

    @Query(value = "SELECT prediction, count(*) FROM prediction GROUP BY prediction", nativeQuery = true)
    List<Object[]> getCountByPrediction();

    Long countBydate(Instant date);


    @Query("SELECT p.date, COUNT(p) FROM Prediction p WHERE p.date BETWEEN :startDate AND :endDate GROUP BY p.date")
    List<Object[]> countByDateBetween(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

}


