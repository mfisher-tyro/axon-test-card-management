package com.tyro.cardmanagement.adapter.provides.rest

import com.tyro.cardmanagement.core.AllCardsQuery
import com.tyro.cardmanagement.query.cards.CardSummary
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.concurrent.Future

@RestController
class QueryController(private val gateway: QueryGateway, private val reactorQueryGateway: ReactorQueryGateway) {

    @GetMapping("/blocking/cards")
    fun listCardsBlocking(): List<CardSummary> {
        return gateway.query(AllCardsQuery(), ResponseTypes.multipleInstancesOf(CardSummary::class.java)).get()
    }

    @GetMapping("/futures/cards")
    fun listCardsFutures(): Future<List<CardSummary>> {
        return gateway.query(AllCardsQuery(), ResponseTypes.multipleInstancesOf(CardSummary::class.java))
    }

    @GetMapping("/reactor/cards")
    fun listCardsReactor(): Mono<List<CardSummary>> {
        return reactorQueryGateway.query(AllCardsQuery(), ResponseTypes.multipleInstancesOf(CardSummary::class.java))
    }
}
