package com.example.midterm.finalmobileproject.view.dashboard

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.midterm.finalmobileproject.callback.*
import com.example.midterm.finalmobileproject.databinding.FragmentDashboardBinding
import com.example.midterm.finalmobileproject.model.*
import com.example.midterm.finalmobileproject.viewmodel.dao.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

class DashboardFragment : Fragment() {
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private var statusMap: HashMap<String, Status>? = HashMap()
    private var dayMap: HashMap<String, Day>? = HashMap()
    private var timeMap: HashMap<String, Time>? = HashMap()
    private var shiftMap: HashMap<String, Shift>? = HashMap()
    private var slotMap: HashMap<String, Slot>? = HashMap()
    private var slotMap2: HashMap<String, String>? =
        HashMap() // luu dac diem cua slot hosp+day+time+shift
    private var hospitalMap: HashMap<String, Hospital>? = HashMap()
    private var hospitalMapTemp: HashMap<String, Hospital>? = HashMap() // luu bv theo city
    private var cityMap: HashMap<String, MutableList<String>>? = HashMap()
    private var bookingMap: HashMap<String, Booking>? = HashMap()
    private lateinit var booking: Booking
    private lateinit var auth: FirebaseAuth
    private lateinit var informationID: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        intiData()
        initBookingPage()
        startBooking()
        return binding.root
    }

    private fun preShowQR() {
        try {
            binding.layoutQrCode.visibility = View.VISIBLE
        } catch (e: Exception) {
            Log.e("895", "WAIT")
        }
        val code = bookingMap?.get("0")?.qrcode
        val confirm = status((bookingMap?.get("0")?.confirm!!.toInt()).toString())
        val checkIn = status((bookingMap?.get("0")?.check_in!!.toInt()).toString())
        val checkOut = status((bookingMap?.get("0")?.check_out!!.toInt()).toString())
        val slotInfo = "Status"
        val noti = "$slotInfo\nConfirm: $confirm\nCheck-in: $checkIn\nCheck-out: $checkOut"
        qrCodeGenerate(code!!, noti)
    }

    private fun status(i: String): String {
        if (i == "1")
            return "Success"
        if (i == "2")
            return "Pending"
        return "Failed"
    }

    private fun startBooking() {
        auth = FirebaseAuth.getInstance()
        var user = auth.currentUser
        informationID = user!!.displayName.toString()
        verificationCallbacks()
        clickSubmit()
        clickConfirm()
        clickCancel()
        selectedCity()
        seletedHospital()
        binding.vcLayoutConfirm.setOnClickListener {}
    }

    private fun initBookingPage() {
        var id = "-1"
        val dbManager = DBManager(requireContext())
        dbManager.open()
        try {
            id = dbManager.read()
        } catch (e: Exception) {
        }
        val dao = BookingDAO()
        dao.getItems(object : BookingCallback {
            override fun onCallbackBooking(value: HashMap<String, Booking>) {
                bookingMap = value
                val stringDate = bookingMap?.get("0")?.date
                val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                val dt = sdf.parse(stringDate!!)
                val range = (Date().time - dt!!.time) / 1000 / 60 / 60 / 24
                val confirm = bookingMap?.get("0")?.confirm.toString()
                val checkIn = bookingMap?.get("0")?.check_in.toString()
                val checkOut = bookingMap?.get("0")?.check_out.toString()
                if ((range <= 7) && (confirm != "3") && (checkIn != "3") && (checkOut != "3") && ((confirm != "1") || (checkIn != "1") || (checkOut != "1"))) {
                    preShowQR()
                } else {
                    binding.layoutQrCode.visibility = View.INVISIBLE
                    binding.layoutBooking.visibility = View.VISIBLE
                    binding.vcLayoutConfirm.visibility = View.INVISIBLE

                }

            }
        }, id)

    }

    private fun qrCodeGenerate(code: String, notification: String) {
        try {
            binding.layoutQrCode.visibility = View.VISIBLE
        } catch (e: Exception) {
            Log.e("895", "WAIT")
        }
        try {
            binding.layoutBooking.visibility = View.INVISIBLE
        } catch (e: Exception) {
            Log.e("895", "WAIT")
        }
        try {
            binding.vcLayoutConfirm.visibility = View.INVISIBLE
        } catch (e: Exception) {
            Log.e("895", "WAIT")
        }
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(code, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        qr_code.setImageBitmap(bitmap)
        try {
            binding.txtNotification.text = notification
        } catch (e: Exception) {
            Log.e("895", "WAIT")
        }
    }

    private fun clickCancel() {
        binding.vcBtnCancel.setOnClickListener {
            vc_layout_confirm.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun clickSubmit() {
        binding.vcBtnSubmit.setOnClickListener {

            if (binding.checkBox1.isChecked && binding.checkBox2.isChecked) {
                val hospital = binding.spHospital.selectedItemPosition
                val day = binding.spDay.selectedItemPosition
                val time = binding.spTime.selectedItemPosition
                val shift = binding.spShift.selectedItemPosition
                val slotInForm =
                    hospitalMap?.get(hospital.toString())!!.ID + dayMap?.get(day.toString())!!.ID + timeMap?.get(
                        time.toString()
                    )!!.ID + shiftMap?.get(shift.toString())!!.ID
                try {
                    val slot =
                        slotMap?.get((slotMap2?.get(slotInForm)!!.toInt() - 1).toString())!!.ID
                    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                    val currentDate = sdf.format(Date())
                    booking = Booking(
                        "0",
                        informationID,
                        slot,
                        currentDate,
                        "2",
                        "2",
                        "2",
                        randomcode(),
                        "none"
                    )
                    vc_layout_confirm.visibility = View.VISIBLE
                    layout_btn_confirm.visibility = View.VISIBLE
                    progressbar_confirming.visibility = View.INVISIBLE
                } catch (e: Exception) {
                    layout_not_found_slot.visibility = View.VISIBLE
                    val handler = Handler()
                    handler.postDelayed({
                        layout_not_found_slot.visibility = View.INVISIBLE
                    }, 1000)
                }
            }
        }
    }

    private fun createBooking() {
        binding.vcLayoutConfirm.visibility = View.INVISIBLE
        Toast.makeText(context, "Confirm Success", Toast.LENGTH_LONG).show()
        val b = BookingDAO()
        b.addBookingToFirestore(booking, requireContext())

    }

    private fun verificationCallbacks() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential_temp: PhoneAuthCredential) {
                createBooking()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(context, "Please Enter Phone Number again.", Toast.LENGTH_LONG)
                    .show()
            }

            override fun onCodeSent(
                s: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
            }
        }
    }

    private fun clickConfirm() {
        binding.vcBtnConfirm.setOnClickListener {
            if (TextUtils.isEmpty(binding.txtPhoneNumber.text.toString())) {
                Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT)
                    .show()
            } else {
                sendVerificationCode("+84" + binding.txtPhoneNumber.text.toString())
                progressbar_confirming.visibility = View.VISIBLE
            }
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun selectedCity() {
        binding.spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                hospitalMapTemp?.clear()
                val city = binding.spCity.selectedItem
                val mList: MutableList<String> = mutableListOf()
                var d = 0
                for (i in cityMap?.get(city)!!) {
                    hospitalMapTemp?.put(d.toString(), hospitalMap?.get(i)!!)
                    mList.add(hospitalMapTemp!![d.toString()]!!.Name)
                    d++
                }
                binding.spHospital.adapter =
                    context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, mList) }
            }
        }
    }

    private fun seletedHospital() {
        binding.spHospital.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val hospital = binding.spHospital.selectedItemPosition
                binding.txtDescription.text =
                    hospitalMapTemp?.get(hospital.toString())!!.Description
            }
        }
    }

    private fun intiData() {
        initDay()
        initTime()
        initShift()
        initHospital()
        initSlot()

    }

    private fun initStatus() {
        val dao = StatusDAO()
        dao.getItems(object : StatusCallback {
            override fun onCallbackStatus(value: HashMap<String, Status>) {
                statusMap = value
            }
        })
    }

    private fun initDay() {
        val dao = DayDAO()
        dao.getItems(object : DayCallback {
            override fun onCallbackDay(value: HashMap<String, Day>) {
                dayMap = value
                val mList: MutableList<String> = mutableListOf()
                for (i in value.keys)
                    mList.add(value[i]!!.Title)
                try {
                    binding.spDay.adapter = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item,
                            mList
                        )
                    }
                } catch (e: Exception) {
                    Log.e("895", "WAIT")
                }
            }
        })
    }

    private fun initTime() {
        val dao = TimeDAO()
        dao.getItems(object : TimeCallback {
            override fun onCallbackTime(value: HashMap<String, Time>) {
                timeMap = value
                val mList: MutableList<String> = mutableListOf()
                for (i in 0..(value.size - 1))
                    mList.add(value[i.toString()]?.Title.toString())
                try {
                    binding.spTime.adapter = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item,
                            mList
                        )
                    }
                } catch (e: Exception) {
                    Log.e("895", "WAIT")
                }
            }
        })
    }

    private fun initShift() {
        val dao = ShiftDAO()
        dao.getItems(object : ShiftCallback {
            override fun onCallbackShift(value: HashMap<String, Shift>) {
                shiftMap = value
                val mList: MutableList<String> = mutableListOf()
                for (i in value.keys)
                    mList.add(value[i]!!.Title)
                try {
                    binding.spShift.adapter = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item,
                            mList
                        )
                    }
                } catch (e: Exception) {
                    Log.e("895", "WAIT")
                }
            }
        })
    }

    private fun initHospital() {
        val dao = HospitalDAO()
        dao.getItems(object : HospitalCallback {
            override fun onCallbackHospital(value: HashMap<String, Hospital>) {
                hospitalMap = value
                val mList: MutableList<String> = mutableListOf()
                for (i in value.keys) {
                    hospitalMapTemp?.put(i, value[i]!!)
                    mList.add(value[i]!!.Name)
                    if (cityMap?.get(value[i]!!.City) == null) {
                        val x: MutableList<String> = mutableListOf(i)
                        cityMap?.put(value[i]!!.City, x)
                    } else {
                        cityMap?.get(value[i]!!.City)!!.add(i)
                    }
                }
                try {
                    binding.spHospital.adapter = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item,
                            mList
                        )
                    }
                } catch (e: Exception) {
                    Log.e("895", "WAIT")
                }
                try {
                    binding.spCity.adapter = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item,
                            cityMap!!.keys.toList()
                        )
                    }
                } catch (e: Exception) {
                    Log.e("895", "WAIT")
                }
            }
        })
    }

    private fun initSlot() {
        val dao = SlotDAO()
        dao.getItems(object : SlotCallback {
            override fun onCallbackSlot(value: HashMap<String, Slot>) {
                slotMap = value
                for (i in value.keys) {
                    slotMap2?.put(
                        value[i]!!.ID_Hospital + value[i]!!.ID_Day + value[i]!!.ID_Time + value[i]!!.ID_Shift,
                        value[i]!!.ID
                    )
                }
            }
        })
    }

    @SuppressLint("NewApi")
    fun randomcode(): String {
        val leftLimit = 48 // numeral '0'
        val rightLimit = 122 // letter 'z'
        val targetStringLength = 10
        val random = Random()
        return random.ints(leftLimit, rightLimit + 1)
            .filter { i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97) }
            .limit(targetStringLength.toLong())
            .collect(
                { StringBuilder() },
                { obj: java.lang.StringBuilder, codePoint: Int -> obj.appendCodePoint(codePoint) },
                java.lang.StringBuilder::append
            )
            .toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}