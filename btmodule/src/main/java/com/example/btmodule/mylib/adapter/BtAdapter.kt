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
            val pairedDevices = bluetoothAdapter.bondedDevices
//            pairedDevices?.forEach { device ->
            val state = bluetoothAdapter.state
            Log.d("state", "$state")

//            viewLifecycleOwner.lifecycleScope.launch {
//                // repeatOnLifecycle launches the block in a new coroutine every time the
//                // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
//                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                    // Trigger the flow and start listening for values.
//                    // This happens when lifecycle is STARTED and stops
//                    // collecting when the lifecycle is STOPPED
//                    viewModel.someDataFlow.collect {
//                        // Process item
//                    }
//                }
//            }

//            val list : ArrayList<BluetoothDevice> = ArrayList()
            if (pairedDevices.size > 0 ) {
                for (device: BluetoothDevice in pairedDevices) {
                    val deviceName = device.name
                    Log.d("device", "$deviceName")
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
