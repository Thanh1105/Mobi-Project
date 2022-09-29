package com.example.midterm.finalmobileproject.viewmodel.dao

import android.content.Context
import com.example.midterm.finalmobileproject.callback.BookingCallback
import com.example.midterm.finalmobileproject.model.Booking
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BookingDAO {

    fun addBookingToFirestore(booking : Booking, context : Context){
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child("Booking")

        var dbManager = DBManager(context)
        dbManager.open()


        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val id = (dataSnapshot.childrenCount+1).toString()
                booking.id = id
                booking.qrcode = id+"/"+booking.qrcode
                val ref = Firebase.database.getReference("Booking").child(id).setValue(booking)
                dbManager.insert(id)
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }



    var map:HashMap<String, Booking>? =HashMap<String, Booking>()
    fun getItems(myCallback: BookingCallback, id: String){
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child("Booking").child(id)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                if (ds.exists()) {
                    //Log.e("895","Firebase Booking " + ds.toString())
                    val d=0
                    val id = ds.child("id").value.toString()
                    val info = ds.child("info").value.toString()
                    val slot = ds.child("slot").value.toString()
                    val date = ds.child("date").value.toString()
                    val confirm = ds.child("confirm").value.toString()
                    val check_in = ds.child("check_in").value.toString()
                    val check_out = ds.child("check_out").value.toString()
                    val qrcode = ds.child("qrcode").value.toString()
                    val note = ds.child("note").value.toString()
                    val g: Booking? = Booking(id, info, slot, date, confirm, check_in, check_out, qrcode, note)
                    map?.put(d.toString(), g!!)
                    myCallback.onCallbackBooking(map!!)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}