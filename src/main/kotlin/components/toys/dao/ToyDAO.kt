package components.toys.dao

import components.toys.models.Toy

interface ToyDAO {
    /**
     * creates a new record in the TOYS table
     * */
    fun create(toy: Toy):Int

    /**
     * update an existing toy record
     * */
    fun update(toy: Toy):Int

    /**
     * delete an existing toy record
     * */
    fun delete(id: Int):Int

    /**
     * get one  toy record
     * */
    fun get(id: Int): Toy?

    /**
     * get a list of  toys
     * */
    fun list(startIndex: Int,size:Int):List<Toy>
}