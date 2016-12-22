package com.jalgoarena

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication
@EnableEurekaClient
open class JAlgoArenaProblemsApp

fun main(args: Array<String>) {
    SpringApplication.run(JAlgoArenaProblemsApp::class.java, *args)
}
