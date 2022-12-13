package components.toys.dao

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Toys: Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 255).index()
    val colour: Column<String> = varchar("colour", 120)
    val manufacturer: Column<String> = varchar("manufacturer", 120)
    val owner: Column<String> = varchar("owner", 160)
    override val primaryKey = PrimaryKey(id, name = "PK_Toys_ID")

}