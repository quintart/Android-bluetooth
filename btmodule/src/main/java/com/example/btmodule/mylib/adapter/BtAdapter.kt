@file:Suppress("DEPRECATION")

package com.example.btmodule.mylib.adapter

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat


class BtAdapter {
    fun bAdapter(context: Context, enable : Boolean) {
        Log.d("context", "$context")
        val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!enable) {
            println("Device not compatible")
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                 return
            }
            bluetoothAdapter.enable()
            Log.d("adapter","enabled the bluetooth")
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            val list : ArrayList<BluetoothDevice> = ArrayList()
            if (!pairedDevices?.isEmpty()!!) {
                for (device: BluetoothDevice in pairedDevices) {
                    list.add(device)
                    Log.i("device", ""+device)
                }
            } else {
                Toast.makeText(context,"no paired bluetooth devices found", Toast.LENGTH_SHORT).show()
            }
        }
        else{
                bluetoothAdapter.disable()

        }
    }
}
