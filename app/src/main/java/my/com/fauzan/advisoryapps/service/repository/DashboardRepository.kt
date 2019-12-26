package my.com.fauzan.advisoryapps.service.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import my.com.fauzan.advisoryapps.App
import my.com.fauzan.advisoryapps.utils.Constants
import my.com.fauzan.advisoryapps.model.Model
import my.com.fauzan.advisoryapps.service.ApiService
import retrofit2.Response
import java.util.ArrayList

class DashboardRepository(private val apiService: ApiService) {
    private val listingData = MutableLiveData<Model.ListingResponse>()
    private val updateData = MutableLiveData<Model.UpdateResponse>()

    companion object {
        @Volatile
        private var instance: DashboardRepository? = null

        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: DashboardRepository(apiService).also { instance = it }
            }
    }

    fun hitListing() {
        val mutableList = mutableListOf<Model.Person>()
        listingData.value = Model.ListingResponse(
            Constants.Progress.LOADING,
            mutableList as ArrayList<Model.Person>, null
        )

        val userData: Model.UserData =
            Gson().fromJson(App.prefs?.userData, Model.UserData::class.java)

        if (userData.id != null && userData.token != null) {
            apiService.hitList(userData.id, userData.token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    object : DisposableObserver<Response<Model.ListingResponse>>() {
                        override fun onComplete() {

                        }

                        override fun onNext(t: Response<Model.ListingResponse>) {
                            if (t.isSuccessful) {
                                if (t.body() != null) {
                                    if (t.body()!!.status?.code == 200) {
                                        mutableList.addAll(t.body()!!.list)
                                        listingData.value =
                                            Model.ListingResponse(
                                                Constants.Progress.SUCCESSFUL,
                                                mutableList,
                                                t.body()!!.status
                                            )
                                    } else {
                                        listingData.value =
                                            Model.ListingResponse(
                                                Constants.Progress.SUCCESSFUL,
                                                mutableList,
                                                t.body()!!.status
                                            )
                                    }
                                }
                            }
                        }

                        override fun onError(e: Throwable) {
                            listingData.value =
                                Model.ListingResponse(
                                    Constants.Progress.FAILED,
                                    mutableList,
                                    e.message?.let { Model.Status(0, it) }
                                )
                        }

                    }
                )
        }
    }

    fun getListingData() = listingData as LiveData<Model.ListingResponse>

    fun hitUpdateData(person: Model.Person) {
        updateData.value = Model.UpdateResponse(Constants.Progress.LOADING, null)

        val userData: Model.UserData =
            Gson().fromJson(App.prefs?.userData, Model.UserData::class.java)

        if (userData.id != null && userData.token != null) {
            apiService.hitUpdate(userData.id, userData.token, person.id.toString(), person.name, person.distance).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    object : DisposableObserver<Response<Model.UpdateResponse>>() {
                        override fun onComplete() {

                        }

                        override fun onNext(t: Response<Model.UpdateResponse>) {
                            if (t.isSuccessful) {
                                if (t.body() != null) {
                                    if (t.body()!!.status?.code == 200) {
                                        updateData.value = Model.UpdateResponse(
                                            Constants.Progress.SUCCESSFUL, t.body()!!.status)
                                    } else {
                                        updateData.value = Model.UpdateResponse(
                                            Constants.Progress.FAILED, t.body()!!.status)
                                    }
                                }
                            }
                        }

                        override fun onError(e: Throwable) {
                            updateData.value =
                                Model.UpdateResponse(
                                    Constants.Progress.FAILED,
                                     e.message?.let { Model.Status(0, it) }
                                )
                        }
                    }
                )
        }
    }

    fun getUpdateData() = updateData as LiveData<Model.UpdateResponse>
}