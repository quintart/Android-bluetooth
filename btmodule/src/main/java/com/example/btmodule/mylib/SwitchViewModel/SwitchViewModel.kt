package com.example.btmodule.mylib.SwitchViewModel

import android.bluetooth.BluetoothDevice
data class ScreenState(
    var btState: Boolean = false,
    var discoverState : Boolean = false,
    var pairedDevicesList: Set<BluetoothDevice> = mutableSetOf(),
    var availableDeviceList : Set<BluetoothDevice> = mutableSetOf()
)