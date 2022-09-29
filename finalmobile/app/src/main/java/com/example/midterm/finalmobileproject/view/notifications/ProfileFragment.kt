package com.example.midterm.finalmobileproject.view.notifications

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.midterm.finalmobileproject.R
import com.example.midterm.finalmobileproject.databinding.FragmentNotificationsBinding
import com.example.midterm.finalmobileproject.model.Gender
import com.example.midterm.finalmobileproject.model.Information
import com.google.firebase.auth.FirebaseAuth
import java.util.HashMap


class ProfileFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private var genderMap: HashMap<String, Gender>? = HashMap()
    private var InformationMap: HashMap<String, Information>? = HashMap()
    private var informationID =""
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        try{
            binding.layoutWaitProfile.visibility = View.VISIBLE
        }catch (e:Exception){
            Log.e("895","WAIT")
        }
        val handler = Handler()
        handler.postDelayed({
            try{
                binding.layoutWaitProfile.visibility = View.INVISIBLE
            }catch (e:Exception){
                Log.e("895","WAIT")
            }
        }, 2000)

        initAction()

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAction(){
        var auth = FirebaseAuth.getInstance()
        var user = auth.currentUser
        informationID=user!!.displayName.toString()
//        initGender()
        initInformation()
//        clickFinish()
    }

//    private fun clickFinish(){
//        //try {
//            binding.btnFinishProfile.setOnClickListener {
//                val name = edt_name_profile.text.toString()
//                val gender = genderMap?.get(sp_gender.selectedItemPosition.toString())!!.ID
//                val birthdate = edt_birthdate_profile.text.toString()
//                val address = edt_address_profile.text.toString()
//                val ward = edt_ward_profile.text.toString()
//                val district = edt_district_profile.text.toString()
//                val city = edt_city_profile.text.toString()
//                val national = edt_national_profile.text.toString()
//                val info = Information(
//                    informationID,
//                    name,
//                    gender,
//                    birthdate,
//                    address,
//                    ward,
//                    district,
//                    city,
//                    national,
//                    InformationMap!!["0"]!!.date,
//                    InformationMap!!["0"]!!.bluetooth,
//                    InformationMap!!["0"]!!.date
//                )
//                Log.e("895", info.toString())
//                val dao = InformationDAO()
//                dao.addInformationToFirestore(info, requireContext(), false)
//                Toast.makeText(requireContext(),"Update Success", Toast.LENGTH_LONG).show()
//            }
//        //}catch (e:Exception){
//        //    Log.e("895","WAIT")
//        //}
//    }

//    private fun initGender(){
//        val dao = GenderDAO()
//        dao.getItems( object : GenderCallback {
//            override fun onCallbackGender(value: HashMap<String, Gender>) {
//                genderMap = value
//                val mList: MutableList<String> = mutableListOf()
//                for (i in value.keys)
//                    mList.add(value[i]!!.Title)
//                try{
//                    sp_gender.adapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, mList) }
//                }catch (e:Exception){
//                    Log.e("895","WAIT")
//                }
//            }
//        })
//    }

    private fun initInformation(){
//        comment lai a voi
//        dao.getItems( object : InformationCallback {
//            override fun onCallbackInformation(value: HashMap<String, Information>) {
//                InformationMap = value
//                Log.e("895","Information " + InformationMap.toString())
//                try{
//                edt_name_profile.setText(InformationMap!!["0"]!!.name)
//                edt_birthdate_profile.setText(InformationMap!!["0"]!!.birthdate)
//                edt_address_profile.setText(InformationMap!!["0"]!!.address)
//                edt_ward_profile.setText(InformationMap!!["0"]!!.ward)
//                edt_district_profile.setText(InformationMap!!["0"]!!.district)
//                edt_city_profile.setText(InformationMap!!["0"]!!.city)
//
//                }catch (e:Exception){
//                    Log.e("895","WAIT")
//                }
//
//            }
//        }, informationID)
    }
}