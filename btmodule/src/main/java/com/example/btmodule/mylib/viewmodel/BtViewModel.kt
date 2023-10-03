@file:Suppress("DEPRECATION")

package com.example.btmodule.mylib.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.example.btmodule.mylib.adapter.BtAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BtViewModel @Inject constructor
    (
    private val bluetoothAdapter : BtAdapter,
    private val application: Application) : ViewModel() {


    private val screenModel = MutableStateFlow(ScreenModel())
    val state: StateFlow<ScreenModel> = screenModel.asStateFlow()

    init {
        screenModel.value.btState = bluetoothAdapter.isBtOn()
        screenModel.value.pairedDevicesList = bluetoothAdapter.getPaired()
    }

    fun changeBtAction() {
        bluetoothAdapter.changeBtAction()
    }

    fun deletePaired(device: BluetoothDevice) {
        screenModel.update { screen ->
            screen.copy(pairedDevicesList = screen.pairedDevicesList - device)
        }
        device.javaClass.getMethod("removeBond").invoke(device)
    }

    fun fetchList() {
        if (bluetoothAdapter.isBtOn()) {
            screenModel.update { screen ->
                screen.copy(
                    pairedDevicesList = bluetoothAdapter.getPaired()
                )
            }
        } else {
            screenModel.update { screen ->
                screen.copy(pairedDevicesList = mutableSetOf())
            }
        }
    }

    fun fetchAvailList() {
        bluetoothAdapter.getAvail()
        screenModel.update { screen ->
            screen.copy(discoverState = true)
        }

    }

    fun stopScan() {
        bluetoothAdapter.stopScan()
        screenModel.update { screen ->
            screen.copy(discoverState = false)
        }
    }

    fun editName(name: String) {
        bluetoothAdapter.editDeviceName(name)
    }

    fun discoverable(){
        bluetoothAdapter.discoverable()
    }

    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            if (bluetoothAdapter.isBtOn()) {
                screenModel.update { screen ->
                    screen.copy(
                        btState = true,
                        pairedDevicesList = bluetoothAdapter.getPaired()
                    )
                }

            } else {
                screenModel.update { screen ->
                    screen.copy(btState = false, pairedDevicesList = mutableSetOf())
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun connect(device: BluetoothDevice) {
        device.createBond()
    }

    val btDeviceReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {

            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (ActivityCompat.checkSelfPermission(
                            application,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (device?.name != null) {
                            if (device !in screenModel.value.pairedDevicesList) {
                                screenModel.update { screen ->
                                    screen.copy(
                                        availableDeviceList = screenModel.value.availableDeviceList + device
                                    )
                                }
                            }
                        }
                    }
                }

                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (ActivityCompat.checkSelfPermission(
                            application,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (device != null) {
                            screenModel.update { screen ->
                                screen.copy(
                                    pairedDevicesList = bluetoothAdapter.getPaired(),
                                    availableDeviceList = screenModel.value.availableDeviceList - device

                                )
                            }
                            Log.d("ListUpdate",device.name)
                        }
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Log.d("Discovery", "Started")
                    if (bluetoothAdapter.isDiscovering()) {
                        screenModel.update { screenState ->
                            screenState.copy(discoverState = true)
                        }
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.d("Discovery", "Finished")

                    if (!bluetoothAdapter.isDiscovering()) {
                        screenModel.update { screenState ->
                            screenState.copy(discoverState = false)
                        }
                    }
                }

                BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED -> {
                    screenModel.update { screenState ->
                        screenState.copy(deviceName = bluetoothAdapter.getDeviceName())
                    }
                }
            }
        }
    }
}
