package com.example.midterm.finalmobileproject.model

import java.io.Serializable

data class Gender(val ID: String, val Title: String) : Serializable {
    override fun toString(): String {
        return "Gender(ID='$ID', Title='$Title')"
    }
}