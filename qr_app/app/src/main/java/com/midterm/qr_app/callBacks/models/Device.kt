package com.midterm.qr_app.callBacks.models

data class Device(var id: String, val device_name: String, val hospital: String, val date: String, val role: String) {
    override fun toString(): String {
        return "Device(id='$id', device_name='$device_name', hospital='$hospital', date='$date', role='$role')"
    }
}