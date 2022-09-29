package com.midterm.qr_app.callBacks.modelDAOs

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.testdb.callBacks.HospitalCallback

class HospitalDAO {
    fun checkHospital(myCallback: HospitalCallback, code: String){
        val codevalue = code.split("_")
        val id=codevalue[0]
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child(FirebaseChild.datachild).child("Hospital").child(id).child("join_code")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.e("895",dataSnapshot.value.toString()+"    "+code)
                    myCallback.onCallbackHospital(dataSnapshot.value.toString() == code)
                } else{
                    myCallback.onCallbackHospital(false)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}