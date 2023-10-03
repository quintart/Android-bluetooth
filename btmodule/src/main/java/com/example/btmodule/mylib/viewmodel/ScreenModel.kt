package com.example.btmodule.mylib.viewmodel

import android.bluetooth.BluetoothDevice
data class ScreenModel(
    var btState: Boolean = false,
    var discoverState : Boolean = false,
    var deviceName : String = "default",
    var pairedDevicesList: Set<BluetoothDevice> = mutableSetOf(),
    var availableDeviceList : Set<BluetoothDevice> = mutableSetOf()
)