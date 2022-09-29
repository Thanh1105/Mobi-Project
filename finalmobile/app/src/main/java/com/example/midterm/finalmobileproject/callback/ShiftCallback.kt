package com.example.midterm.finalmobileproject.callback

import com.example.midterm.finalmobileproject.model.Shift

interface ShiftCallback {
    fun onCallbackShift(value: HashMap<String, Shift>)
}