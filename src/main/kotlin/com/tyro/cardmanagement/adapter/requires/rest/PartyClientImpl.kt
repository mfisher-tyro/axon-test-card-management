package com.tyro.cardmanagement.adapter.requires.rest

import com.github.javafaker.Faker
import com.tyro.cardmanagement.ports.PartyClient
import org.springframework.stereotype.Service


@Service
class PartyClientImpl : PartyClient {
    private val faker = Faker()

    override fun getParty(crn: String): PartyClient.Party {
        return PartyClient.Party(
                partyId = "partyId",
                name = faker.name().fullName(),
                email = faker.internet().emailAddress(),
                address = faker.address().fullAddress(),
                number = faker.address().buildingNumber(),
                city = faker.address().city(),
                country = faker.address().country(),
        )
    }
}
