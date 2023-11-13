package com.tyro.cardmanagement.ports

interface PartyClient {
    data class Party(val partyId: String,
                     val name: String,
                     val email: String,
                     val address: String,
                     val number: String,
                     val city: String,
                     val country: String)
    fun getParty(crn: String): Party
}
