package com.Mathi.Supermarket.repository;

import com.Mathi.Supermarket.model.Feedback;
import com.Mathi.Supermarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findAllByOrderByCreatedAtDesc();

    // Find feedbacks by specific user
    List<Feedback> findByUserOrderByCreatedAtDesc(User user);

    // Find feedback by ID and user (for security)
    List<Feedback> findByIdAndUser(Long id, User user);
}

