package org.d3if3162.mobpro1.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if3162.mobpro1.model.Kucing
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://raw.githubusercontent.com/" +
        "RafaSuryaputra/static-api/main/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface KucingApiService {
    @GET("static-api.json")
    suspend fun getKucing(): List<Kucing>
}

object KucingApi {
    val service: KucingApiService by lazy {
        retrofit.create(KucingApiService::class.java)
    }

    fun getKucingUrl(imageId: String): String {
        return "$BASE_URL$imageId.jpg"
    }
}
