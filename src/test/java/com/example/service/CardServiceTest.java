package com.example.service;

import com.example.entity.Card;
import com.example.exception.DuplicateCardException;
import com.example.exception.InvalidPanException;
import com.example.model.CardResponse;
import com.example.model.CreateCardRequest;
import com.example.model.SearchCardRequest;
import com.example.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {

    private final CardRepository cardRepository = mock(CardRepository.class);
    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final CardService cardService = new CardService(cardRepository, encryptionService);

    @Test
    void createCardShouldThrowInvalidPanExceptionForInvalidPan() {
        CreateCardRequest request = new CreateCardRequest();
        request.setPan("123");
        request.setCardholderName("John Doe");

        assertThrows(InvalidPanException.class, () -> cardService.createCard(request));
        verify(cardRepository, never()).findByPanHash(any());
    }

    @Test
    void createCardShouldThrowDuplicateCardExceptionForExistingCard() throws Exception {
        CreateCardRequest request = new CreateCardRequest();
        request.setPan("4111111111111111");
        request.setCardholderName("John Doe");
        when(encryptionService.hash(anyString())).thenReturn("hashedPan");
        when(cardRepository.findByPanHash("hashedPan")).thenReturn(java.util.Optional.of(new Card()));

        assertThrows(DuplicateCardException.class, () -> cardService.createCard(request));
        verify(cardRepository).findByPanHash("hashedPan");
    }

    @Test
    void searchCardsShouldThrowBadRequestForInvalidQuery() {
        SearchCardRequest request = new SearchCardRequest();
        request.setQuery("invalidQuery");

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> cardService.searchCards(request));
        assertEquals("Invalid search query. Must be 4 digits or full PAN (12-19 digits)", exception.getStatusText());
        verify(cardRepository, never()).findByPanHash(any());
        verify(cardRepository, never()).findByLast4Hash(any());
    }

    @Test
    void searchCardsShouldReturnEmptyListForNonExistentLast4Digits() throws Exception {
        SearchCardRequest request = new SearchCardRequest();
        request.setQuery("1234");
        when(encryptionService.hash("1234")).thenReturn("hashedLast4");
        when(cardRepository.findByLast4Hash("hashedLast4")).thenReturn(Collections.emptyList());

        List<CardResponse> result = cardService.searchCards(request);

        assertTrue(result.isEmpty());
        verify(cardRepository).findByLast4Hash("hashedLast4");
    }

    @Test
    void searchCardsShouldReturnCardResponseForValidFullPan() throws Exception {
        SearchCardRequest request = new SearchCardRequest();
        request.setQuery("4111111111111111");
        Card card = new Card();
        card.setCardholderName("John Doe");
        card.setMaskedPan("**** **** **** 1111");
        card.setCreatedAt(Instant.now());
        when(encryptionService.hash("4111111111111111")).thenReturn("hashedPan");
        when(cardRepository.findByPanHash("hashedPan")).thenReturn(java.util.Optional.of(card));

        List<CardResponse> result = cardService.searchCards(request);

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getCardholderName());
        assertEquals("**** **** **** 1111", result.get(0).getMaskedPan());
        verify(cardRepository).findByPanHash("hashedPan");
    }
}