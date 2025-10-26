package com.Mathi.Supermarket.service;

import com.Mathi.Supermarket.model.Feedback;
import com.Mathi.Supermarket.model.User;
import com.Mathi.Supermarket.repository.FeedbackRepository;
import com.Mathi.Supermarket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    public Feedback saveFeedback(Feedback feedback, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        feedback.setUser(user);
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Feedback> getFeedbacksByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return feedbackRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    public Optional<Feedback> getFeedbackByIdAndUser(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Feedback> feedbacks = feedbackRepository.findByIdAndUser(id, user);
        return feedbacks.isEmpty() ? Optional.empty() : Optional.of(feedbacks.get(0));
    }

    public void deleteFeedback(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Feedback> feedbacks = feedbackRepository.findByIdAndUser(id, user);
        if (!feedbacks.isEmpty()) {
            feedbackRepository.deleteById(id);
        } else {
            throw new RuntimeException("Feedback not found or access denied");
        }
    }

    public boolean feedbackExists(Long id) {
        return feedbackRepository.existsById(id);
    }

    public boolean feedbackExistsForUser(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return !feedbackRepository.findByIdAndUser(id, user).isEmpty();
    }

    public Feedback updateRating(Long id, Integer rating, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Feedback> feedbacks = feedbackRepository.findByIdAndUser(id, user);
        if (!feedbacks.isEmpty()) {
            Feedback feedback = feedbacks.get(0);
            feedback.setRating(rating);
            return feedbackRepository.save(feedback);
        }
        return null;
    }
}

