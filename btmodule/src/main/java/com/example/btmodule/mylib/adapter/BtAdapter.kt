@file:Suppress("DEPRECATION")

package com.example.btmodule.mylib.adapter

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import android.content.Intent

class BtAdapter(private val context: Context) {
    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    fun changeBtAction(){
        if (ActivityCompat.checkSelfPermission(
                /* context = */ context,
                /* permission = */ Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (bluetoothAdapter.isEnabled) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU )
                {
                    Toast.makeText(context,"Turn off bluetooth from you device settings.", Toast.LENGTH_LONG).show()
                }else{
                    bluetoothAdapter.disable()
                }
            } else {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(enableBtIntent)
                }else{
                    bluetoothAdapter.enable()
                }
            }
        }
    }

    fun isBtOn() : Boolean{
        return bluetoothAdapter.isEnabled
    }

    fun getPaired() : Set<BluetoothDevice> {
        if (ActivityCompat.checkSelfPermission(
                 context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }
        val pairedDevices = bluetoothAdapter.bondedDevices
        return pairedDevices
    }
    fun getAvail(){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (bluetoothAdapter.isDiscovering) {
                bluetoothAdapter.cancelDiscovery()
            }
            bluetoothAdapter.startDiscovery()
        }
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bluetoothAdapter.cancelDiscovery()    }

    @SuppressLint("MissingPermission")
    fun editName(name : String){
        bluetoothAdapter.name = name
    }

}
