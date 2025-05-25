package com.quizprez.quizprezpptxparsing.controller.v1;

import com.quizprez.quizprezpptxparsing.configs.ImageEmbeddingConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageEmbeddingConfig imageEmbeddingConfig;

    @GetMapping(value = "/{imagePath}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody Resource image(@PathVariable String imagePath) throws IOException {
        Path path = Paths.get("").resolve(imageEmbeddingConfig.volumePath()).resolve(imagePath);
        System.out.println(path.toAbsolutePath());
        if (!Files.isRegularFile(path)) {
            throw new RuntimeException();
        }
        return new ByteArrayResource(Files.readAllBytes(path));
    }
}
