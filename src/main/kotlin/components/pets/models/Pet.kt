package components.pets.models

data class Pet(
    val id:Int,
    val name:String,
    val gender:String,
    val sound:String,
    var age:Int,
    var weight:Double,
    val species:String,
    val colour:String,
    val owner:String,
)
