package core.ktorplugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import org.slf4j.event.Level

fun Application.configureHTTP(){
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }
    install(CallLogging){
        level = Level.DEBUG
    }
}