import core.ktorplugins.configureDI
import core.ktorplugins.configureHTTP
import core.ktorplugins.configureRouting
import io.ktor.application.*

fun main(args: Array<String>):Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(){
    configureDI()
    configureHTTP()
    configureRouting()
    //configureSerialization() TODO: Fixme
}