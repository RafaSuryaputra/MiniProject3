package org.d3if3162.catizen.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.d3if3162.catizen.model.Kucing
import org.d3if3162.catizen.model.OpStatus
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

// private const val BASE_URL = " IPv4 Address kalian + :8000" untuk jalanin local dengan laravel

// untuk menyalakan server laravel dengan ngtok menggunakan : ngrok.exe http http://localhost:8000
private const val BASE_URL = "https://3975-2404-8000-1024-1a47-6c1f-8b6d-decc-399.ngrok-free.app"

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
        @Part("id") id: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("namaPemilik") namaPemilik: RequestBody,
        @Part("isUserInputted") isUserInputted: Int,
        @Part image: MultipartBody.Part
    ): OpStatus


    @DELETE("/hapus-kucing")
    suspend fun deleteHewan(
        @Header("Authorization") userId: String,
        @Query("id") id: String
    ): OpStatus
}

object KucingApi {
    val service: KucingApiService by lazy {
        retrofit.create(KucingApiService::class.java)
    }

    fun getKucingUrl(imageId: String): String {
        return "$BASE_URL$imageId"
    }
    enum class ApiStatus {LOADING, SUCCESS, FAILED}
}
