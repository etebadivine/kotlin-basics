package components.toys.controllers

import components.toys.models.Toy
import core.shared.APIResponse

interface ToyController {

    /**
     * Create records in the Toy database
     * */
    fun buy(toy: Toy): APIResponse<String>

    /**
     *Get all information on one toy
     * */
    fun obtainInfo(toyID: Int): APIResponse<Toy>

    /**
     *list all toys
     * */
    fun list(startIndex:Int?,numberOfRecords:Int?): APIResponse<Toy>

    /**
     *filter list of toys
     * */
    fun filter(startIndex:Int?=1,numberOfRecords:Int?=50,name:String?): APIResponse<Toy>

}