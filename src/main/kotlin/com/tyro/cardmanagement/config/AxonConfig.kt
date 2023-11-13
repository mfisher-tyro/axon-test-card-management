package com.tyro.cardmanagement.config

import org.axonframework.commandhandling.SimpleCommandBus
import org.axonframework.eventhandling.SimpleEventBus
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class AxonConfig {

    // Simple command bus using Spring's PlatformTransactionManager
    @Bean
    fun commandBus(platformTransactionManager: PlatformTransactionManager?) = SimpleCommandBus.builder()
            .transactionManager(SpringTransactionManager(platformTransactionManager))
            .build()


    // Simple event bus
    @Bean
    fun eventBus() = SimpleEventBus.builder().build()

    // Do not need to declare JPA repositories, just annotating a domain class with @Aggregate
    // and @Entity will create this type of repository for the corresponding type automatically
}
