package com.example.midterm.finalmobileproject.viewmodel.data

import com.google.gson.annotations.SerializedName

data class CovidWW (
    @SerializedName("updated") var updated : Long,
    @SerializedName("cases") var cases : Long,
    @SerializedName("todayCases") var todayCases : Long,
    @SerializedName("deaths") var deaths : Long,
    @SerializedName("todayDeaths") var todayDeaths : Long,
    @SerializedName("recovered") var recovered : Long,
    @SerializedName("todayRecovered") var todayRecovered : Long,
    @SerializedName("active") var active : Long,
    @SerializedName("critical") var critical : Long,
    @SerializedName("casesPerOneMillion") var casesPerOneMillion : Long,
    @SerializedName("deathsPerOneMillion") var deathsPerOneMillion : Double,
    @SerializedName("tests") var tests : Long,
    @SerializedName("testsPerOneMillion") var testsPerOneMillion : Double,
    @SerializedName("population") var population : Long,
    @SerializedName("oneCasePerPeople") var oneCasePerPeople : Long,
    @SerializedName("oneDeathPerPeople") var oneDeathPerPeople : Long,
    @SerializedName("oneTestPerPeople") var oneTestPerPeople : Long,
    @SerializedName("undefined") var undefined : Long,
    @SerializedName("activePerOneMillion") var activePerOneMillion : Double,
    @SerializedName("recoveredPerOneMillion") var recoveredPerOneMillion : Double,
    @SerializedName("criticalPerOneMillion") var criticalPerOneMillion : Double,
    @SerializedName("affectedCountries") var affectedCountries : Long

)