package com.midterm.qr_app.callBacks.modelDAOs

import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.testdb.callBacks.DayCallback
import com.example.testdb.callBacks.DeviceCallback
import com.example.testdb.models.Day
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.testdb.models.*
import com.save.modelDAOs.DBManager

class DeviceDAO {

    fun addDeviceToFirestore(device: Device, context : Context){
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child(FirebaseChild.datachild).child("Device")
        var dbManager = DBManager(context)
        dbManager.open()
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val id = (dataSnapshot.childrenCount+1).toString()
                device.id = id
                val ref = Firebase.database.getReference(FirebaseChild.datachild).child("Device").child(id).setValue(device)
                dbManager.insert(id)
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }



    var map:HashMap<String, Device>? =HashMap()
    fun getItems(myCallback: DeviceCallback, id: String){
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child(FirebaseChild.datachild).child("Device").child(id)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                if (ds.exists()) {
                    val d=0
                    val id = ds.child("id").value.toString()
                    val deviceName = ds.child("device_name").value.toString()
                    val hospital = ds.child("hospital").value.toString()
                    val date = ds.child("date").value.toString()
                    val role = ds.child("role").value.toString()
                    val g: Device? = Device(id, deviceName, hospital, date,role)
                    map?.put(d.toString(), g!!)
                    myCallback.onCallbackDevice(map!!)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}