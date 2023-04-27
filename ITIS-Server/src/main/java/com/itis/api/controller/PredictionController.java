package com.itis.api.controller;

import com.itis.api.service.PredictionService;
import com.itis.api.entity.Prediction;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/itis")
@Log4j2
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @PostMapping(value = "/predict/{pratientId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HashMap> predict(@PathVariable String pratientId ,@RequestParam("file") MultipartFile file){
        log.info("Controller predict method called");
        Map<String , String> res = new HashMap<String, String>();
        res.put("dis" , predictionService.predict(pratientId , file));

        return new ResponseEntity<HashMap>((HashMap) res,HttpStatus.OK);
    }


    @GetMapping ("/predictions/trend")
    public List<Prediction> getLastSevenDaysPredictions(){
        List<Prediction> list = predictionService.getLastSevenDaysPredictions();
        return list;
    }


    @GetMapping("/predictions/report")
    public Map<String, Long> getCountByPrediction() {
        return predictionService.getCountByPrediction();
    }

    @DeleteMapping("/predictions/{predictionId}")
    public ResponseEntity<Void> deleteByPredictionId(@PathVariable String predictionId){
        predictionService.deleteByPredictionId(predictionId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/audits")
    public List<Prediction> getAllPrediction(){
        List<Prediction> list = predictionService.getAllPrediction();
        return list;
    }

    @GetMapping("/predictions/dateBased")
    public Map<Date, Long> getCountByLastSevenDates() {
        return predictionService.getCountByLastSevenDates();
    }

}

