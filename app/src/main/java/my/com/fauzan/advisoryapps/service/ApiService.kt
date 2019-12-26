package my.com.fauzan.advisoryapps.service

import io.reactivex.Observable
import my.com.fauzan.advisoryapps.BuildConfig
import my.com.fauzan.advisoryapps.model.Model
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    @POST("/login")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @FormUrlEncoded
    fun hitLogin(@Field("email") email: String, @Field("password") password: String): Observable<Response<Model.LoginResponse>>

    @GET("/listing")
    fun hitList(@Query("id") id: String, @Query("token") token: String): Observable<Response<Model.ListingResponse>>

    @POST("/listing/update")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @FormUrlEncoded
    fun hitUpdate(
        @Field("id") id: String, @Field("token") token: String,
        @Field("listing_id") listingID: String, @Field("listing_name") listingName: String,
        @Field("distance") distance: String
    ): Observable<Response<Model.UpdateResponse>>

    companion object {
        fun create(): ApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val httpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            httpClientBuilder.readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) httpClientBuilder.addInterceptor(interceptor)

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                //               .baseUrl("https://jsonplaceholder.typicode.com")
                .baseUrl("https://interview.advisoryapps.com")
                .client(httpClientBuilder.build())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }

}