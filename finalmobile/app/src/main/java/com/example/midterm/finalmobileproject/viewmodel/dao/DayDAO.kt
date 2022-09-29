package com.example.midterm.finalmobileproject.viewmodel.dao

import com.example.midterm.finalmobileproject.callback.DayCallback
import com.example.midterm.finalmobileproject.model.Day
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DayDAO {
    var map:HashMap<String, Day>? =HashMap<String, Day>()
    fun getItems(myCallback: DayCallback){
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child("Day")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var d:Int=0
                    for (ds in dataSnapshot!!.children) {
                        val id = ds.child("id").value.toString()
                        val text = ds.child("title").value.toString()
                        val g: Day? = Day(id, text)
                        map?.put(d.toString(), g!!)
                        d++
                    }
                    myCallback.onCallbackDay(map!!)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}