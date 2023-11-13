package com.tyro.cardmanagement.query.cards

import com.tyro.cardmanagement.command.Card
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class CardSummary(
    @Id
    var cardId: String? = null,
    var embossedName: String? = null,
    var maskedPan: String? = null,
    var state: Card.CardState,
)
