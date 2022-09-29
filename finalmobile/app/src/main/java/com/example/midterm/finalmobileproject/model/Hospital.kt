package com.example.midterm.finalmobileproject.model

data class Hospital(
    val ID: String,
    val Name: String,
    val Description: String,
    val Hotline: String,
    val Latitude: String,
    val Longitude: String,
    val Join_Code: String,
    val Location: String,
    val Ward: String,
    val District: String,
    val City: String,
    val National: String,
    val Date: String
) {
    override fun toString(): String {
        return "Hospital(ID='$ID', Name='$Name', Description='$Description', Hotline='$Hotline', Latitude='$Latitude', Longitude='$Longitude', Join_Code='$Join_Code', Location='$Location', Ward='$Ward'', District='$District', City='$City, National='$National', Date='$Date')"
    }
}