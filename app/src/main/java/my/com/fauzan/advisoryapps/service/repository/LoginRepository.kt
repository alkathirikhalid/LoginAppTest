package my.com.fauzan.advisoryapps.service.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import my.com.fauzan.advisoryapps.utils.Constants
import my.com.fauzan.advisoryapps.model.Model
import my.com.fauzan.advisoryapps.service.ApiService
import retrofit2.Response

class LoginRepository (private val apiService:ApiService){

    companion object {
        @Volatile private var instance: LoginRepository? = null

        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: LoginRepository(apiService).also { instance = it }
            }
    }

    private val data = MutableLiveData<Model.LoginResponse>()

    fun hitLogin(email: String, password: String) {
        data.value = Model.LoginResponse(Constants.Progress.LOADING, null, null, null)

        apiService.hitLogin(email, password).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                object : DisposableObserver<Response<Model.LoginResponse>>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: Response<Model.LoginResponse>) {
                        if (t.isSuccessful) {
                            if (t.body() != null){
                                data.value = Model.LoginResponse(
                                    Constants.Progress.SUCCESSFUL,
                                    t.body()?.id, t.body()?.token, t.body()?.status)
                            }

                        }
                    }

                    override fun onError(e: Throwable) {
                        data.value = Model.LoginResponse(
                            Constants.Progress.FAILED,null, null,
                            e.message?.let { Model.Status(0, it) })
                    }

                }
            )
    }

    fun getData() = data as LiveData<Model.LoginResponse>

}