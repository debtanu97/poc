package com.example.api;

import com.example.model.KafkaMessage;
import com.example.model.UserDetails;
import com.example.service.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaProducer kafkaProducer;

    private final ObjectMapper objectMapper;

    @PostMapping("/publish")
    ResponseEntity<?> publishMessage(
            @RequestParam String kafkaTopic,
            @RequestBody KafkaMessage<UserDetails> kafkaMessage
    ) {
        try {
            kafkaProducer.sendMessage(
                    objectMapper.writeValueAsString(kafkaMessage),
                    kafkaTopic
            );
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
