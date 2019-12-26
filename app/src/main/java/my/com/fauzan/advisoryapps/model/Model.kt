package my.com.fauzan.advisoryapps.model

import com.google.gson.annotations.SerializedName
import my.com.fauzan.advisoryapps.utils.Constants
import java.util.ArrayList

object Model {

    // Login
    data class LoginResponse(
        val callStatus: Constants.Progress,
        @SerializedName("id") val id: String?, @SerializedName("token")
        val token: String?, @SerializedName("status") val status: Status?
    )


    data class Status(
        @SerializedName("code") val code: Int,
        @SerializedName("message") val message: String
    )

    //Listing
    data class ListingResponse(
        val callStatus: Constants.Progress,
        @SerializedName("listing") val list: ArrayList<Person>,
        @SerializedName("status") val status: Status?
    )

    //Update
    data class UpdateResponse(
        val callStatus: Constants.Progress,
        @SerializedName("status") val status: Status?
    )
    data class Person(
        @SerializedName("id") val id: Int, @SerializedName("list_name") val name: String,
        @SerializedName("distance") val distance: String
    )

    // Store data
    data class UserData(val id: String?, val token: String?)

//    data class Result(@SerializedName("id") val id: String, @SerializedName("token") val token:String,
//                      @SerializedName("status") val status:Any, @SerializedName("message") val message: String)

}