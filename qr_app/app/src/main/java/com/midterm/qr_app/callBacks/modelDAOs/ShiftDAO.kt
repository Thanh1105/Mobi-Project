package com.midterm.qr_app.callBacks.modelDAOs

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.testdb.callBacks.ShiftCallback
import com.example.testdb.models.Shift

class ShiftDAO {
    var map:HashMap<String, Shift>? =HashMap<String, Shift>()
    fun getItems(myCallback: ShiftCallback){
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child(FirebaseChild.datachild).child("Shift")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var d:Int=0
                    for (ds in dataSnapshot!!.children) {
                        val id = ds.child("id").value.toString()
                        val text = ds.child("title").value.toString()
                        val g: Shift? = Shift(id, text)
                        map?.put(d.toString(), g!!)
                        d++
                    }
                    myCallback.onCallbackShift(map!!)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}