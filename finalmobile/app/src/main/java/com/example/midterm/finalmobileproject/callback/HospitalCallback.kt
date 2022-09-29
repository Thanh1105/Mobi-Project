package com.example.midterm.finalmobileproject.callback

import com.example.midterm.finalmobileproject.model.Hospital

interface HospitalCallback {
    fun onCallbackHospital(value: HashMap<String, Hospital>)
}