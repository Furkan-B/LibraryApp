package com.example.kutuphaneuyg

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater

class LoadingScreen(private val myContext: Context) {

    private var mAlert : AlertDialog ?= null

    fun showLoading() {
        val mBuilder = AlertDialog.Builder(myContext)
        mBuilder.setView(LayoutInflater.from(myContext).inflate(R.layout.loading_screen,null))
        mBuilder.setCancelable(false)
        mAlert = mBuilder.show()
    }

    fun hideLoading() {
        if(mAlert != null)
            mAlert?.dismiss()
    }


}