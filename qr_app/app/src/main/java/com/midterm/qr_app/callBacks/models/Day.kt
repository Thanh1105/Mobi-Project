package com.midterm.qr_app.callBacks.models

data class Day(val ID: String, val Title: String) {
    override fun toString(): String {
        return "Day(ID='$ID', Title='$Title')"
    }
}