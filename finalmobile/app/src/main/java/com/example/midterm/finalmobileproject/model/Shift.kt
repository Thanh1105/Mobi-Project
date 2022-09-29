package com.example.midterm.finalmobileproject.model

data class Shift(val ID: String, val Title: String) {
    override fun toString(): String {
        return "Shift(ID='$ID', Title='$Title')"
    }
}