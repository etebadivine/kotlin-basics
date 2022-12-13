package components.toys.dao


import components.toys.models.Toy
import core.services.Configuration
import core.services.DatabaseFactory
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.jemos.podam.api.PodamFactoryImpl
import kotlin.random.Random


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ToyJDBCImplTest {

    private val log: Logger
    private lateinit var underTest: ToyJDBCImpl
    private val factory: PodamFactoryImpl
    private lateinit var toys:List<Toy>

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
        underTest = ToyJDBCImpl()
        toys = factory.manufacturePojoWithFullData(List::class.java, Toy::class.java) as List<Toy>
        toys.forEach{
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
        val toy = factory.manufacturePojoWithFullData(Toy::class.java)
        //WHEN
        val expected = underTest.create(toy)
        //THEN
        assertThat(expected).isGreaterThan(0)
    }

    @Test
    fun `it cannot update when ID not found in DB`() {
        //GIVEN
        val anotherToy = toys.first()
        anotherToy.owner = "James"
        //WHEN
        val expected = underTest.update(anotherToy)
        //THEN
        assertThat(expected).isEqualTo(0)
    }

    @Test
    fun `it should update`() {
        //GIVEN
        val list = underTest.list(1,50)
        val oneToy = list[Random.nextInt(0,list.size)]
        oneToy.owner = "Divine"
        //WHEN
        val expected = underTest.update(oneToy)
        //THEN
        assertThat(expected).isEqualTo(1)
    }

    @Test
    fun `it cannot delete when ID not found in DB`() {
        //GIVEN
        val anotherToy = toys.first()
        //WHEN
        val expected = underTest.delete(anotherToy.id)
        //THEN
        assertThat(expected).isEqualTo(0)
    }

    @Test
    fun `it should delete`() {
        //GIVEN
        val list = underTest.list(1,50)
        val oneToy = list[Random.nextInt(0,list.size)]
        //WHEN
        val expected = underTest.delete(oneToy.id)
        //THEN
        assertThat(expected).isEqualTo(1)
    }

    @Test
    fun `it cannot get when ID not found in DB`() {
        //GIVEN
        val anotherToy = toys.first()
        //WHEN
        val expected = underTest.get(anotherToy.id)
        //THEN
        assertThat(expected).isNull()
    }

    @Test
    fun get() {
        //GIVEN
        val list = underTest.list(1,50)
        val oneToy = list[Random.nextInt(0,list.size)]
        //WHEN
        val expected = underTest.get(oneToy.id)
        //THEN
        assertThat(expected).isNotNull
        assertThat(expected).isEqualTo(oneToy)
    }

    @Test
    fun list() {
        //GIVEN
        val allToys = underTest.list(1,80)
        //WHEN
        val expected = underTest.list(1,80)
        //THEN
        assertThat(expected).isEqualTo(allToys)
    }
}