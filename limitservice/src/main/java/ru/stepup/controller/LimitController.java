package ru.stepup.controller;

import ru.stepup.model.Limit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stepup.service.LimitService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/limits")
public class LimitController {

    @Autowired
    private LimitService limitService;

    @GetMapping("/limit/{userId}")
    public ResponseEntity<Limit> getLimitByUserId(@PathVariable Long userId) {
        Limit limit = limitService.getLimitByUserId(userId);
        return ResponseEntity.ok(limit);
    }

    @PostMapping("/reducelimit")
    public ResponseEntity<Limit> reduceUserLimit(@RequestParam Long userId, @RequestParam BigDecimal summa) {
        Limit limit = limitService.reduceUserLimit(userId, summa);
        return ResponseEntity.ok(limit);
    }

    @PostMapping("/restorelimit/{userId}")
    public ResponseEntity<Void> restoreUserLimit(@PathVariable Long userId) {
        limitService.restoreUserLimit(userId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}