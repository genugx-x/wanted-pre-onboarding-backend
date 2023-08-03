package com.genug.wpob.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public ResponseEntity<?> healthCheck() {
        String message = "The service is up and running....";
        log.info("[HomeController] {}", message);
        return ResponseEntity.ok().body(message);
    }

}
