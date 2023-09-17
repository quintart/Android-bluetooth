package com.example.btmodule.mylib.SwitchViewModel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object SwitchBT : ViewModel(){
    var myCheck : MutableState<String>? = null
    var mCheckState : MutableState<Boolean>? = null
}