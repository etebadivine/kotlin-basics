package components.pets.dao

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Pets:Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 255).index()
    val gender: Column<String> = varchar("gender", 15)
    val sound: Column<String> = varchar("sound", 20)
    val species: Column<String> = varchar("species", 60)
    val colour: Column<String> = varchar("colour", 120)
    val age: Column<Int> = integer("age")
    val weight: Column<Double> = double("weight")
    val owner: Column<String> = varchar("owner", 160)
    override val primaryKey = PrimaryKey(id, name = "PK_Pets_ID")
}