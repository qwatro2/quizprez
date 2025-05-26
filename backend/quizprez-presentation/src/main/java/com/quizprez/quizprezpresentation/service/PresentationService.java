package com.quizprez.quizprezpresentation.service;

import com.quizprez.quizprezpresentation.dto.PresentationRequest;
import com.quizprez.quizprezpresentation.dto.PresentationResponse;

import java.util.List;

public interface PresentationService {
    PresentationResponse create(PresentationRequest request);
    PresentationResponse getById(Long id);
    List<PresentationResponse> getAll();
    List<PresentationResponse> getByOwner(Long ownerId);
    PresentationResponse update(Long id, PresentationRequest request);
    void delete(Long id);
}
