package com.example.marketapp

import com.example.marketapp.repository.AccountRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MarketAppApplication(val repository: AccountRepository) : CommandLineRunner {

    override fun run(vararg args: String?) {
        runBlocking {

        }
    }
}

fun main(args: Array<String>) {
    runApplication<MarketAppApplication>(*args)
}
