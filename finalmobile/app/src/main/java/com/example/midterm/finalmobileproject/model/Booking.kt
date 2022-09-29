package com.example.midterm.finalmobileproject.model

data class Booking(
    var id: String,
    val info: String,
    val slot: String,
    val date: String,
    val confirm: String,
    val check_in: String,
    val check_out: String,
    var qrcode: String,
    val note: String)
{
    override fun toString(): String {
        return "Booking(id='$id', info='$info', slot='$slot', date='$date', confirm='$confirm', check_in='$check_in', check_out='$check_out', qrcode='$qrcode', note='$note')"
    }
}