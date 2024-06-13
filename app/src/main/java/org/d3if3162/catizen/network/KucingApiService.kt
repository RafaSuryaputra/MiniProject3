package org.d3if3162.catizen.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.d3if3162.catizen.model.Kucing
import org.d3if3162.catizen.model.OpStatus
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

//private const val BASE_URL = "https://ghastly-delicate-dragon.ngrok-free.app/API/46-04/static-api-roy/"
private const val BASE_URL = "http://172.20.10.8:8000"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface KucingApiService {
    @GET("/kucing")
    suspend fun getKucing(
        @Header("Authorization") userId: String
    ): List<Kucing>

    @Multipart
    @POST("/bikin-kucing")
    suspend fun postKucing(
        @Header("Authorization") userId: String,
        @Part("nama") nama: RequestBody,
        @Part("namaPemilik") namaPemilik: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus
}

object KucingApi {
    val service: KucingApiService by lazy {
        retrofit.create(KucingApiService::class.java)
    }

    fun getKucingUrl(imageId: String): String {
        return "$BASE_URL$imageId"
    }
}

enum class ApiStatus {LOADING, SUCCESS, FAILED}
