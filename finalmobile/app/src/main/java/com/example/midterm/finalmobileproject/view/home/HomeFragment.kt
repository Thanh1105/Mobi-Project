package com.example.midterm.finalmobileproject.view.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.midterm.finalmobileproject.R
import com.example.midterm.finalmobileproject.databinding.FragmentHomeBinding
import com.example.midterm.finalmobileproject.viewmodel.APIHelper
import com.example.midterm.finalmobileproject.viewmodel.Constants
import com.example.midterm.finalmobileproject.viewmodel.Utils
import com.example.midterm.finalmobileproject.viewmodel.data.CovidVietNam
import com.example.midterm.finalmobileproject.viewmodel.data.CovidWW
import retrofit2.Call
import kotlin.math.roundToInt

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var root: View
    private var isLoadAPIVN = false;
    private var isLoadAPIWW = false;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        root = binding.root
        return root
    }

    override fun onStart() {
        super.onStart()
        initAction()
        initData()
    }

    private fun initData() {
        Utils.showLoadingDialog(requireContext())
        restAPIVN()
        restAPIWW()
        binding.wvCovidMap.settings.javaScriptEnabled = true
        binding.wvCovidMap.loadUrl(Constants.URL_COVID_MAPS)
    }

    private fun initAction() {
        binding.layoutHotLine.setOnClickListener {
            val i = Intent(
                Intent.ACTION_DIAL,
                Uri.fromParts("tel", Constants.HOT_LINE, null)
            )
            startActivity(i)
        }
        binding.layoutMedicalDecl.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(Constants.URL_KHAI_BAO_Y_TE)
            startActivity(i)
        }
//        binding.layoutAiDetect.setOnClickListener {
//            val i = Intent(activity?.applicationContext, XrayActivity::class.java)
//            startActivity(i)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun restAPIVN() {
        val call = APIHelper.initApiService(Constants.API_COVID).getCovidVietNam
        call.enqueue(object : retrofit2.Callback<CovidVietNam> {
            override fun onResponse(
                call: retrofit2.Call<CovidVietNam>,
                response: retrofit2.Response<CovidVietNam>
            ) {
                isLoadAPIVN = true
                if (isLoadAPIVN && isLoadAPIWW) {
                    Utils.hideLoadingDialog()
                }
                if (response.code() == Constants.HTTP_CODE_SUCCESS) {
                    val covidVietNam = response.body()
                    if (covidVietNam != null) {
                        displayVN(covidVietNam)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<CovidVietNam>, t: Throwable) {
                isLoadAPIVN = true
                if (isLoadAPIVN && isLoadAPIWW) {
                    Utils.hideLoadingDialog()
                }
                Utils.showToast(requireContext(), t.message.toString())
            }
        })
    }

    private fun restAPIWW() {
        val call = APIHelper.initApiService(Constants.API_COVID).getCovidWW
        call.enqueue(object : retrofit2.Callback<CovidWW> {
            override fun onResponse(
                call: retrofit2.Call<CovidWW>,
                response: retrofit2.Response<CovidWW>
            ) {
                isLoadAPIWW = true
                if (isLoadAPIVN && isLoadAPIWW) {
                    Utils.hideLoadingDialog()
                }
                if (response.code() == Constants.HTTP_CODE_SUCCESS) {
                    val covidWW = response.body()
                    if (covidWW != null) {
                        displayWW(covidWW)
                    }
                }
            }

            override fun onFailure(call: Call<CovidWW>, t: Throwable) {
                isLoadAPIWW = true
                if (isLoadAPIVN && isLoadAPIWW) {
                    Utils.hideLoadingDialog()
                }
                Utils.showToast(requireContext(), t.message.toString())
            }
        })
    }

    private fun displayVN(covidVietNam: CovidVietNam) {
        try {
            val covidVietNamConfirmed = covidVietNam.cases
            val covidVietNamActive = covidVietNam.active
            val covidVietNamRecoverd = covidVietNam.recovered
            val covidVietNamDeaths = covidVietNam.deaths
            val covidVietNamActivePercent =
                ((covidVietNamActive.toDouble() / covidVietNamConfirmed) * 100)
            val covidVietNamDeathPercent =
                (covidVietNamDeaths.toDouble() / covidVietNamConfirmed) * 100
            val covidVietNamRecoveredPercent =
                100 - covidVietNamActivePercent - covidVietNamDeathPercent
            binding.tvVnConfirmLastUpdate.text = "+".plus(covidVietNam.todayCases)
            binding.tvVnRecoveredLastUpdate.text = "+".plus(covidVietNam.todayRecovered)
            binding.tvVnDeathsLastUpdate.text = "+".plus(covidVietNam.todayDeaths)
            binding.tvVnConfirm.text = covidVietNamConfirmed.toString()
                .plus(" ${getString(R.string.confirmed)}")
            binding.tvVnActive.text = covidVietNamActive.toString()
                .plus(" ${getString(R.string.active)}")
            binding.tvVnRecovered.text = covidVietNamRecoverd.toString()
                .plus(" ${getString(R.string.recovered)}")
            binding.tvVnDeaths.text = covidVietNamDeaths.toString()
                .plus(" ${getString(R.string.deaths)}")
            binding.proVnCovidActive.progress = covidVietNamActivePercent.roundToInt()
            binding.proVnCovidRecovered.progress = covidVietNamRecoveredPercent.roundToInt()
            if (covidVietNamDeathPercent > 2) {
                binding.proVnCovidDeaths.progress = covidVietNamDeathPercent.roundToInt()
            } else {
                binding.proVnCovidDeaths.progress = 1
            }
            binding.tvVnActivePercent.text =
                String.format("%.1f", covidVietNamActivePercent).plus("%")
            binding.tvVnRecoveredPercent.text =
                String.format("%.1f", covidVietNamRecoveredPercent).plus("%")
            binding.tvVnDeathsPercent.text =
                String.format("%.1f", covidVietNamDeathPercent).plus("%")
        } catch (e: Exception) {
            Log.e("895", "WAIT")
        }
    }

    private fun displayWW(covidWW: CovidWW) {
        try {
            binding.tvGlobalConfirm.text = covidWW.cases.toString()
            binding.tvGlobalRecovered.text = covidWW.recovered.toString()
            binding.tvGlobalDeaths.text = covidWW.deaths.toString()
        } catch (e: Exception) {
            Log.e("895", "WAIT")
        }
    }
}