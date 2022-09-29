package com.midterm.qr_app.callBacks.models

data class Shift(val ID: String, val Title: String) {
    override fun toString(): String {
        return "Shift(ID='$ID', Title='$Title')"
    }
}
