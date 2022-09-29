package com.example.midterm.finalmobileproject.viewmodel;

import com.example.midterm.finalmobileproject.viewmodel.data.CovidVietNam
import com.example.midterm.finalmobileproject.viewmodel.data.CovidWW
import com.example.midterm.finalmobileproject.viewmodel.data.Province
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @get:GET("countries/vn")
    val getCovidVietNam: Call<CovidVietNam>

    @get:GET("all")
    val getCovidWW: Call<CovidWW>

    @get:GET("p")
    val getProvince: Call<List<Province>>

    @GET("p/{id}")
    fun getDistricts(
        @Path("id") provinceID: Int,
        @Query("depth") responseType: Int
    ): Call<Province>
}