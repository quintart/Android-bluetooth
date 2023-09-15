package com.example.final_project.viewModel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object switchBT : ViewModel(){
    var myCheck : MutableState<String>? = null
    var mCheckState : MutableState<Boolean>? = null
}