package com.tyro.cardmanagement.core

data class CardCreatedEvent(val cardId: String, val crn: String)
data class PismoCardCreatedEvent(
    val cardId: String,
    val pismoAccountId: String,
    val pismoCustomerId: String,
    val pismoCardId: String,
    val embossedName: String,
    val maskedPan: String,
)
data class CardDeletedEvent(val cardId: String)
