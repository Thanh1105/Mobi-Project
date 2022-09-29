package com.example.midterm.finalmobileproject.model

data class Status(val ID: String, val Title: String) {
    override fun toString(): String {
        return "Status(ID='$ID', Title='$Title')"
    }
}