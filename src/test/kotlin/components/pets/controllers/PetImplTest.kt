package components.pets.controllers

import components.pets.dao.PetDAO
import components.pets.models.Pet
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindSingleton
import org.kodein.di.singleton
import uk.co.jemos.podam.api.PodamFactoryImpl

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PetImplTest {
    private lateinit var service: PetDAO
    private lateinit  var di: DI
    private lateinit  var underTest:PetImpl
    private val factory: PodamFactoryImpl = PodamFactoryImpl()

    @BeforeAll
    fun setUp() {
        service = mockk(relaxed = true)
        di = DI{
            bindSingleton { service }
        }
        underTest = PetImpl(di)
    }

    @AfterAll
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `it cannot buy details don't get recorded`() {
        //GIVEN
        val onePet = factory.manufacturePojoWithFullData(Pet::class.java)
        every { service.create(any()) } returns 0
        //WHEN
        val expected = underTest.buy(onePet)
        //THEN
        assertThat(expected.code).isEqualTo("01")
        assertThat(expected.systemCode).isEqualTo("PET200")
    }

    @Test
    fun buy() {
    }

    @Test
    fun obtainInfo() {
    }

    @Test
    fun play() {
    }

    @Test
    fun sell() {
    }

    @Test
    fun list() {
    }

    @Test
    fun filter() {
    }
}