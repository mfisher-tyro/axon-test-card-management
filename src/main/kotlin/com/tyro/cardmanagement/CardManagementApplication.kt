package com.tyro.cardmanagement

import org.axonframework.springboot.autoconfig.JdbcAutoConfiguration
import org.axonframework.springboot.autoconfig.JpaEventStoreAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [ErrorMvcAutoConfiguration::class, JpaEventStoreAutoConfiguration::class, JdbcAutoConfiguration::class])
class CardManagementApplication

fun main(args: Array<String>) {
    runApplication<CardManagementApplication>(*args)
}
