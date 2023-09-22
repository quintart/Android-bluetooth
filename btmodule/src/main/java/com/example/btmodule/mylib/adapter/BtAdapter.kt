@file:Suppress("DEPRECATION")

package com.example.btmodule.mylib.adapter

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat

class BtAdapter {
    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    fun bAdapter(context: Context, enable : Boolean) {
        Log.d("context", "$context")
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
            val state = bluetoothAdapter.state
            Log.d("state", "$state")
        }
        else{
                bluetoothAdapter.disable()

        }


    }
    fun getPaired(context: Context) : ArrayList<BluetoothDevice> {
        val list : ArrayList<BluetoothDevice> = ArrayList()

        if (ActivityCompat.checkSelfPermission(
                 context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }
        val pairedDevices =
            bluetoothAdapter.bondedDevices

        if (pairedDevices.size > 0 ) {
            for (device: BluetoothDevice in pairedDevices) {
                val deviceName = device.name
                Log.d("device", deviceName)
                list.add(device)
            }
        }
        return list
    }
    fun getAvail(context: Context){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ){return }
        if (bluetoothAdapter.isEnabled)
            bluetoothAdapter.startDiscovery()
        Log.d("discover","started discovering")
    }

}
