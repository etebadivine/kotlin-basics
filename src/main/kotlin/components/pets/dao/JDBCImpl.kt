package components.pets.dao

import components.pets.models.Pet

class JDBCImpl:DAO {

    /**
     * creates a new record in the PETS table
     * */
    override fun create(pet: Pet): Int {
        TODO("Not yet implemented")
    }

    /**
     * update an existing pet record
     * */
    override fun update(pet: Pet): Int {
        TODO("Not yet implemented")
    }

    /**
     * delete an existing pet record
     * */
    override fun delete(id: Int): Int {
        TODO("Not yet implemented")
    }

    /**
     * get one  pet record
     * */
    override fun get(id: Int): Pet {
        TODO("Not yet implemented")
    }

    /**
     * get a list of  pets
     * */
    override fun list(startIndex: Int, size: Int): List<Pet> {
        TODO("Not yet implemented")
    }

    /**
     * get a list of  pets
     * */
    override fun list(startIndex: Int, size: Int, name: String?, species: String?): List<Pet> {
        TODO("Not yet implemented")
    }
}