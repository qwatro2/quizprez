package com.quizprez.quizprezpresentation.controller.v1;

import com.quizprez.quizprezpresentation.dto.PresentationRequest;
import com.quizprez.quizprezpresentation.dto.PresentationResponse;
import com.quizprez.quizprezpresentation.service.PresentationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/presentations")
@RequiredArgsConstructor
public class PresentationController {
    private final PresentationService service;

    @PostMapping
    public ResponseEntity<PresentationResponse> create(@RequestBody PresentationRequest req) {
        PresentationResponse resp = service.create(req);
        return ResponseEntity.status(201).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PresentationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PresentationResponse>> getAll(
            @RequestParam(required = false) Long ownerId) {
        List<PresentationResponse> list =
                (ownerId == null)
                        ? service.getAll()
                        : service.getByOwner(ownerId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PresentationResponse> update(
            @PathVariable Long id,
            @Validated @RequestBody PresentationRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
