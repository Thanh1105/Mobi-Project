package com.example.midterm.finalmobileproject.viewmodel.dao

import com.example.midterm.finalmobileproject.callback.StatusCallback
import com.example.midterm.finalmobileproject.model.Status
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StatusDAO {
    var map:HashMap<String, Status>? =HashMap<String, Status>()
    fun getItems(myCallback: StatusCallback){
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child("Status")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var d:Int=0
                    for (ds in dataSnapshot!!.children) {
                        val id = ds.child("id").value.toString()
                        val text = ds.child("title").value.toString()
                        val g: Status? = Status(id, text)
                        map?.put(d.toString(), g!!)
                        d++
                    }
                    myCallback.onCallbackStatus(map!!)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}