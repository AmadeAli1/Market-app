package com.example.marketapp

import com.example.marketapp.service.UserService
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MarketAppApplication : CommandLineRunner {

    override fun run(vararg args: String?) {
        runBlocking {

//            service.save(
//                user = User(
//                    email = "aliamade29@gmail.com",
//                    username = "Amade Ali",
//                    password = "amade123"
//                )
//            )

        }
    }
}

fun main(args: Array<String>) {
    runApplication<MarketAppApplication>(*args)
}
