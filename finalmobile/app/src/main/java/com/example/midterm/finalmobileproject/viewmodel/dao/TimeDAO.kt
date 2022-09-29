package com.example.midterm.finalmobileproject.viewmodel.dao

import com.example.midterm.finalmobileproject.callback.TimeCallback
import com.example.midterm.finalmobileproject.model.Time
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TimeDAO {
    var map:HashMap<String, Time>? =HashMap<String, Time>()
    fun getItems(TimeCallback: TimeCallback){
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child("Time")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var d:Int=0
                    for (ds in dataSnapshot!!.children) {
                        val id = ds.child("id").value.toString()
                        val text = ds.child("title").value.toString()
                        val g: Time? = Time(id, text)
                        map?.put(d.toString(), g!!)
                        d++
                    }
                    TimeCallback.onCallbackTime(map!!)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}