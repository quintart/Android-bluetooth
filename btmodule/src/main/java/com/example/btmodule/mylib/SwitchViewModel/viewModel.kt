package com.example.btmodule.mylib.SwitchViewModel

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

class BtViewModel(application: Application) : ViewModel() {
    private val mRepository = Repository(application)
    private val _list = MutableLiveData<ArrayList<BluetoothDevice>>()
    val list : LiveData<ArrayList<BluetoothDevice>> = _list
    fun fetchList() {
        viewModelScope.launch {
            try {
                val card = mRepository.getList()
                _list.value = card
            } catch (e : Exception){
                Log.d("Exception", e.toString())
            }
        }
    }
}