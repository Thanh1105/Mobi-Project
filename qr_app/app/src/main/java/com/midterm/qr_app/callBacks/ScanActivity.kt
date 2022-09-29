package com.midterm.qr_app.callBacks

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testdb.callBacks.*
import com.example.testdb.modelDAOs.*
import com.example.testdb.models.*
import com.google.firebase.database.DatabaseError
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import com.save.modelDAOs.DBManager
import kotlinx.android.synthetic.main.activity_scan.*


class ScanActivity : AppCompatActivity() {
    private var mCapture: CaptureManager? = null
    private var lastText = ""
    private var role = "3"
    private var slot = "0"
    private var dayMap:HashMap<String,Day>? = HashMap()
    private var timeMap:HashMap<String, Time>? = HashMap()
    private var shiftMap:HashMap<String,Shift>? = HashMap()
    private var slotMap:HashMap<String,Slot>? = HashMap()
    private var slotMap2:HashMap<String,String>? = HashMap() // luu dac diem cua slot hosp+day+time+shift
    private var hospital = "-1"
    private lateinit var contextx:Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        val i=intent
        role = i.getStringExtra("role").toString()
        hospital = i.getStringExtra("hospital").toString()
        initData()
        image_left.setOnClickListener { onBackPressed() }
        mCapture = CaptureManager(this, qr_barcode_scanner)
        mCapture!!.initializeFromIntent(intent, null)
        mCapture!!.setShowMissingCameraPermissionDialog(true)
        mCapture!!.decode()
        qr_barcode_scanner.decodeContinuous(mCallback)
        btn_save.setOnClickListener {
            save()
        }
        btn_edit.setOnClickListener {
            edit()
        }
        contextx = this
    }
    private fun initData(){
        initDay()
        initTime()
        initShift()
        initSlot()

        txt_noti.text = when(role){
            "1" -> "Check-in"
            "2" -> "Check-out"
            else -> "No Permission"
        }
    }

    override fun onPause() {
        super.onPause()
        mCapture!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        mCapture!!.onResume()
    }
    private val mCallback: BarcodeCallback? = BarcodeCallback { result: BarcodeResult? ->
        if (result?.text != null && lastText != result.text) {
            Log.e("895",result.text.toString())
            if (slot == "0") {
                Toast.makeText(this, "Please choice slot.", Toast.LENGTH_SHORT).show()
                val handler = Handler()
                handler.postDelayed({
                }, 1500)
            }
            else {
                lastText = result.text
                //Toast.makeText(this, lastText, Toast.LENGTH_SHORT).show()
                val handler = Handler()
                handler.postDelayed({
                }, 1000)
            }
            val dao = BookingDAO()
            dao.checkBooking( object : BookingCallback {
                override fun onCallbackBooking(value: Boolean) {
                    if (value) {
                        imgdone.visibility = View.VISIBLE
                        val handler = Handler()
                        handler.postDelayed({
                            imgdone.visibility = View.INVISIBLE
                        }, 1500)
                    }else{
                        lastText=""
                    }
                }
            },lastText,slot,role,contextx)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mCapture!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun initDay(){
        val dao = DayDAO()
        dao.getItems( object : DayCallback {
            override fun onCallbackDay(value: HashMap<String, Day>) {
                dayMap = value
                val mList: MutableList<String> = mutableListOf()
                for (i in value.keys)
                    mList.add(value[i]!!.Title)
                try{
                    sp_day.adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, mList)
                }catch (e:Exception){
                    Log.e("895","WAIT")
                }
            }
        })
    }

    private fun initTime(){
        val dao = TimeDAO()
        dao.getItems( object : TimeCallback {
            override fun onCallbackTime(value: HashMap<String, Time>) {
                timeMap = value
                val mList: MutableList<String> = mutableListOf()
                for (i in 0..(value.size-1))
                    mList.add(value[i.toString()]?.Title.toString())
                try{
                    sp_time.adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, mList)
                }catch (e:Exception){
                    Log.e("895","WAIT")
                }
            }
        })
    }

    private fun initShift(){
        val dao = ShiftDAO()
        dao.getItems( object : ShiftCallback {
            override fun onCallbackShift(value: HashMap<String, Shift>) {
                shiftMap = value
                val mList: MutableList<String> = mutableListOf()
                for (i in value.keys)
                    mList.add(value[i]!!.Title)
                try {
                    sp_shift.adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, mList)
                }catch (e:Exception){
                    Log.e("895","WAIT")
                }
            }
        })
    }

    private fun initSlot(){
        val dao = SlotDAO()
        dao.getItems( object : SlotCallback {
            override fun onCallbackSlot(value: HashMap<String, Slot>) {
                slotMap = value
                for (i in value.keys) {
                    slotMap2?.put(value[i]!!.ID_Hospital+ value[i]!!.ID_Day+ value[i]!!.ID_Time+ value[i]!!.ID_Shift,
                        value[i]!!.ID)
                }
            }
        })
    }

    private fun edit(){
        btn_edit.setTextColor(resources.getColor(R.color.silver))
        btn_save.setTextColor(resources.getColor(R.color.black))
        sp_day.isEnabled = true
        sp_time.isEnabled = true
        sp_shift.isEnabled = true

    }
    private fun save(){
        val time =  (hospital.toInt()).toString() + dayMap?.get(sp_day.selectedItemPosition.toString())!!.ID + timeMap?.get(sp_time.selectedItemPosition.toString())!!.ID + shiftMap?.get(sp_shift.selectedItemPosition.toString())!!.ID
        if(slotMap2?.get(time) != null) {
            slot = slotMap2?.get(time).toString()
            Log.e("895",slot)
            btn_edit.setTextColor(resources.getColor(R.color.black))
            btn_save.setTextColor(resources.getColor(R.color.silver))
            sp_day.isEnabled = false
            sp_time.isEnabled = false
            sp_shift.isEnabled = false
        }else{
            Toast.makeText(this,"Not match any slot", Toast.LENGTH_SHORT).show()
        }
    }

}