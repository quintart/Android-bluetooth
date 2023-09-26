package com.example.btmodule.mylib.SwitchViewModel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
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


    private val screenState = MutableStateFlow(ScreenState())
    val state: StateFlow<ScreenState> = screenState.asStateFlow()

    init {
        screenState.value.btState = bluetoothAdapter.isBtOn()
        screenState.value.pairedDevicesList = bluetoothAdapter.getPaired()
    }

    fun changeBtAction() {
        bluetoothAdapter!!.changeBtAction()
    }

    fun deletePaired(device: BluetoothDevice) {
        screenState.update { screen ->
            screen.copy(pairedDevicesList = screen.pairedDevicesList - device)
        }
        device.javaClass.getMethod("removeBond").invoke(device)
    }

    fun fetchList() {
        if (bluetoothAdapter!!.isBtOn()) {
            screenState.update { screen ->
                screen.copy(
                    pairedDevicesList = bluetoothAdapter!!.getPaired()
                )
            }
        } else {
            screenState.update { screen ->
                screen.copy(pairedDevicesList = mutableSetOf())
            }
        }
    }

    fun fetchAvailList() {
        bluetoothAdapter!!.getAvail()
        screenState.update { screen ->
            screen.copy(discoverState = true,)
        }

    }

    fun stopScan() {
        bluetoothAdapter.stopScan()
        screenState.update { screen ->
            screen.copy(discoverState = false)
        }
    }

    fun editName(name: String) {
        bluetoothAdapter.editName(name)
    }

    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            if (bluetoothAdapter.isBtOn()) {
                screenState.update { screen ->
                    screen.copy(
                        btState = true,
                        pairedDevicesList = bluetoothAdapter.getPaired()
                    )
                }

            } else {
                screenState.update { screen ->
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
            val action: String? = intent?.action

            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (ActivityCompat.checkSelfPermission(
                            application,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (device?.name != null) {
                            if (device !in screenState.value.pairedDevicesList) {
                                screenState.update { screen ->
                                    screen.copy(
                                        availableDeviceList = screenState.value.availableDeviceList + device
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
                            screenState.update { screen ->
                                screen.copy(
                                    pairedDevicesList = bluetoothAdapter.getPaired(),
                                    availableDeviceList = screenState.value.availableDeviceList - device

                                )
                            }
                            Log.d("ListUpdate",device.name)
                        }
                    }
                }

                BluetoothDevice.ACTION_NAME_CHANGED -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (ActivityCompat.checkSelfPermission(
                            application,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (device != null) {
                            Log.d("name Change",device.name)
                        }
                    }
                }
            }
        }
    }
}
