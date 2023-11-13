package com.tyro.cardmanagement.adapter.requires.rest

import com.github.javafaker.CreditCardType
import com.github.javafaker.Faker
import com.tyro.cardmanagement.command.Card
import com.tyro.cardmanagement.ports.PartyClient
import com.tyro.cardmanagement.ports.PismoClient
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.random.Random

@Service
class PismoClientImpl : PismoClient {
    private val faker = Faker()

    override fun createPismoAccountAndCustomer(crn: String, party: PartyClient.Party): PismoClient.PismoAccountAndCustomer {
        Random.nextInt(from = 0, until = 3).takeIf { it == 0 }?.let { throw RuntimeException("Pismo.createPismoAccountAndCustomer is down") }
        return PismoClient.PismoAccountAndCustomer(
                pismoAccountId = UUID.randomUUID().toString(),
                pismoCustomerId = UUID.randomUUID().toString(),
        )
    }

    override fun createPismoCard(pismoAccountId: String, pismoCustomerId: String, party: PartyClient.Party): PismoClient.PismoCard {
        Random.nextInt(from = 0, until = 5).takeIf { it == 0 }?.let { throw RuntimeException("Pismo.createPismoCard is down") }
        return PismoClient.PismoCard(
            pismoCardId = UUID.randomUUID().toString(),
            pismoAccountId = UUID.randomUUID().toString(),
            pismoCustomerId = UUID.randomUUID().toString(),
            embossedName = party.name,
            maskedPan = faker.finance().creditCard(CreditCardType.VISA)
                    .replaceRange(5, 9, "****")
        )
    }

    override fun deletePismoCard(card: Card) {
        Random.nextInt(from = 0, until = 2).takeIf { it == 0 }?.let { throw RuntimeException("Pismo.deletePismoCard is down") }
        // do nothing
    }
}
