package components.toys.dao

import components.pets.dao.Pets
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Toys: Table() {
    val id: Column<Int> = Toys.integer("id").autoIncrement()
    val name: Column<String> = Toys.varchar("name", 255).index()
    val colour: Column<String> = Toys.varchar("colour", 120).index()
    val manufacturer: Column<String> = Toys.varchar("manufacturer", 120).index()
    val owner: Column<String> = varchar("owner", 160)
    override val primaryKey = PrimaryKey(Pets.id, name = "PK_Toys_ID")

}