package com.example.service;

import com.example.entity.Card;
import com.example.exception.DuplicateCardException;
import com.example.exception.InvalidPanException;
import com.example.model.CardResponse;
import com.example.model.CreateCardRequest;
import com.example.model.SearchCardRequest;
import com.example.repository.CardRepository;
import com.example.util.PanValidator;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CardService.class);
    private final CardRepository cardRepository;
    private final EncryptionService encryptionService;

    public CardService(CardRepository cardRepository, EncryptionService encryptionService) {
        this.cardRepository = cardRepository;
        this.encryptionService = encryptionService;
    }


    @Transactional
    //Needed for consistency and predicable behavior. Not needed for H2 but needed for other DBs like Postgres.
    //H2 works in auto commit mode by default.
    public CardResponse createCard(CreateCardRequest request) throws Exception {
        String pan = request.getPan();
        String name = request.getCardholderName();

        if (!PanValidator.isValid(pan)) {
            log.info("PAN is invalid");
            throw new InvalidPanException("PAN is invalid");
        }

        String panHash = encryptionService.hash(pan);
        if (cardRepository.findByPanHash(panHash).isPresent()) {
            log.info("Card already exists");
            throw new DuplicateCardException("Card already exists");
        }

        byte[] iv = encryptionService.generateIv();
        byte[] encryptedPan = encryptionService.encrypt(pan, iv);
        String maskedPan = encryptionService.maskPan(pan);
        log.info("Card details encrypted and masked");
        Card card = new Card();
        card.setCardholderName(name);
        card.setEncryptedPan(encryptedPan);
        card.setIv(iv);
        card.setPanHash(panHash);
        card.setLast4Hash(encryptionService.hash(pan.substring(pan.length() - 4)));
        card.setMaskedPan(maskedPan);
        card.setCreatedAt(Instant.now());
        card.setKeyVersion(encryptionService.getCurrentKeyVersion());

        cardRepository.saveAndFlush(card);
        return new CardResponse(name, maskedPan, card.getCreatedAt());
    }

    public List<CardResponse> searchCards(SearchCardRequest request) throws Exception {
        String query = request.getQuery();
        List<Card> results = new ArrayList<>();

        if (!request.getQuery().matches("\\d{4}")
                && !PanValidator.isValid(request.getQuery())) {
            log.info("Invalid search query: {}", request.getQuery());
            throw HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "Invalid search query. Must be 4 digits or full PAN (12-19 digits)", null, null, null);
        }

        if (query.length() == 4) {
            log.info("Searching card with last 4 digits");
            results = cardRepository.findByLast4Hash(encryptionService.hash(query));
        } else {
            log.info("Searching card with full PAN");
            cardRepository.findByPanHash(encryptionService.hash(query)).ifPresent(results::add);
        }

        log.info("Cards found : {}", results.size());
        return results.stream()
                .map(card -> new CardResponse(card.getCardholderName(), card.getMaskedPan(), card.getCreatedAt()))
                .collect(Collectors.toList());
    }

}