package com.example.midterm.finalmobileproject.callback

import com.example.midterm.finalmobileproject.model.Time

interface TimeCallback {
    fun onCallbackTime(value: HashMap<String, Time>)
}