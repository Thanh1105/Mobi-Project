package com.example.midterm.finalmobileproject.viewmodel.data

import com.google.gson.annotations.SerializedName

data class Wards(
    @SerializedName("name") var name: String,
    @SerializedName("code") var code: Int,
    @SerializedName("division_type") var divisionType: String,
    @SerializedName("codename") var codename: String,
    @SerializedName("district_code") var districtCode: Int
)