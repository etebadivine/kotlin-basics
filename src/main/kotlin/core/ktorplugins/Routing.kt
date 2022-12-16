package core.ktorplugins

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting(){

    routing {
        get("/ping"){
            call.respondText { "Pong!" }
        }
    }
}