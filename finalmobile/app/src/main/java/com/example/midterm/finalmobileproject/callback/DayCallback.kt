package com.example.midterm.finalmobileproject.callback

import com.example.midterm.finalmobileproject.model.Day

interface DayCallback {
    fun onCallbackDay(value: HashMap<String, Day>)
}