package com.example.btmodule.mylib.SwitchViewModel

import android.Manifest
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

    fun changeBtAction(){
        bluetoothAdapter!!.changeBtAction()
    }

    fun fetchList() {
        if (bluetoothAdapter!!.isBtOn()) {
            screenState.update { screenState ->
                screenState.copy(pairedDevicesList = bluetoothAdapter!!.getPaired())
            }
        } else {
            screenState.update { screenState ->
                screenState.copy(pairedDevicesList = mutableSetOf())
            }
        }
    }

    fun fetchAvailList() {
        bluetoothAdapter!!.getAvail()
    }

    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            if (bluetoothAdapter.isBtOn()) {
                screenState.update { screenState ->
                    screenState.copy(
                        btState = true,
                        pairedDevicesList = bluetoothAdapter.getPaired()
                    )
                }

            } else {
                screenState.update { screenState ->
                    screenState.copy(btState = false, pairedDevicesList = mutableSetOf())
                }
            }
        }
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
                                screenState.value.availableDeviceList =
                                    screenState.value.availableDeviceList + device
                            }
                        }
                    }
                }
            }
        }
    }
}