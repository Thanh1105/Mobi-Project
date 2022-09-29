package com.example.midterm.finalmobileproject.viewmodel.data

import com.google.gson.annotations.SerializedName

data class CountryInfo(
    @SerializedName("_id") var Id : Int,
    @SerializedName("iso2") var iso2 : String,
    @SerializedName("iso3") var iso3 : String,
    @SerializedName("lat") var lat : Int,
    @SerializedName("long") var long : Double,
    @SerializedName("flag") var flag : String
)