package com.example.midterm.finalmobileproject.model

data class Day(val ID: String, val Title: String) {
    override fun toString(): String {
        return "Day(ID='$ID', Title='$Title')"
    }
}