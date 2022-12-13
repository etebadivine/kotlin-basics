package components.toys.controllers

import components.toys.dao.ToyDAO
import components.toys.models.Toy
import core.shared.APIResponse
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.slf4j.LoggerFactory
import java.sql.SQLException

class ToyImpl(override val di: DI): ToyController, DIAware {

    private val toyDao: ToyDAO by instance()
    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Create records in the Toy database
     * */
    override fun buy(toy: Toy): APIResponse<String> {
        val response: APIResponse<String> = try {
            val count = toyDao.create(toy)
            if(count>0){
                APIResponse("TOY201","00","Toy details recorded successfully",listOf())
            }else{
                APIResponse("TOY200","01","Toy details failed to be recorded.",listOf())
            }
        }catch (se: SQLException){
            log.warn("Failed to store new toy details",se)
            APIResponse("TOY500","02","An error occurred when adding new toy. ${se.message}",listOf())
        }
        return response
    }

    /**
     *Get all information on one toy
     * */
    override fun obtainInfo(toyID: Int): APIResponse<Toy> {
        val response: APIResponse<Toy> = try {
            val oneToy = toyDao.get(toyID)
            if(oneToy !=null){
                APIResponse("TOY200","00","Toy details obtained.",listOf(oneToy))
            }else{
                APIResponse("TOY404","00","Toy not found.",listOf())
        }
        }catch (se: SQLException){
            log.warn("Failed to obtain toy details",se)
            APIResponse("TOY500","02","An error occurred when getting toy details. ${se.message}",listOf())
        }
        return response
    }

    /**
     *list all toys
     * */
    override fun list(startIndex: Int?, numberOfRecords: Int?): APIResponse<Toy> {
        val start = startIndex ?: 1
        val size = numberOfRecords ?: 50
        val response: APIResponse<Toy> = try {
            val listOfToys = toyDao.list(start,size)
            if(listOfToys.isNotEmpty()){
                APIResponse("TOY206","00","content found",listOfToys)
            }else{
                APIResponse("TOY204","00","Toy not found.",listOf())
            }
        }catch (se: SQLException){
            log.warn("Failed to get list requested",se)
            APIResponse("TOY500","02","An error occurred when listing toys. ${se.message}",listOf())
        }
        return response
    }

    /**
     *filter list of toys
     * */
    override fun filter(startIndex: Int?, numberOfRecords: Int?, name: String?, species: String?): APIResponse<Toy> {
        TODO("Not yet implemented")
    }
}
