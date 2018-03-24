package com.dylancamus.tabletoprank

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.annotation.PostConstruct

@SpringBootApplication
internal class TabletopRankApplication {

//    val logger: Logger = LoggerFactory.getLogger(TabletopRankApplication::class.java)
//
//    @Value("\${spring.datasource.username}")
//    lateinit var username: String
//
//    @PostConstruct
//    fun log() = logger.info("Username is $username")

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()
}

fun main(args: Array<String>) {
    SpringApplication.run(TabletopRankApplication::class.java, *args)
}
