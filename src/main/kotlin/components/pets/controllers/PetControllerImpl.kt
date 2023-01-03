package components.pets.controllers

import components.pets.dao.PetDAO
import components.pets.models.Pet
import components.toys.models.Toy
import core.shared.APIResponse
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.slf4j.LoggerFactory
import java.sql.SQLException

class PetControllerImpl(override val di: DI) :PetController,DIAware {

    private val petDao:PetDAO by instance()
    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Create records in the Pet database
     * */
    override fun buy(pet: Pet): APIResponse<String> {
        val response: APIResponse<String> = try {
            val count = petDao.create(pet)
            if(count>0){
                APIResponse("PET201","00","Pet details recorded successfully",listOf())
            }else{
                APIResponse("PET200","01","Pet details failed to be recorded.",listOf())
            }
        }catch (se:SQLException){
            log.warn("Failed to store new pet details",se)
            APIResponse("PET500","02","An error occurred when adding new pet. ${se.message}",listOf())
        }
        return response
    }

    /**
     *Get all information on one pet
     * */
    override fun obtainInfo(petID: Int): APIResponse<Pet> {
        val response: APIResponse<Pet> = try {
            val onePet = petDao.get(petID)
            if(onePet !=null){
                APIResponse("PET200","00","Pet details obtained.",listOf(onePet))
            }else{
                APIResponse("PET404","00","Pet not found.",listOf())
            }
        }catch (se:SQLException){
            log.warn("Failed to obtain pet details",se)
            APIResponse("PET500","02","An error occurred when getting pet details. ${se.message}",listOf())
        }
        return response
    }

    /**
     *Makes some noise
     * */
    override fun play(petID: Int): APIResponse<String> {
        val response: APIResponse<String> = try {
            val onePet = petDao.get(petID)
            if(onePet !=null){
                APIResponse("PET201","00","${onePet.name} is making a sound: ${onePet.sound}",listOf())
            }else{
                APIResponse("PET404","00","Pet not found.",listOf())
            }
        }catch (se:SQLException){
            log.warn("Failed to get pet details",se)
            APIResponse("PET500","02","An error occurred when getting pet details. ${se.message}",listOf())
        }
        return response
    }

    /**
     *Change ownership of the pet
     * */
    override fun sell(petID: Int, newOwnerName: String): APIResponse<String> {
        val response: APIResponse<String> = try {
            val onePet = petDao.get(petID)
            if(onePet !=null){
                onePet.owner = newOwnerName
                if(petDao.update(onePet)>0){
                    APIResponse("PET201","00","${onePet.name}'s owner has been changed",listOf())
                }else{
                    APIResponse("PET200","00","${onePet.name}'s owner name has not been changed.",listOf())
                }
            }else{
                APIResponse("PET404","00","Pet not found.",listOf())
            }
        }catch (se:SQLException){
            log.warn("Failed to change pet details",se)
            APIResponse("PET500","02","An error occurred when processing sale. ${se.message}",listOf())
        }
        return response
    }

    /**
     *list all pets
     * */
    override fun list(startIndex: Int?, numberOfRecords: Int?): APIResponse<Pet> {
        val start = startIndex ?: 1
        val size = numberOfRecords ?: 50
        val response: APIResponse<Pet> = try {
           val listOfPets = petDao.list(start,size)
            if(listOfPets.isNotEmpty()){
                APIResponse("PET206","00","content found",listOfPets)
            }else{
                APIResponse("PET204","00","No Pets not found.",listOf())
            }
        }catch (se:SQLException){
            log.warn("Failed to get list requested",se)
            APIResponse("PET500","02","An error occurred when listing pets. ${se.message}",listOf())
        }
        return response
    }

    /**
     *filter list of pets
     * */
    override fun filter(startIndex: Int?, numberOfRecords: Int?, name: String?, species: String?): APIResponse<Pet> {
        val start = startIndex ?: 1
        val size = numberOfRecords ?: 50
        val response: APIResponse<Pet> = try {
            val filteredPets = petDao.list(start, size, name, species)
            if(filteredPets.isNotEmpty()){
                APIResponse("PET206","00","content found",filteredPets)
            }else{
                APIResponse("PET204","00","Pet not found.",listOf())
            }
        }catch (se: SQLException){
            log.warn("Failed to get list requested",se)
            APIResponse("PET500","02","An error occurred when filtering pets. ${se.message}",listOf())
        }
        return response
    }
}