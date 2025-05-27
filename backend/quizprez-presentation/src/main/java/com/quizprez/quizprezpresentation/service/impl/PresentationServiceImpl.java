package com.quizprez.quizprezpresentation.service.impl;

import com.quizprez.quizprezpresentation.dto.PresentationRequest;
import com.quizprez.quizprezpresentation.dto.PresentationResponse;
import com.quizprez.quizprezpresentation.exception.ResourceNotFoundException;
import com.quizprez.quizprezpresentation.model.Presentation;
import com.quizprez.quizprezpresentation.repository.PresentationRepository;
import com.quizprez.quizprezpresentation.service.CustomHtmlConverter;
import com.quizprez.quizprezpresentation.service.PresentationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PresentationServiceImpl implements PresentationService {
    private final PresentationRepository repo;
    private final CustomHtmlConverter customHtmlConverter;

    @Override
    public PresentationResponse create(PresentationRequest req) {
        Presentation p = Presentation.builder()
                .ownerId(req.getOwnerId())
                .title(req.getTitle())
                .customHtml(req.getHtml())
                .convertedHtml(customHtmlConverter.convert(req.getHtml()))
                .build();
        p = repo.save(p);
        return toDto(p);
    }

    @Override
    public PresentationResponse getById(Long id) {
        return repo.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Presentation", "id", id));
    }

    @Override
    public List<PresentationResponse> getAll() {
        return repo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PresentationResponse> getByOwner(Long ownerId) {
        return repo.findByOwnerId(ownerId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PresentationResponse update(Long id, PresentationRequest req) {
        Presentation p = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presentation", "id", id));
        p.setTitle(req.getTitle());
        p.setCustomHtml(req.getHtml());
        p.setConvertedHtml(customHtmlConverter.convert(req.getHtml()));
        p.setOwnerId(req.getOwnerId());
        p = repo.save(p);
        return toDto(p);
    }

    @Override
    public void delete(Long id) {
        Presentation p = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presentation", "id", id));
        repo.delete(p);
    }

    private PresentationResponse toDto(Presentation p) {
        return PresentationResponse.builder()
                .id(p.getId())
                .ownerId(p.getOwnerId())
                .title(p.getTitle())
                .customHtml(p.getCustomHtml())
                .convertedHtml(p.getConvertedHtml())
                .build();
    }
}
