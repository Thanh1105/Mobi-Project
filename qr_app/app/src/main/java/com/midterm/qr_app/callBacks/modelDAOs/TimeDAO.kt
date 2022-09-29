package com.midterm.qr_app.callBacks.modelDAOs

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.testdb.callBacks.TimeCallback
import com.example.testdb.models.Time

class TimeDAO {
    var map:HashMap<String, Time>? =HashMap<String, Time>()
    fun getItems(TimeCallback: TimeCallback){
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child(FirebaseChild.datachild).child("Time")
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