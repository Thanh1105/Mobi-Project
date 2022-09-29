package com.example.midterm.finalmobileproject.model

data class Time(val ID: String, val Title: String) {
    override fun toString(): String {
        return "Time(ID='$ID', Title='$Title')"
    }
}