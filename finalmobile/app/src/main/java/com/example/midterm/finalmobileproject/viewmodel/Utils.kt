package com.example.midterm.finalmobileproject.viewmodel

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.example.midterm.finalmobileproject.R
import kotlinx.android.synthetic.main.progress_dialog.*
import kotlinx.android.synthetic.main.progress_dialog.view.*
import kotlinx.android.synthetic.main.activity_splash.*
import com.bumptech.glide.Glide

object Utils {
    private var mLoadingDialog : Dialog? = null

    fun showLoadingDialog(context: Context) {
        if(mLoadingDialog == null){
            mLoadingDialog = Dialog(context, R.style.FullScreen_Dialog)
            mLoadingDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mLoadingDialog!!.setCancelable(false)
            mLoadingDialog!!.setContentView(R.layout.progress_dialog)
            Glide.with(context).asGif().load(R.drawable.ic_loading).into(mLoadingDialog!!.img_loading)
            mLoadingDialog!!.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            mLoadingDialog!!.show()
        } else {
            mLoadingDialog!!.show()
        }
    }

    fun hideLoadingDialog() {
        if(mLoadingDialog != null){
            mLoadingDialog!!.dismiss()
            mLoadingDialog = null
        }
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getSharedPreferences(context: Context) : SharedPreferences {
        return context.getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE)
    }
}