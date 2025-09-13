package com.example.controller;

import com.example.model.CardResponse;
import com.example.model.CreateCardRequest;
import com.example.model.SearchCardRequest;
import com.example.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/cards")
public class CardController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CardController.class);
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    @Operation(summary = "Create a new card")
    public ResponseEntity<CardResponse> createCard(@RequestBody @Valid CreateCardRequest request) throws Exception {
        log.info("Create card request received");
        CardResponse card = cardService.createCard(request);
        log.info("Card created successfully");
        return ResponseEntity.ok(card);
    }

    //POST is used instead of GET to avoid logging sensitive data in URL search params via browser history, server logs etc.
    @PostMapping("/search")
    @Operation(summary = "Search for a card")
    public ResponseEntity<List<CardResponse>> search(@RequestBody @Valid SearchCardRequest request) throws Exception {
        log.info("Search card request received");
        List<CardResponse> cardResponses = cardService.searchCards(request);
        return ResponseEntity.ok(cardResponses);
    }
}