package com.example.paysrest.data

import com.example.paysrest.model.Country
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CountryApi {
    @GET("v3.1/all")
    suspend fun getAllCountries(@Query("fields") fields: String = "name,flags,capital,population,continents"): List<Country>

    @GET("v3.1/region/{region}")
    suspend fun getCountriesByRegion(
        @Path("region") region: String,
        @Query("fields") fields: String = "name,flags,capital,population,continents"
    ): List<Country>

    companion object {
        fun create(): CountryApi {
            return Retrofit.Builder()
                .baseUrl("https://restcountries.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CountryApi::class.java)
        }
    }
}
