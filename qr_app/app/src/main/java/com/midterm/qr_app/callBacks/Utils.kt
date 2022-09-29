package com.midterm.qr_app.callBacks

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.Window

object Utils {
    private var mLoadingDialog : Dialog? = null

    /**
     * This method using to create dialog loading screen
     *
     * @param context [Context]
     * @return [Dialog]
     */
    fun showLoadingDialog(context: Context) {
        if(mLoadingDialog == null){
            mLoadingDialog = Dialog(context, R.style.FullScreen_Dialog)
            mLoadingDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mLoadingDialog!!.setCancelable(false)
            mLoadingDialog!!.setContentView(R.layout.progress_dialog)
            mLoadingDialog!!.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            mLoadingDialog!!.show()
        } else {
            mLoadingDialog!!.show()
        }
    }

    /**
     * This method using to hide dialog loading screen
     *
     * @param context [Context]
     * @return [Dialog]
     */
    fun hideLoadingDialog() {
        if(mLoadingDialog != null){
            mLoadingDialog!!.hide()
            mLoadingDialog = null
        }
    }
}