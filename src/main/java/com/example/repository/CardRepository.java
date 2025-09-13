package com.example.repository;

import com.example.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByPanHash(String panHash);
    List<Card> findByLast4Hash(String last4Hash);
}