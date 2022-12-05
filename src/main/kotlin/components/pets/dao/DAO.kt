package components.pets.dao

import components.pets.models.Pet

interface DAO {

    /**
     * creates a new record in the PETS table
     * */
    fun create(pet: Pet):Int

    /**
     * update an existing pet record
     * */
    fun update(pet: Pet):Int

    /**
     * delete an existing pet record
     * */
    fun delete(id: Int):Int

    /**
     * get one  pet record
     * */
    fun get(id: Int):Pet

    /**
     * get a list of  pets
     * */
    fun list(startIndex: Int,size:Int):List<Pet>

    /**
     * get a list of  pets
     * */
    fun list(startIndex: Int,size:Int,name:String?,species:String?):List<Pet>




}