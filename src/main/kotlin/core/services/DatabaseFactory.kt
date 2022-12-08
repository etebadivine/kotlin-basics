package core.services


import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import components.pets.dao.Pets
import core.services.Configuration.getSystemProperties
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

object DatabaseFactory {
    private val log = LoggerFactory.getLogger(this::class.java)
    private var hasRun = false
    fun connect() {
        log.info("Connecting to database")
        val pool = hikari()
        Database.connect(pool)
        runUpdateAndMigration()
        //runFlyway(pool)
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            this.jdbcUrl = getSystemProperties().getProperty("hikari.jdbcUrl")
            this.username = getSystemProperties().getProperty("hikari.dataSource.user")
            this.password = getSystemProperties().getProperty("hikari.dataSource.password")
            this.isAutoCommit = true
            this.maximumPoolSize = getSystemProperties().getProperty("hikari.dataSource.maxPoolSize","10").toInt()
        }
        config.validate()
        return HikariDataSource(config)
    }

    private fun runUpdateAndMigration(){
        if(!hasRun){
            transaction {
                SchemaUtils.createMissingTablesAndColumns(Pets)
            }
            hasRun = true
        }

    }

    /*private fun runFlyway(datasource: DataSource) {
        val flyway = Flyway.configure()
            .dataSource(datasource)
            .load()
        try {
            flyway.info()
            flyway.migrate()
        } catch (e: Exception) {
            log.error("Exception running flyway migration", e)
            throw e
        }
        log.info("Flyway migration has finished")
    }*/

}