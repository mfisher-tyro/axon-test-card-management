package com.tyro.cardmanagement.core

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreateCardCommand(@TargetAggregateIdentifier val cardId: String, val crn: String)
data class DeleteCardCommand(@TargetAggregateIdentifier val cardId: String)
data class CreatePismoAccountAndCustomerCommand(@TargetAggregateIdentifier val cardId: String, val crn: String)
