package com.quizprez.quizprezpptxparsing.controller.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/parse")
public class ParserController {

    @PostMapping("/pptx")
    public ResponseEntity<?> parsePptx(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok("Файл " + file.getOriginalFilename() + " принят для парсинга (заглушка). Результатом будет HTML-код.");
    }
}
