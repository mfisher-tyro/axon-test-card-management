package com.tyro.cardmanagement.query.cards

import com.tyro.banking.logger.LoggerDelegate
import com.tyro.cardmanagement.command.Card
import com.tyro.cardmanagement.core.AllCardsQuery
import com.tyro.cardmanagement.core.CardCreatedEvent
import com.tyro.cardmanagement.core.CardDeletedEvent
import com.tyro.cardmanagement.core.PismoCardCreatedEvent
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

@Component
class CardSummaryProjection(private val cardSummaryRepository: CardSummaryRepository) {
    companion object {
        val logger by LoggerDelegate()
    }

    @EventHandler
    fun on(event: CardCreatedEvent) {
        logger.debug { "[Query][Cards][Event] On event: $event" }
        cardSummaryRepository.save(CardSummary(event.cardId, null, null, Card.CardState.CREATED))
    }

    @EventHandler
    fun on(event: CardDeletedEvent) {
        logger.debug { "[Query][Cards][Event] On event: $event" }
        val card = cardSummaryRepository.findById(event.cardId)
        card.ifPresent { summary: CardSummary ->
            summary.state = Card.CardState.CANCELED
            cardSummaryRepository.save(summary)
        }
    }

    @EventHandler
    fun on(event: PismoCardCreatedEvent) {
        logger.debug { "[Query][Cards][Event] On event: $event" }
        val card = cardSummaryRepository.findById(event.cardId)
        card.ifPresent { summary: CardSummary ->
            summary.embossedName = event.embossedName
            summary.maskedPan = event.maskedPan
            summary.state = Card.CardState.ACTIVE
            cardSummaryRepository.save(summary)
        }
    }

    @QueryHandler
    fun handle(query: AllCardsQuery): List<CardSummary> {
        logger.debug { "[Query][Cards] Handle query: $query" }
        return cardSummaryRepository.findCardSummariesByStateNot(Card.CardState.CANCELED)
    }
}

interface CardSummaryRepository: JpaRepository<CardSummary, String> {
    fun findCardSummariesByStateNot(status: Card.CardState): List<CardSummary>
}
