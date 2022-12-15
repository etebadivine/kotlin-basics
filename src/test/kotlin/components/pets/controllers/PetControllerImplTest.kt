package components.pets.controllers

import components.pets.dao.PetDAO
import components.pets.models.Pet
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import uk.co.jemos.podam.api.PodamFactoryImpl
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PetControllerImplTest {
    private lateinit var service: PetDAO
    private lateinit  var di: DI
    private lateinit  var underTest:PetControllerImpl
    private val factory: PodamFactoryImpl = PodamFactoryImpl()

    @BeforeAll
    fun setUp() {
        service = mockk(relaxed = true)
        di = DI{
            bindSingleton { service }
        }
        underTest = PetControllerImpl(di)
    }

    @AfterAll
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `it cannot buy when details don't get recorded`() {
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
    fun `it cannot buy when details SQLException is thrown`() {
        //TODO: Assignment
    }

    @Test
    fun `it can buy a pet`() {
        //GIVEN
        val onePet = factory.manufacturePojoWithFullData(Pet::class.java)
        every { service.create(any()) } returns 1
        //WHEN
        val expected = underTest.buy(onePet)
        //THEN
        assertThat(expected.code).isEqualTo("00")
        assertThat(expected.systemCode).isEqualTo("PET201")
    }

    @Test
    fun `it cannot obtain pet info due to id not found`() {
        //GIVEN
        val randomID = Random.nextInt()
        every { service.get(any()) } returns null
        //WHEN
        val expected = underTest.obtainInfo(randomID)
        //THEN
        assertThat(expected.code).isEqualTo("00")
        assertThat(expected.systemCode).isEqualTo("PET404")
    }

    @Test
    fun `it cannot obtain pet info due SQLException`() {
        //TODO: Assignment
    }

    @Test
    fun obtainInfo() {
        //GIVEN
        val randomID = Random.nextInt()
        val onePet = factory.manufacturePojoWithFullData(Pet::class.java)
        every { service.get(any()) } returns onePet
        //WHEN
        val expected = underTest.obtainInfo(randomID)
        //THEN
        assertThat(expected.code).isEqualTo("00")
        assertThat(expected.systemCode).isEqualTo("PET200")
        assertThat(expected.data).isNotEmpty
        assertThat(expected.data.size).isEqualTo(1)
        assertThat(expected.data.first()).isInstanceOf(Pet::class.java)
    }

    @Test
    fun `it cannot play when pet info not found due to wrong id`() {
        //GIVEN
        val randomID = Random.nextInt()
        every { service.get(any()) } returns null
        //WHEN
        val expected = underTest.play(randomID)
        //THEN
        assertThat(expected.code).isEqualTo("00")
        assertThat(expected.systemCode).isEqualTo("PET404")
    }

    @Test
    fun `it cannot play when pet info not found due SQLException`() {
        //TODO: Assignment
    }

    @Test
    fun `it can play when pet info found`() {
        //GIVEN
        val randomID = Random.nextInt()
        val onePet = factory.manufacturePojoWithFullData(Pet::class.java)
        every { service.get(any()) } returns onePet
        //WHEN
        val expected = underTest.play(randomID)
        //THEN
        assertThat(expected.code).isEqualTo("00")
        assertThat(expected.systemCode).isEqualTo("PET201")
        assertThat(expected.message).contains(onePet.name)
        assertThat(expected.message).contains(onePet.sound)
        println(expected.message)
    }

    @Test
    fun sell() {
        //TODO: Assignment - all cases
    }

    @Test
    fun `it cannot list due to an SQLException`() {
        //TODO:Assignment
    }

    @Test
    fun `it returns an empty list when no data found in database`() {
        //GIVEN
        val startIndex = Random.nextInt()
        val numberOfRecords = Random.nextInt()
        val listOfPets:List<Pet> = listOf()
        every { service.list(any(),any()) } returns listOfPets
        //WHEN
        val expected = underTest.list(startIndex,numberOfRecords)
        //THEN
        assertThat(expected.code).isEqualTo("00")
        assertThat(expected.systemCode).isEqualTo("PET204")
        assertThat(expected.data).isEmpty()
    }

    @Test
    fun `it calls dao list() with default of 1 as start index when not defined`() {
        //GIVEN
        val numberOfRecords = Random.nextInt(11,999999)
        val maxRecords = Random.nextInt(1,10)
        /*val listOfPets: MutableList<Pet> = mutableListOf()
        for (i in 1..maxRecords){
            listOfPets.add(factory.manufacturePojoWithFullData(Pet::class.java))
        }*/
        every { service.list(any(),any()) } returns listOf()
        //WHEN
        val expected = underTest.list(null,numberOfRecords)
        //THEN
        verify { service.list(1,numberOfRecords) }
    }

    @Test
    fun `it calls dao list() with default of 50 as number of records when not defined`() {
        //GIVEN
        val startIndex = Random.nextInt()
        every { service.list(any(),any()) } returns listOf()
        //WHEN
        val expected = underTest.list(startIndex,null)
        //THEN
        verify { service.list(startIndex,50) }
    }

    @Test
    fun `it calls dao list() with default of 1 and 50 as start index and number of records when none are defined`() {
        //GIVEN
        /*val listOfPets: MutableList<Pet> = mutableListOf()
        for (i in 1..maxRecords){
            listOfPets.add(factory.manufacturePojoWithFullData(Pet::class.java))
        }*/
        every { service.list(any(),any()) } returns listOf()
        //WHEN
        val expected = underTest.list(null,null)
        //THEN
        verify { service.list(1,50) }
    }

    @Test
    fun `it returns a list when data found in database`() {
        //GIVEN
        val startIndex = Random.nextInt()
        val numberOfRecords = Random.nextInt()
        val maxRecords = Random.nextInt(1,10)
        val listOfPets: MutableList<Pet> = mutableListOf()
        for (i in 1..maxRecords){
            listOfPets.add(factory.manufacturePojoWithFullData(Pet::class.java))
        }
        every { service.list(any(),any()) } returns listOfPets
        //WHEN
        val expected = underTest.list(startIndex,numberOfRecords)
        //THEN
        assertThat(expected.code).isEqualTo("00")
        assertThat(expected.systemCode).isEqualTo("PET206")
        assertThat(expected.data).isNotEmpty
    }

    @Test
    fun filter() {
        //TODO:Assignment
    }
}