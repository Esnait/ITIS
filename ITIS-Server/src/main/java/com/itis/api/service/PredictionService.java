package com.itis.api.service;

import com.itis.api.entity.Prediction;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PredictionService {

    List<Prediction> getLastSevenDaysPredictions();

    Map<String, Long> getCountByPrediction();

    void deleteByPredictionId(String predictionId) ;

    List<Prediction> getAllPrediction();

    String predict(String pratientId , MultipartFile file);

    Map<Date, Long> getCountByLastSevenDates();

}

