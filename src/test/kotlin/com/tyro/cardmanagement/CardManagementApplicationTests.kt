package com.tyro.cardmanagement

import org.assertj.core.api.Assertions
import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.SimpleCommandBus
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.SimpleEventBus
import org.axonframework.eventsourcing.eventstore.EventStorageEngine
import org.axonframework.modelling.saga.repository.SagaStore
import org.axonframework.modelling.saga.repository.jpa.JpaSagaStore
import org.axonframework.queryhandling.DefaultQueryGateway
import org.axonframework.queryhandling.QueryGateway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaRepository

@SpringBootTest
class CardManagementApplicationTests {

    @Autowired(required = false)
    @Qualifier("cardSummaryRepository")
    private val repository: Any? = null

    @Autowired(required = false)
    private val commandGateway: CommandGateway? = null

    @Autowired(required = false)
    private val commandBus: CommandBus? = null

    @Autowired(required = false)
    private val eventBus: EventBus? = null

    @Autowired(required = false)
    private val queryGateway: QueryGateway? = null

    @Autowired(required = false)
    private val eventStorageEngine: EventStorageEngine? = null

    @Autowired(required = false)
    private val sagaStore: SagaStore<Any>? = null

    @Test
    fun contextLoads() {
        Assertions.assertThat(repository)
                .isNotNull()
                .isInstanceOf(JpaRepository::class.java)
        Assertions.assertThat(commandGateway)
                .isNotNull()
                .isInstanceOf(DefaultCommandGateway::class.java)
        Assertions.assertThat(commandBus)
                .isNotNull()
                .isInstanceOf(SimpleCommandBus::class.java)
        Assertions.assertThat(eventBus)
                .isNotNull()
                .isInstanceOf(SimpleEventBus::class.java)
        Assertions.assertThat(queryGateway)
                .isNotNull()
                .isInstanceOf(DefaultQueryGateway::class.java)
        Assertions.assertThat(sagaStore)
                .isNotNull()
                .isInstanceOf(JpaSagaStore::class.java)
        // assert that we do not have any EventStorageEngine
        Assertions.assertThat(eventStorageEngine).isNull()
    }
}
