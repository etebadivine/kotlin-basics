package components.pets.dao

import components.pets.models.Pet
import core.services.Configuration
import core.services.DatabaseFactory
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.jemos.podam.api.PodamFactoryImpl
import java.time.LocalDateTime
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS) //This annotation ask Junit5 to create only one instance of the class TradePostServiceTest and use it to perform the tests
internal class JDBCImplTest {

    private val log: Logger
    private lateinit var underTest:JDBCImpl
    private val factory: PodamFactoryImpl
    //init will be called once thus instantiating everything that need to be called once
    //init -> constructor -> any other constructors.... > Instantiated > ready for use
    init {
        Configuration.loadSystemProperties() // load my configuration file
        factory = PodamFactoryImpl()
        log = LoggerFactory.getLogger(this::class.java)
    }

    @BeforeAll
    fun setup(){
        DatabaseFactory.connect() // connect to database using resource in test package
        underTest = JDBCImpl()
        val pets = factory.manufacturePojoWithFullData(List::class.java, Pet::class.java) as List<Pet>
        pets.forEach{
          underTest.create(it)
        }
    }

    @AfterAll
    fun cleanup(){
        transaction {
            SchemaUtils.drop()
        }
    }

    @Test
    fun create() {
        //GIVEN
        val pet = factory.manufacturePojoWithFullData(Pet::class.java)
        //WHEN
        val expected = underTest.create(pet)
        //THEN
        assertThat(expected).isGreaterThan(0)
    }

    @Test
    fun update() {
        //GIVEN
        val list = underTest.list(1,50)
        val onePet = list[Random.nextInt(0,list.size)]
        onePet.weight = 2.5
        onePet.age = 2
        //WHEN
        val expected = underTest.update(onePet)
        //THEN
        assertThat(expected).isEqualTo(1)
    }

    @Test
    fun delete() {
        //GIVEN
        val list = underTest.list(1,50)
        val onePet = list[Random.nextInt(0,list.size)]
        //WHEN
        val expected = underTest.delete(onePet.id)
        //THEN
        assertThat(expected).isEqualTo(1)
    }

    @Test
    fun get() {
        //GIVEN
        val list = underTest.list(1,50)
        val onePet = list[Random.nextInt(0,list.size)]
        //WHEN
        val expected = underTest.get(onePet.id)
        //THEN
        assertThat(expected).isNotNull
        assertThat(expected).isEqualTo(onePet)
    }

    @Test
    fun list() {
        //GIVEN
        //WHEN
        //THEN
    }

    @Test
    fun testList() {
        //GIVEN
        //WHEN
        //THEN
    }
}