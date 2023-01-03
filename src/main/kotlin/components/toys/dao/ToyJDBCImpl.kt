package components.toys.dao

import components.pets.dao.Pets
import components.pets.models.Pet
import components.toys.models.Toy
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ToyJDBCImpl:ToyDAO {

    /**
     * creates a new record in the TOYS table
     * */
    override fun create(toy: Toy): Int = transaction {
        Toys.insert {
            it[name] = toy.name
            it[colour] = toy.colour
            it[manufacturer] = toy.manufacturer
            it[owner] = toy.owner
        }.insertedCount
    }

    /**
     * update an existing toy record
     * */
    override fun update(toy: Toy): Int = transaction {
        Toys.update({ Toys.id eq toy.id }) {
            it[owner] = toy.owner
        }
    }

    /**
     * delete an existing toy record
     * */
    override fun delete(id: Int): Int = transaction {
        Toys.deleteWhere { Toys.id eq id }
    }

    /**
     * get one  toy record
     * */
    override fun get(id: Int): Toy? = transaction {
        Toys.selectAll().andWhere { Toys.id eq id }.map {
            Toy(
                id = it[Toys.id],
                name = it[Toys.name],
                colour = it[Toys.colour],
                manufacturer = it[Toys.manufacturer],
                owner = it[Toys.owner],

                )
        }.singleOrNull()
    }

    /**
     * get a list of  toys
     * */
    override fun list(startIndex: Int, size: Int): List<Toy> = transaction {
        val query = Toys.selectAll()
        query.limit(size, ((startIndex - 1) * size).toLong())
        query.map {
            Toy(
                id = it[Toys.id],
                name = it[Toys.name],
                colour = it[Toys.colour],
                owner = it[Toys.owner],
                manufacturer = it[Toys.manufacturer]
            )
        }
    }

    override fun list(startIndex: Int, size: Int, name: String?): List<Toy> = transaction{
        val query = Toys.selectAll()
        name?.let {
            query.andWhere { Toys.name eq it }
        }

        query.limit(size, ((startIndex - 1) * size).toLong())
        query.map {
            Toy(
                id = it[Toys.id],
                name = it[Toys.name],
                colour = it[Toys.colour],
                owner = it[Toys.owner],
                manufacturer = it[Toys.manufacturer]
            )
        }
    }
}


