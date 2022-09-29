package com.midterm.qr_app.callBacks.models

data class Time(val ID: String, val Title: String) {
    override fun toString(): String {
        return "Time(ID='$ID', Title='$Title')"
    }
}
