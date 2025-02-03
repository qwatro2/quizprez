package com.quizprez.quizprezpresentation.controller.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/presentations")
public class PresentationController {

    @GetMapping
    public ResponseEntity<?> getPresentations() {
        return ResponseEntity.ok("Список презентаций (заглушка)");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPresentation(@PathVariable Long id) {
        return ResponseEntity.ok("Детали презентации с id " + id + " (заглушка)");
    }

    @PostMapping
    public ResponseEntity<?> createPresentation() {
        return ResponseEntity.ok("Презентация создана (заглушка)");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePresentation(@PathVariable Long id, @RequestParam String title) {
        return ResponseEntity.ok("Название презентации с id " + id + " обновлено на: " + title + " (заглушка)");
    }
}
