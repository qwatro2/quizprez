package com.quizprez.quizprezquiz.controller.v1;

import com.quizprez.quizprezquiz.dto.ParticipantJoinRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/participant")
@RequiredArgsConstructor
public class ParticipantController {

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody ParticipantJoinRequest req) {
        throw new NotImplementedException();
    }
}
