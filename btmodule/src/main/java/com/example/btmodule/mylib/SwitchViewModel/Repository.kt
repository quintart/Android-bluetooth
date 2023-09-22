package com.example.btmodule.mylib.SwitchViewModel

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.example.btmodule.mylib.adapter.BtAdapter

class Repository(context: Context) {
    private var obj : BtAdapter = BtAdapter()
    private val cont = context
    fun getList(): ArrayList<BluetoothDevice>{
        return obj.getPaired(cont)
    }
}