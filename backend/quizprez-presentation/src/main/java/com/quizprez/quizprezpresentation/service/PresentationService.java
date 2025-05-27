package com.quizprez.quizprezpresentation.service;

import com.quizprez.quizprezpresentation.dto.PresentationRequest;
import com.quizprez.quizprezpresentation.dto.PresentationResponse;

import java.util.List;
import java.util.Map;

public interface PresentationService {
    PresentationResponse create(PresentationRequest request, Map<String, Object> options);
    PresentationResponse getById(Long id);
    List<PresentationResponse> getAll();
    List<PresentationResponse> getByOwner(Long ownerId);
    PresentationResponse update(Long id, PresentationRequest request, Map<String, Object> options);
    void delete(Long id);
}
