package com.midterm.qr_app.callBacks.modelDAOs

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.testdb.callBacks.BookingCallback
import com.example.testdb.callBacks.HospitalCallback
import com.midterm.qr_app.callBacks.BookingCallback

class BookingDAO {
    fun checkBooking(myCallback: BookingCallback, code: String, slot: String, permission: String, context: Context){
        
        Log.e("895",code +"/"+ slot +"/" + permission)
        val codevalue = code.split("/")
        val id=codevalue[0]
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child(FirebaseChild.datachild).child("Booking").child(id)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.e("895",dataSnapshot.value.toString()+"    "+code)
                    if(dataSnapshot.child("qrcode").value.toString() == code && dataSnapshot.child("slot").value.toString() == slot) {
                        if (permission == "3"){
                            Toast.makeText(context, "Not permission yet", Toast.LENGTH_LONG).show()
                        }else
                            if (dataSnapshot.child("confirm").value.toString() == "1") {
                                if (permission == "1") {
                                    ref.child("check_in").setValue("1")
                                    myCallback.onCallbackBooking(true)
                                } else if (permission == "2" && dataSnapshot.child("check_in").value.toString() == "1") {
                                    ref.child("check_out").setValue("1")
                                    myCallback.onCallbackBooking(true)
                                }else{
                                    Toast.makeText(context,"Not checked-in yet", Toast.LENGTH_LONG).show()
                                }
                            }else {
                                Toast.makeText(context, "Unconfirmed Booking", Toast.LENGTH_LONG).show()
                            }
                    }
                }
                myCallback.onCallbackBooking(false)
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}