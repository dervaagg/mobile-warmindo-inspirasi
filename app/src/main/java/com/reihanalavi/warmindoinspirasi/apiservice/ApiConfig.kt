package com.reihanalavi.warmindoinspirasi.apiservice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    private const val baseUrl = "https://warmindo-setia-default-rtdb.firebaseio.com/"

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService(): ApiService{
        return getRetrofit().create(ApiService::class.java)
    }
}