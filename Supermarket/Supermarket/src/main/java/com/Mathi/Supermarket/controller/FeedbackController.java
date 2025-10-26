package com.Mathi.Supermarket.controller;

import com.Mathi.Supermarket.model.Feedback;
import com.Mathi.Supermarket.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/feedbacks")
@CrossOrigin(origins = "*")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<?> submitFeedback(@RequestBody Map<String, Object> request) {
        try {
            String username = (String) request.get("username");
            if (username == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "message", "Username is required"
                ));
            }

            Feedback feedback = new Feedback();
            feedback.setMessage((String) request.get("message"));
            feedback.setRating((Integer) request.get("rating"));

            Feedback savedFeedback = feedbackService.saveFeedback(feedback, username);
            return ResponseEntity.ok(Map.of(
                    "message", "Feedback submitted successfully",
                    "feedback", savedFeedback
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "message", "Failed to submit feedback",
                    "error", e.getMessage()
            ));
        }
    }

    @PutMapping("/{id}/rating")
    public ResponseEntity<?> updateRating(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String username = (String) request.get("username");
            if (username == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "message", "Username is required"
                ));
            }

            Integer rating = (Integer) request.get("rating");
            if (rating == null || rating < 1 || rating > 5) {
                return ResponseEntity.status(400).body(Map.of(
                        "message", "Rating must be between 1 and 5"
                ));
            }

            Feedback updatedFeedback = feedbackService.updateRating(id, rating, username);
            if (updatedFeedback != null) {
                return ResponseEntity.ok(Map.of(
                        "message", "Rating updated successfully",
                        "feedback", updatedFeedback
                ));
            } else {
                return ResponseEntity.status(403).body(Map.of(
                        "message", "Feedback not found or access denied"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "message", "Failed to update rating",
                    "error", e.getMessage()
            ));
        }
    }

    @GetMapping
    public ResponseEntity<List<Feedback>> getFeedbacksByUser(@RequestParam String username) {
        try {
            List<Feedback> feedbacks = feedbackService.getFeedbacksByUser(username);
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedbackById(@PathVariable Long id) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
        if (feedback.isPresent()) {
            return ResponseEntity.ok(feedback.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
