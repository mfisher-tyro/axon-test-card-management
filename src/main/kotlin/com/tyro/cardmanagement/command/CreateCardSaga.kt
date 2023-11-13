package com.tyro.cardmanagement.command

import com.tyro.banking.logger.LoggerDelegate
import com.tyro.cardmanagement.core.CardCreatedEvent
import com.tyro.cardmanagement.core.CreatePismoAccountAndCustomerCommand
import com.tyro.cardmanagement.core.PismoCardCreatedEvent
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.SagaLifecycle
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired

@Saga
class CreateCardSaga {
    companion object {
        val logger by LoggerDelegate()
    }

    @Autowired
    @Transient
    private lateinit var commandGateway: CommandGateway

    @StartSaga
    @SagaEventHandler(associationProperty = "cardId")
    fun handle(event: CardCreatedEvent) {
        logger.debug{ "[Saga] Handle $event" }
        commandGateway.send<CreatePismoAccountAndCustomerCommand>(CreatePismoAccountAndCustomerCommand(event.cardId, event.crn))
    }

    @SagaEventHandler(associationProperty = "cardId")
    fun handle(event: PismoCardCreatedEvent) {
        logger.debug{ "[Saga] Ending saga for card ${event.cardId}" }
        SagaLifecycle.end()
    }
}
