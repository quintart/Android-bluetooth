package com.example.btmodule.mylib.broadcast

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.btmodule.mylib.SwitchViewModel.SwitchBT

class MyBtReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val mBluetoothAdapter : BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if(mBluetoothAdapter.isEnabled){
            if (!SwitchBT.mCheckState!!.value) {
                Log.d("Broadcast", "in the adapter enabled broadcast")
                SwitchBT.myCheck!!.value = "On"
                SwitchBT.mCheckState!!.value = true
            }
        }
        else{
            SwitchBT.myCheck!!.value = "Off"
            SwitchBT.mCheckState!!.value = false
        }

    }
}