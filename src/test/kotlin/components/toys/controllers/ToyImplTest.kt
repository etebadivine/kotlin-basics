package components.toys.controllers

import components.toys.dao.ToyDAO
import components.toys.models.Toy
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.annotations.NotNull

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.TestInstance
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import uk.co.jemos.podam.api.PodamFactoryImpl
import kotlin.random.Random


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ToyImplTest {
    private lateinit var service: ToyDAO
    private lateinit  var di: DI
    private lateinit  var underTest: ToyImpl
    private val factory: PodamFactoryImpl = PodamFactoryImpl()

    @BeforeAll
    fun setUp() {
        service = mockk(relaxed = true)
        di = DI{
            bindSingleton { service }
        }
        underTest = ToyImpl(di)
    }

    @AfterAll
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `it cannot buy, details don't get recorded`() {
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
    fun obtainInfo() {

    }

    @Test
    fun list() {
    }

    @Test
    fun filter() {
    }

    @Test
    fun getDi() {
    }
}