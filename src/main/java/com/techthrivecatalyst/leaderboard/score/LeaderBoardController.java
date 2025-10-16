package com.techthrivecatalyst.leaderboard.score;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class LeaderBoardController {

    private final LeaderBoardService service;

    public LeaderBoardController(LeaderBoardService service) {
        this.service = service;
    }

    @PostMapping("/scores")
    public ResponseEntity<?> submitScore(@RequestBody SubmitRequest body) {
        if (!StringUtils.hasText(body.getUserId())) {
            return ResponseEntity.badRequest().body("userId required");
        }

        service.submitScore(body.getUserId(), body.getScore());
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/top/{n}")
    public ResponseEntity<List<Map<String, Object>>> topN(@PathVariable("n") int n) {
        return ResponseEntity.ok(service.topN(n));
    }
}
