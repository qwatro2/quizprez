package com.quizprez.quizprezpptxparsing.controller.v1;

import com.quizprez.quizprezpptxparsing.services.PresentationConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/parse")
@RequiredArgsConstructor
public class ParserController {
    private static final Set<String> SUPPORTED_FORMATS = Set.of("pptx", "ppt", "odp");

    private final PresentationConverterService presentationConverterService;

    @PostMapping(value = "/pptx", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> parsePptx(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        String filename = file.getOriginalFilename();
        if (!isSupportedFormat(filename)) {
            return ResponseEntity.badRequest().body("Unsupported file format");
        }

        try {
            String html = presentationConverterService.convert(file);
            return ResponseEntity.ok().body(html);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error processing file: " + e.getMessage());
        }
    }

    // TODO: remove
    @RequestMapping(method = RequestMethod.OPTIONS, value = "/pptx")
    public ResponseEntity<?> dummyOptions() {
        return ResponseEntity.ok().build();
    }

    private boolean isSupportedFormat(String filename) {
        if (filename == null) {
            return false;
        }
        String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        return SUPPORTED_FORMATS.contains(ext);
    }
}
