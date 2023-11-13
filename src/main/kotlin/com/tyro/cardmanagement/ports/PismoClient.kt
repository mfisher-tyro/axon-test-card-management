package com.tyro.cardmanagement.ports

import com.tyro.cardmanagement.command.Card

interface PismoClient {
    data class PismoAccountAndCustomer(val pismoAccountId: String, val pismoCustomerId: String)
    data class PismoCard(
        val pismoCardId: String,
        val pismoAccountId: String,
        val pismoCustomerId: String,
        val embossedName: String,
        val maskedPan: String,
    )

    fun createPismoAccountAndCustomer(crn: String, party: PartyClient.Party): PismoAccountAndCustomer
    fun createPismoCard(pismoAccountId: String, pismoCustomerId: String, party: PartyClient.Party): PismoCard
    fun deletePismoCard(card: Card)
}
