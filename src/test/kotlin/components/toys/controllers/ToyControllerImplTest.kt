package components.toys.controllers

import components.pets.models.Pet
import components.toys.dao.ToyDAO
import components.toys.models.Toy
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.TestInstance
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import uk.co.jemos.podam.api.PodamFactoryImpl
import java.sql.SQLException
import kotlin.random.Random


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ToyControllerImplTest {
    private lateinit var service: ToyDAO
    private lateinit  var di: DI
    private lateinit  var underTest: ToyControllerImpl
    private val factory: PodamFactoryImpl = PodamFactoryImpl()

    @BeforeAll
    fun setUp() {
        service = mockk(relaxed = true)
        di = DI{
            bindSingleton { service }
        }
        underTest = ToyControllerImpl(di)
    }

    @AfterAll
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `toys cannot be bought, details don't get recorded`() {
        //GIVEN
        val oneToy = factory.manufacturePojoWithFullData(Toy::class.java)
        every { service.create(any()) } returns 0
        //WHEN
        val expected = underTest.buy(oneToy)
        //THEN
        assertThat(expected.code).isEqualTo("01")
        assertThat(expected.systemCode).isEqualTo("TOY200")
    }

    @Test
    fun `toys cannot be bought when SQLException is thrown`(){
        //GIVEN
        val oneToy = factory.manufacturePojoWithFullData(Toy::class.java)
        every { service.create(any()) } throws SQLException()
        //WHEN
        val expected = underTest.buy(oneToy)
        //THEN
        assertThat(expected.code).isEqualTo("02")
        assertThat(expected.systemCode).isEqualTo("TOY500")
    }

    @Test
    fun `toys can be bought and get recorded` (){
        //GIVEN
        val buyToy = factory.manufacturePojoWithFullData(Toy::class.java)
        every { service.create(any()) } returns 1
        //WHEN
        val expected = underTest.buy(buyToy)
        //THEN
        assertThat(expected.code).isEqualTo("00")
        assertThat(expected.systemCode).isEqualTo("TOY201")
    }

    @Test
    fun `it cannot obtain toy info because id cannot be found`(){
        //GIVEN
        val randomID = Random.nextInt()
        every { service.get(any()) } returns null
        //WHEN
        val expected = underTest.obtainInfo(randomID)
        //THEN
        assertThat(expected.code).isEqualTo("00")
        assertThat(expected.systemCode).isEqualTo("TOY404")

    }

    @Test
    fun  `it cannot obtain toy info due to SQLException`(){
        //GIVEN
        val randomID = Random.nextInt()
        every { service.get(any()) } throws SQLException()
        //WHEN
        val expected = underTest.obtainInfo(randomID)
        //THEN
        assertThat(expected.code).isEqualTo("02")
        assertThat(expected.systemCode).isEqualTo("TOY500")
    }

    @Test
    fun obtainInfo() {
        //GIVEN
        val randomID = Random.nextInt()
        val oneToy = factory.manufacturePojoWithFullData(Toy::class.java)
        every { service.get(any()) } returns oneToy
        //WHEN
        val expected = underTest.obtainInfo(randomID)
        //THEN
        assertThat(expected.code).isEqualTo("00")
        assertThat(expected.systemCode).isEqualTo("TOY200")
        assertThat(expected.data).isNotEmpty
        assertThat(expected.data.size).isEqualTo(1)
        assertThat(expected.data.first()).isInstanceOf(Toy::class.java)

    }

    @Test
    fun `it cannot list items due to an SQLException`() {
        //GIVEN
        val startIndex = Random.nextInt()
        val numberOfRecords = Random.nextInt()
        every { service.list(any(),any()) } throws SQLException()
        //WHEN
        val expected = underTest.list(startIndex,numberOfRecords)
        //THEN
        assertThat(expected.code).isEqualTo("02")
        assertThat(expected.systemCode).isEqualTo("TOY500")
    }

    @Test
    fun `it returns and empty list when no data is found in the database`(){
        //GIVEN
        val startIndex = Random.nextInt()
        val numberOfRecords = Random.nextInt()
        val listOfToys:List<Toy> = listOf()
        every { service.list(any(),any()) } returns listOfToys
        //WHEN
        val expected = underTest.list(startIndex,numberOfRecords)
        //THEN
        assertThat(expected.code).isEqualTo("00")
        assertThat(expected.systemCode).isEqualTo("TOY204")
        assertThat(expected.data).isEmpty()
    }

    @Test
    fun filter() {
    }
}