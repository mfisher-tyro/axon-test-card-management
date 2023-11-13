package com.tyro.cardmanagement.command

import com.tyro.banking.logger.LoggerDelegate
import com.tyro.cardmanagement.core.CardCreatedEvent
import com.tyro.cardmanagement.core.CardDeletedEvent
import com.tyro.cardmanagement.core.CreateCardCommand
import com.tyro.cardmanagement.core.CreatePismoAccountAndCustomerCommand
import com.tyro.cardmanagement.core.DeleteCardCommand
import com.tyro.cardmanagement.core.PismoCardCreatedEvent
import com.tyro.cardmanagement.ports.PartyClient
import com.tyro.cardmanagement.ports.PismoClient
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.EventHandler
import org.axonframework.messaging.interceptors.ExceptionHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
@Entity
class Card() {
    companion object {
        val logger by LoggerDelegate()
    }

    enum class CardState {
        CREATED, ACTIVE, CANCELED,
    }

    @AggregateIdentifier
    @Id
    private var cardId: String? = null
    private lateinit var crn: String
    private var cardState: CardState = CardState.CREATED

    private lateinit var pismoAccountId: String
    private lateinit var pismoCustomerId: String
    private lateinit var pismoCardId: String

    // region Create card
    @CommandHandler
    constructor(command: CreateCardCommand): this() {
        logger.debug { "[Aggregate][Command] Handle command: $command" }
        AggregateLifecycle.apply(CardCreatedEvent(command.cardId, command.crn))
    }

    @EventHandler
    fun on(event: CardCreatedEvent) {
        logger.debug { "[Aggregate][Event] On event: $event" }
        cardId = event.cardId
        crn = event.crn
        cardState = CardState.CREATED
    }

    @CommandHandler
    fun handle(command: CreatePismoAccountAndCustomerCommand, partyClient: PartyClient, pismoClient: PismoClient) {
        logger.debug { "[Aggregate][Command] Handle command: $command" }

        val party = partyClient.getParty(command.crn)
        // idempotent
        val pismoAccountAndCustomer = pismoClient.createPismoAccountAndCustomer(command.crn, party)
        val pismoCard = pismoClient.createPismoCard(
            pismoAccountId = pismoAccountAndCustomer.pismoAccountId,
            pismoCustomerId = pismoAccountAndCustomer.pismoCustomerId,
            party = party)

        AggregateLifecycle.apply(PismoCardCreatedEvent(
            cardId = cardId!!,
            pismoAccountId = pismoCard.pismoAccountId,
            pismoCustomerId = pismoCard.pismoCustomerId,
            pismoCardId = pismoCard.pismoCardId,
            embossedName = pismoCard.embossedName,
            maskedPan = pismoCard.maskedPan))
    }

    @ExceptionHandler
    fun handle(command: CreatePismoAccountAndCustomerCommand, exception: Exception) {
        logger.error { "[Aggregate][Exception] Handle $command exception: $exception" }
    }

    @EventHandler
    fun on(event: PismoCardCreatedEvent) {
        logger.debug { "[Aggregate][Event] On event: $event" }
        pismoAccountId = event.pismoAccountId
        pismoCustomerId = event.pismoCustomerId
        pismoCardId = event.pismoCardId
        cardState = CardState.ACTIVE
    }
    // endregion

    // region Delete card
    @CommandHandler
    fun handle(command: DeleteCardCommand, pismoClient: PismoClient) {
        logger.debug { "[Aggregate][Command] Handle command: $command" }

        pismoClient.deletePismoCard(this)

        AggregateLifecycle.apply(CardDeletedEvent(command.cardId))
    }

    @EventHandler
    fun on(event: CardDeletedEvent) {
        logger.debug { "[Aggregate][Event] On event: $event" }
        cardState = CardState.CANCELED
    }
    // endregion
}
