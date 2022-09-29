package com.example.midterm.finalmobileproject.model

data class Slot(
    val ID: String,
    val ID_Hospital: String,
    val ID_Day: String,
    val ID_Time: String,
    val ID_Shift: String,
    val Booked: String
) {
    override fun toString(): String {
        return "Slot(ID='$ID', ID_Hos='$ID_Hospital', ID_Day='$ID_Day', ID_Time='$ID_Time', ID_Shift='$ID_Shift', Booked='$Booked')"
    }
}