package com.example.midterm.finalmobileproject.callback

import com.example.midterm.finalmobileproject.model.Slot

interface SlotCallback {
    fun onCallbackSlot(value: HashMap<String, Slot>)
}