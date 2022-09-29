package com.example.midterm.finalmobileproject.callback

import com.example.midterm.finalmobileproject.model.Booking

interface BookingCallback {
    fun onCallbackBooking(value: HashMap<String, Booking>)
}