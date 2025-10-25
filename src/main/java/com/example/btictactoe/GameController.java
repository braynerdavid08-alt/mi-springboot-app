package com.example.btictactoe;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.btictactoe.GameState.PlayRequest;

@RestController
@RequestMapping("/api")

public class GameController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

   
    @PostMapping("/start")
    public ResponseEntity<PlayResponse> start() {
        PlayResponse resp = service.startGame();
        return ResponseEntity.ok(resp);
    }

    
    @PostMapping("/play")
    public ResponseEntity<PlayResponse> play(@RequestBody PlayRequest request) {
       
        if (request == null || request.board() == null || request.board().length != 9) {
            return ResponseEntity.badRequest().build();
        }
        PlayResponse resp = service.play(request.board(), request.move());
        return ResponseEntity.ok(resp);
    }
}

    
