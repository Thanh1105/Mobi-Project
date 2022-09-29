package com.example.midterm.finalmobileproject.viewmodel.dao

import com.example.midterm.finalmobileproject.callback.HospitalCallback
import com.example.midterm.finalmobileproject.model.Hospital
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HospitalDAO {
    var map:HashMap<String, Hospital>? =HashMap<String, Hospital>()
    fun getItems(myCallback: HospitalCallback){
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child("Hospital")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var d:Int=0
                    for (ds in dataSnapshot!!.children) {
                        val id = ds.child("id").value.toString()
                        val name = ds.child("name").value.toString()
                        val description = ds.child("description").value.toString()
                        val hotline = ds.child("hotline").value.toString()
                        val latitude = ds.child("latitude").value.toString()
                        val longitude = ds.child("longitude").value.toString()
                        val join_code = ds.child("join_code").value.toString()
                        val location = ds.child("location").value.toString()
                        val ward = ds.child("ward").value.toString()
                        val district = ds.child("district").value.toString()
                        val city = ds.child("city").value.toString()
                        val national = ds.child("national").value.toString()
                        val date = ds.child("date").value.toString()
                        val g: Hospital? = Hospital(id,name,description,hotline,latitude,longitude,join_code,location,ward,district,city,national,date)
                        map?.put(d.toString(), g!!)
                        d++
                    }
                    myCallback.onCallbackHospital(map!!)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}