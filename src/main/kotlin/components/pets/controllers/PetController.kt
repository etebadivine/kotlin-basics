package components.pets.controllers

import components.pets.models.Pet
import core.shared.APIResponse

interface PetController {

    /**
     * Create records in the Pet database
     * */
    fun buy(pet:Pet):APIResponse<String>

    /**
     *Get all information on one pet
     * */
    fun obtainInfo(petID: Int):APIResponse<Pet>

    /**
     *Makes some noise
     * */
    fun play(petID: Int):APIResponse<String>

    /**
     *Change ownership of the pet
     * */
    fun sell(petID:Int,newOwner:String):APIResponse<String>

    /**
     *list all pets
     * */
    fun list(startIndex:Int?,numberOfRecords:Int?):APIResponse<Pet>

    /**
     *filter list of pets
     * */
    fun filter(startIndex:Int?=1,numberOfRecords:Int?=50,name:String?,species:String?):APIResponse<Pet>
}