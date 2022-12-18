package core.ktorplugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.vertx.core.json.jackson.DatabindCodec

fun Application.configureSerialization(){
    install(ContentNegotiation) {
        //Json object to should be converted using Vertx Jackson pretty Mapper. Use of pretty so that when printed it's formatted
        register(ContentType.Application.Json, JacksonConverter(DatabindCodec.prettyMapper()))
    }
}
