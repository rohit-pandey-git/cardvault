package com.example.controller;


import com.example.model.CardResponse;
import com.example.model.CreateCardRequest;
import com.example.model.SearchCardRequest;
import com.example.service.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CardControllerTest {

    private final CardService cardService = mock(CardService.class);
    private final CardController cardController = new CardController(cardService);

    @Test
    void createCardShouldReturnCardResponseForValidRequest() throws Exception {
        CreateCardRequest request = new CreateCardRequest();
        CardResponse response = new CardResponse("John Doe", "**** **** **** 1234", null);
        when(cardService.createCard(request)).thenReturn(response);

        ResponseEntity<CardResponse> result = cardController.createCard(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(cardService).createCard(request);
    }

    @Test
    void searchShouldReturnCardResponsesForValidQuery() throws Exception {
        SearchCardRequest request = new SearchCardRequest();
        request.setQuery("1234");
        List<CardResponse> responses = Collections.singletonList(new CardResponse("John Doe", "**** **** **** 1234", Instant.now().minus(7, ChronoUnit.DAYS)));
        when(cardService.searchCards(request)).thenReturn(responses);

        ResponseEntity<List<CardResponse>> result = cardController.search(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responses, result.getBody());
        verify(cardService).searchCards(request);
    }

}