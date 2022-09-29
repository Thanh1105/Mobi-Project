package com.example.midterm.finalmobileproject.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.midterm.finalmobileproject.MainActivity
import com.example.midterm.finalmobileproject.R
import com.example.midterm.finalmobileproject.model.Information
import com.example.midterm.finalmobileproject.viewmodel.APIHelper
import com.example.midterm.finalmobileproject.viewmodel.Constants
import com.example.midterm.finalmobileproject.viewmodel.Utils
import com.example.midterm.finalmobileproject.viewmodel.data.Districts
import com.example.midterm.finalmobileproject.viewmodel.data.Province
import com.example.midterm.finalmobileproject.viewmodel.data.Wards
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up_profile.*
import java.text.SimpleDateFormat
import java.util.*

class SignUpProfileActivity : AppCompatActivity() {

    private val mFirebaseDatabase = FirebaseDatabase.getInstance().reference
    private lateinit var mPhoneNumber: String
    private lateinit var mSelectedProvince: Province
    private lateinit var mListProvince: List<Province>
    private lateinit var mListDistricts: List<Districts>
    private lateinit var mSelectedDistricts: Districts
    private lateinit var mSelectedWards: Wards
    private val mCalendar = Calendar.getInstance()
    private val mTypeGetWards = 3
    private val mSdf = SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.US)
    private val mSdfFirebase = SimpleDateFormat(Constants.DATE_TIME_FORMAT_FIREBASE, Locale.US)

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            mCalendar.set(Calendar.YEAR, year)
            mCalendar.set(Calendar.MONTH, monthOfYear)
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            if (mCalendar.timeInMillis > Calendar.getInstance().timeInMillis) {
                Utils.showToast(this, getString(R.string.please_enter_validate_date))
            } else {
                tv_birthday.text = mSdf.format(mCalendar.time)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_profile)
        mPhoneNumber = intent.getStringExtra(Constants.PHONE_NUMBER).toString()
        initGender()
        initProvince()
        initBirthday()
        initAction()
    }

    private fun initGender() {
        val genderList: MutableList<String> = mutableListOf()
        genderList.add(getString(R.string.male))
        genderList.add(getString(R.string.female))
        genderList.add(getString(R.string.other_gender))
        sp_gender.adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            genderList
        )
    }

    private fun initProvince() {
        Utils.showLoadingDialog(this)
        val call = APIHelper.initApiService(Constants.API_GET_PROVINCE).getProvince
        call.enqueue(object : retrofit2.Callback<List<Province>> {
            override fun onResponse(
                call: retrofit2.Call<List<Province>>,
                response: retrofit2.Response<List<Province>>
            ) {
                Utils.hideLoadingDialog()
                val listProvinceName = mutableListOf<String>()
                mListProvince = response.body()!!
                mListProvince.forEach {
                    listProvinceName.add(it.name)
                }
                sp_province.adapter = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    listProvinceName
                )
                sp_province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        mSelectedProvince = mListProvince[position]
                        initDistrict()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }

            override fun onFailure(call: retrofit2.Call<List<Province>>, t: Throwable) {
                Log.d("ttt", t.message.toString())
                Utils.hideLoadingDialog()
            }
        })
    }

    private fun initDistrict() {
        Utils.showLoadingDialog(this)
        val call = APIHelper.initApiService(Constants.API_GET_PROVINCE)
            .getDistricts(mSelectedProvince.code, mTypeGetWards)
        call.enqueue(object : retrofit2.Callback<Province> {
            override fun onResponse(
                call: retrofit2.Call<Province>,
                response: retrofit2.Response<Province>
            ) {
                Utils.hideLoadingDialog()
                val listDistrictsName = mutableListOf<String>()
                mListDistricts = response.body()!!.districts
                mListDistricts.forEach {
                    listDistrictsName.add(it.name)
                }
                sp_district.adapter = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    listDistrictsName
                )
                sp_district.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        mSelectedDistricts = mListDistricts[position]
                        initWards()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }

            override fun onFailure(call: retrofit2.Call<Province>, t: Throwable) {
                Log.d("ttt", t.message.toString())
                Utils.hideLoadingDialog()
            }
        })
    }

    private fun initWards() {
        val listWardsName = mutableListOf<String>()
        mSelectedDistricts.wards.forEach {
            listWardsName.add(it.name)
        }
        sp_ward.adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            listWardsName
        )
        sp_ward.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mSelectedWards = mSelectedDistricts.wards[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun initBirthday() {
        val datePickerDialog = DatePickerDialog(
            this, dateSetListener,
            mCalendar.get(Calendar.YEAR),
            mCalendar.get(Calendar.MONTH),
            mCalendar.get(Calendar.DAY_OF_MONTH)
        )
        tv_birthday.setOnClickListener() {
            datePickerDialog.show()
        }
    }


    private fun initAction() {
        clickFinish()
    }


    private fun addInformationToFirestore(information: Information) {
        val ref = mFirebaseDatabase.child(Constants.FireBaseChild.INFORMATION)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                information.id =
                    dataSnapshot.childrenCount + Constants.ONE_INT // ko code trong nay luon sau login set ve true
                Firebase.database.getReference(Constants.FireBaseChild.INFORMATION)
                    .child(information.id.toString()).setValue(information).addOnSuccessListener {
                        val i = Intent(applicationContext, MainActivity::class.java)
                        Utils.getSharedPreferences(applicationContext).edit()
                            .putBoolean(Constants.PrefKey.IS_LOGIN, true).apply()
                        Utils.hideLoadingDialog()
                        startActivity(i)
                        finish()
                    }.addOnFailureListener {
                        Utils.showToast(applicationContext, it.message.toString())
                    }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Utils.showToast(applicationContext, databaseError.message)
            }
        })
    }

    private fun clickFinish() {
        btn_finish.setOnClickListener {
            Utils.showLoadingDialog(this)
            if (edt_name.text.toString().isEmpty() && edt_address.text.toString().isEmpty()) {
                Utils.showToast(applicationContext, getString(R.string.isEmpty))
            } else {
                signUpInformation()
            }
        }
    }

    private fun signUpInformation() {
        val name = edt_name.text.toString()
        val gender = sp_gender.selectedItem.toString()
        val birthDate = tv_birthday.text.toString()
        val address = edt_address.text.toString()
        val ward = sp_ward.selectedItem.toString()
        val district = sp_district.selectedItem.toString()
        val city = sp_ward.selectedItem.toString()
        val currentDate = mSdfFirebase.format(Date())
        val info = Information(
            -1, // default value
            name,
            gender,
            birthDate,
            address,
            ward,
            district,
            city,
            mPhoneNumber,
            currentDate
        )
        addInformationToFirestore(info)
    }
}
