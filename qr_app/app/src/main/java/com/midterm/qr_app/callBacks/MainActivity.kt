package com.midterm.qr_app.callBacks

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testdb.callBacks.DeviceCallback
import com.example.testdb.callBacks.HospitalCallback
import com.example.testdb.modelDAOs.DeviceDAO
import com.example.testdb.modelDAOs.HospitalDAO
import com.example.testdb.models.Device
import com.save.modelDAOs.DBManager
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    private var deviceMap:HashMap<String,Device>? = HashMap()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try{
            layout_wait_main.visibility = View.VISIBLE
        }catch (e:Exception){
            Log.e("895","WAIT")
        }
        val handler = Handler()
        handler.postDelayed({
            try{
                layout_wait_main.visibility = View.INVISIBLE
            }catch (e:Exception){
                Log.e("895","WAIT")
            }

        }, 2000)
        initDevicePage()
        clickJoin()
        enter_join_code.setOnClickListener {
            closeKeyboard()
        }
        imageView4.setOnClickListener {
            closeKeyboard()
        }
    }

    private fun initDevicePage() {
        var id = "-1"
        val dbManager = DBManager(applicationContext)
        dbManager.open()
        try {
            id = dbManager.read()
        } catch (e: Exception) {
        }
        val dao = DeviceDAO()
        val i = Intent(this, ScanActivity::class.java)
        dao.getItems(object : DeviceCallback {
            override fun onCallbackDevice(value: HashMap<String, Device>) {
                if (!value.isEmpty()) {
                    deviceMap = value
                    i.putExtra("role", deviceMap?.get("0")?.role.toString())
                    i.putExtra("hospital", deviceMap?.get("0")?.hospital.toString())
                    startActivity(i)
                }
            }
        }, id)
    }
    @SuppressLint("SimpleDateFormat")
    private fun clickJoin(){
        btnJoin.setOnClickListener {
            closeKeyboard()
            val code = edtCode.text.toString()
            val dao = HospitalDAO()
            val i = Intent(this, ScanActivity::class.java)
            dao.checkHospital( object : HospitalCallback {
                override fun onCallbackHospital(value: Boolean) {
                    if (value){
                        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                        val currentDate = sdf.format(Date())
                        var device = Device("0",Build.MANUFACTURER+ " " + Build.MODEL,code.split("_")[0],currentDate,"3")
                        val b = DeviceDAO()
                        b.addDeviceToFirestore(device,applicationContext)
                        Toast.makeText(applicationContext, "Successfully", Toast.LENGTH_LONG).show()
                        i.putExtra("role", "3")
                        i.putExtra("hospital",code.split("/")[0])
                        startActivity(i)

                    }
                    else{
                        Toast.makeText(applicationContext, "Not match with any hospital", Toast.LENGTH_LONG).show()
                    }
                }
            }, code)
        }
    }
    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val manager: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}