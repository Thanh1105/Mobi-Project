package com.example.midterm.finalmobileproject.viewmodel

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

object APIHelper {
    private var retrofit: Retrofit? = null
    fun initApiService(url: String) : APIService{
        val apiService: APIService
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit!!.create(APIService::class.java)
    }
}