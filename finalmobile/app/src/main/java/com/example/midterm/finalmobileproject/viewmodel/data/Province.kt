package com.example.midterm.finalmobileproject.viewmodel.data

import com.google.gson.annotations.SerializedName

data class Province(
    @SerializedName("name") var name: String,
    @SerializedName("code") var code: Int,
    @SerializedName("division_type") var divisionType: String,
    @SerializedName("codename") var codename: String,
    @SerializedName("phone_code") var phoneCode: Int,
    @SerializedName("districts") var districts: List<Districts>
)