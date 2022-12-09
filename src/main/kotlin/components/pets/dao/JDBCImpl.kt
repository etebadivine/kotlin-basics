package components.pets.dao

import components.pets.dao.Pets.age
import components.pets.dao.Pets.colour
import components.pets.dao.Pets.gender
import components.pets.dao.Pets.name
import components.pets.dao.Pets.owner
import components.pets.dao.Pets.sound
import components.pets.dao.Pets.species
import components.pets.dao.Pets.weight
import components.pets.models.Pet
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class JDBCImpl:PetDAO {

    /**
     * creates a new record in the PETS table
     * */
    override fun create(pet: Pet): Int = transaction {
        Pets.insert{
            it[name] = pet.name
            it[gender] = pet.gender
            it[sound] = pet.sound
            it[species] = pet.species
            it[colour] = pet.colour
            it[age] = pet.age
            it[weight] = pet.weight
            it[owner] = pet.owner
        }.insertedCount
    }

    /**
     * update an existing pet record
     * */
    override fun update(pet: Pet): Int = transaction {
        Pets.update({Pets.id eq pet.id}){
            it[age] = pet.age
            it[weight] = pet.weight
        }
    }

    /**
     * delete an existing pet record
     * */
    override fun delete(id: Int): Int = transaction{
        Pets.deleteWhere { Pets.id eq id }
    }

    /**
     * get one  pet record
     * */
    override fun get(id: Int): Pet? = transaction {
        Pets.selectAll().andWhere { Pets.id eq id }.map {
            Pet(
                id = it[Pets.id],
                name = it[name],
                gender = it[gender],
                sound = it[sound],
                age = it[age],
                weight =  it[weight],
                colour = it[colour],
                owner = it[owner],
                species = it[species]
            )
        }.singleOrNull()
    }

    /**
     * get a list of  pets
     * */
    override fun list(startIndex: Int, size: Int): List<Pet> = transaction {
        val query = Pets.selectAll()
        query.limit(size,((startIndex - 1)* size).toLong())
        query.map {
            Pet(
                id = it[Pets.id],
                name = it[name],
                gender = it[gender],
                sound = it[sound],
                age = it[age],
                weight =  it[weight],
                colour = it[colour],
                owner = it[owner],
                species = it[species]
            )
        }
    }

    /**
     * get a list of  pets
     * */
    override fun list(startIndex: Int, size: Int, name: String?, species: String?): List<Pet> = transaction {
        val query = Pets.selectAll()
        name?.let {
            query.andWhere { Pets.name eq it }
        }
        species?.let {
            query.andWhere { Pets.species eq it }
        }
        query.limit(size,((startIndex - 1)* size).toLong())
        query.map {
            Pet(
                id = it[Pets.id],
                name = it[Pets.name],
                gender = it[gender],
                sound = it[sound],
                age = it[age],
                weight =  it[weight],
                colour = it[colour],
                owner = it[owner],
                species = it[Pets.species]
            )
        }
    }
}