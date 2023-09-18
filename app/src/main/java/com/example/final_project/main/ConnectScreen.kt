@file:Suppress("DEPRECATION")

package com.example.final_project.main

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btmodule.mylib.adapter.BtAdapter
import com.example.btmodule.mylib.SwitchViewModel.SwitchBT

@Composable
fun ConnectScreen(context: Context) {
    val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    SwitchBT.myCheck = remember {
        mutableStateOf(
            value = if (mBluetoothAdapter.isEnabled) {
                "On"
            } else {
                "Off"
            }
        )
    }

    SwitchBT.mCheckState = remember { mutableStateOf(value = mBluetoothAdapter.isEnabled) }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(
                start = 20.dp, top = 10.dp,
                bottom = 10.dp, end = 20.dp
            )
    ) {

        Text(
            text = SwitchBT.myCheck!!.value, 
            fontSize = 25.sp,
            modifier = Modifier.align(Alignment.CenterVertically),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(checked = SwitchBT.mCheckState!!.value,
            onCheckedChange = {
                if (SwitchBT.myCheck!!.value == "On") {
                    SwitchBT.myCheck!!.value= "Off"
                    SwitchBT.mCheckState!!.value = false
                    BtAdapter().bAdapter(context, true)
                    Log.d("navigation", "toggled")
                } else {
                    BtAdapter().bAdapter(context, false)
                    SwitchBT.myCheck!!.value= "On"
                    SwitchBT.mCheckState!!.value = true

                }

            })
    }
}