package com.example.midterm.finalmobileproject.viewmodel.dao

import com.example.midterm.finalmobileproject.callback.SlotCallback
import com.example.midterm.finalmobileproject.model.Slot
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SlotDAO {
    var map:HashMap<String, Slot>? =HashMap<String, Slot>()
    fun getItems(myCallback: SlotCallback){
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child("Slot")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var d:Int=0
                    for (ds in dataSnapshot!!.children) {
                        val id = ds.child("id").value.toString()
                        val hospital = ds.child("hospital").value.toString()
                        val day = ds.child("day").value.toString()
                        val time = ds.child("time").value.toString()
                        val shift = ds.child("shift").value.toString()
                        val booked = ds.child("booked").value.toString()
                        val g: Slot? = Slot(id, hospital, day, time, shift, booked)
                        map?.put(d.toString(), g!!)
                        d++
                    }
                    myCallback.onCallbackSlot(map!!)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}