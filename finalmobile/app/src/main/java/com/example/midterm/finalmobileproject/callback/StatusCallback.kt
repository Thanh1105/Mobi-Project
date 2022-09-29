package com.example.midterm.finalmobileproject.callback

import com.example.midterm.finalmobileproject.model.Status

interface StatusCallback {
    fun onCallbackStatus(value: HashMap<String, Status>)
}