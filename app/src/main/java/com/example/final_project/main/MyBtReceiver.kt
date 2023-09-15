package com.example.final_project.main

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.final_project.viewModel.switchBT

class MyBtReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val mBluetoothAdapter : BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if(mBluetoothAdapter.isEnabled){
            switchBT.myCheck!!.value = "On"
            switchBT.mCheckState!!.value = true
        }
        else{
            switchBT.myCheck!!.value = "Off"
            switchBT.mCheckState!!.value = false
        }

    }
}