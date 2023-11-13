package com.tyro.cardmanagement.adapter.provides.rest

import com.tyro.cardmanagement.core.CreateCardCommand
import com.tyro.cardmanagement.core.CreatePismoAccountAndCustomerCommand
import com.tyro.cardmanagement.core.DeleteCardCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID
import java.util.concurrent.Future

@RestController
class CardController(
    private val commandGateway: CommandGateway,
    private val reactorCommandGateway: ReactorCommandGateway,
) {

    data class CreateCardRequest(val crn: String)

    // region blocking
    @PostMapping("/blocking/cards")
    fun createCardBlocking(@RequestBody body: CreateCardRequest): String {
        return commandGateway.sendAndWait(CreateCardCommand(UUID.randomUUID().toString(), body.crn))
    }

    @DeleteMapping("/blocking/cards/{cardId}")
    fun deleteCardBlocking(@PathVariable cardId: String) {
        commandGateway.sendAndWait<Unit>(DeleteCardCommand(cardId))
    }
    // endregion

    // region future
    @PostMapping("/futures/cards")
    fun createCardFutures(@RequestBody body: CreateCardRequest): Future<String> {
        return commandGateway.send(CreateCardCommand(UUID.randomUUID().toString(), body.crn))
    }

    @DeleteMapping("/futures/cards/{cardId}")
    fun deleteCardFutures(@PathVariable cardId: String) {
        commandGateway.send<Unit>(DeleteCardCommand(cardId))
    }
    // endregion

    // region reactor
    @PostMapping("/reactor/cards")
    fun createCardReactor(@RequestBody body: CreateCardRequest): Mono<String> {
        return reactorCommandGateway.send(CreateCardCommand(UUID.randomUUID().toString(), body.crn))
    }

    @PostMapping("/reactor/cards/{cardId}/fixCreated")
    fun fixCreatedCardReactor(@PathVariable cardId: String) {
        reactorCommandGateway.send<Unit>(CreatePismoAccountAndCustomerCommand(cardId, "1234567890")).subscribe()
    }

    @DeleteMapping("/reactor/cards/{cardId}")
    fun deleteCardReactor(@PathVariable cardId: String) {
        reactorCommandGateway.send<Unit>(DeleteCardCommand(cardId)).subscribe()
    }
    // endregion
}
