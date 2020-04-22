package com.example.analytiq.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectivityManager {

    fun checkConnectivity(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeInternet: NetworkInfo?=connectivityManager.activeNetworkInfo

        if(activeInternet?.isConnected!=null){
            return activeInternet?.isConnected
        }
        else
            return false
    }
}