package com.itis.api.service;

import com.itis.api.entity.Prediction;
import com.itis.api.exception.CustomException;
import com.itis.api.repository.PredictionRepository;
import jakarta.annotation.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.*;
import org.springframework.web.client.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Log4j2
public class PredictionServiceImpl implements PredictionService {

    @Autowired
    private PredictionRepository predictionRepository;
    @Value("${itis-capture.uri}")
    private String itisCaptureURI;
    private String itisCapturePredictionEndpoint = "/predict";

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @PostConstruct
    public void testItisCaptureConnection() {

        ResponseEntity<String> response = restTemplate.getForEntity(this.itisCaptureURI, String.class);
        if(response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            log.warn("Error occurred while connecting the ITIS-Capture server at : {}", this.itisCaptureURI);
        }
        else {
            log.info("Successfully connected to ITIS-Capture server at : {}", this.itisCaptureURI);
        }

    }

    @Override
    public String predict(String patientId , MultipartFile file) {

        log.info(" Saving Prediction !!! with pratientId : {} "  ,  patientId);

        String strPrediction = predictionResult(file, patientId);

        Prediction prediction = Prediction.builder()
        .patientId(patientId)
        .prediction(strPrediction)
        .date(Instant.now())
        .build();

        predictionRepository.save(prediction);
        log.info("Prediction Saved Sucessfully with pratientId : {} "  ,  patientId );

        return strPrediction;

//        try {
////            InputStream inputStream = file.getInputStream();
////            BufferedImage bufferedImage = ImageIO.read(inputStream);
////            inputStream.close();
////            Prediction prediction = Prediction.builder()
////                    .patientId(patientId)
////                    .prediction(predictionResult(bufferedImage , patientId))
////                    .date(Instant.now())
////                    .build();
////            predictionRepository.save(prediction);
////            log.info("Prediction Saved Sucessfully with pratientId : {} "  ,  patientId );
////            return predictionResult(bufferedImage , patientId);
//        }catch ( IOException e) {
//            throw new CustomException("Invalid image","INVALID_FILE_FORMAT");
//        }
    }

    @Override
    public List<Prediction> getLastSevenDaysPredictions() {
        log.info("Getting Last Seven Days Prediction Details");
        Instant endDate = Instant.now();
        Instant startDate = endDate.minus(Duration.ofDays(7));
        List<Prediction> list=  predictionRepository.findAllByDateBetween(startDate, endDate);
        
        log.info("Last Seven Days Prediction Details Got Successfully!!!");
        return list;
    }


    @Override
    public Map<String, Long> getCountByPrediction() {
        log.info("Getting Count Of Prediction Details");
        List<Object[]> counts = predictionRepository.getCountByPrediction();
        Map<String, Long> result = new HashMap<>();
        for (Object[] count : counts) {
            result.put((String)count[0], (Long)count[1]);
        }
        log.info("Count Of Prediction Details Got Successfully!!!");
        return result;
    }

    @Override
    public void deleteByPredictionId(String predictionId) {
        predictionRepository.findById(predictionId).orElseThrow(() -> new CustomException("Prediction Id Not Found", "INVALID_PREDICTION_ID"));
        predictionRepository.deleteById(predictionId);
        log.info("Prediction Deleted Successfully!!! With predictionId : {}" , predictionId);
    }

    @Override
    public List<Prediction> getAllPrediction() {
        try {
            return predictionRepository.findAll();
        } catch (DataAccessException ex) {
            log.error("Error getting all predictions: {}", ex.getMessage());
            throw new CustomException("Error getting all predictions", "INVALID_PREDICTION_ID");
        }
    }


    private String predictionResult(MultipartFile image , String predictionId) {

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", image.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(this.itisCaptureURI+itisCapturePredictionEndpoint , requestEntity, String.class);

//       switch (predictionId.charAt(0)){
//
//           case 'A': return "Heart Attack";
//           case 'B': return "Coronary artery disease (CAD)";
//           case 'C': return "Hypertrophic cardiomyopathy";
//           case 'D': return "Atherosclerosis";
//           case 'E': return "Heart Attack";
//           case 'F': return "High blood pressure";
//           case 'G': return "Valve disease";
//           case 'H': return "Atrial fibrillation";
//           case 'I': return "Valve disease";
//           case 'J': return "Dilated cardiomyopathy";
//           case 'K': return "Hypertrophic cardiomyopathy";
//           case 'L': return "Valve disease";
//           case 'M': return "Aortic stenosis";
//           case 'N': return "Valve disease";
//           case 'O': return "Coronary artery disease (CAD)";
//           case 'P': return "Pulmonary embolism";
//           case 'Q': return "Valve disease";
//           case 'R': return "Pericarditis";
//           case 'S': return "High blood pressure";
//           case 'T': return "Atherosclerosis";
//           case 'U': return "High blood pressure";
//           case 'V': return "Pulmonary embolism";
//           case 'W': return "Atrial fibrillation";
//           case 'X': return "Dilated cardiomyopathy";
//           case 'Y': return "Pericarditis";
//           case 'Z': return "Aortic stenosis";
//
//       }

        return response.getBody();
    }

    @Override
    public Map<Date, Long> getCountByLastSevenDates() {

        Instant endDate = Instant.now();
        Instant startDate = endDate.minus(7, ChronoUnit.DAYS);
        List<Object[]> resultList = predictionRepository.countByDateBetween(startDate, endDate);
        Map<Date, Long> countByDate = new HashMap<>();
        for (Object[] result : resultList) {
            Instant instant = (Instant) result[0];
            Date date = Date.from(instant);
            Long count = (Long) result[1];
            countByDate.put(date, count);
        }
        return countByDate;
    }

    public List<Prediction> demo() {
        log.info("Getting Last Seven Days Prediction Details");
        Instant endDate = Instant.now();
        Instant startDate = endDate.minus(Duration.ofDays(7));
        List<Prediction> list=  predictionRepository.findAllByDateBetween(startDate, endDate);
        log.info("Last Seven Days Prediction Details Got Successfully!!!");
        return list;
    }


}
