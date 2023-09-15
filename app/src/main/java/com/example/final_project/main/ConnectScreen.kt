@file:Suppress("DEPRECATION")

package com.example.final_project.main

import android.bluetooth.BluetoothAdapter
import android.content.Context
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
import com.example.final_project.adapter.BtAdapter
import com.example.final_project.viewModel.switchBT

@Composable
fun ConnectScreen(context: Context) {
    val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    switchBT.myCheck = remember {
        mutableStateOf(
            value = if (mBluetoothAdapter.isEnabled) {
                "On"
            } else {
                "Off"
            }
        )
    }

    switchBT.mCheckState = remember { mutableStateOf(value = mBluetoothAdapter.isEnabled) }


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
            text = switchBT.myCheck!!.value, /*color = mycolor.value,*/
            fontSize = 25.sp,
            modifier = Modifier.align(Alignment.CenterVertically),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(checked = switchBT.mCheckState!!.value,
            onCheckedChange = {
                if (switchBT.myCheck!!.value == "On") {
                    switchBT.myCheck!!.value= "Off"
                    switchBT.mCheckState!!.value = false
                    BtAdapter().bAdapter(context, true)
                } else {
                    switchBT.myCheck!!.value= "On"
                    switchBT.mCheckState!!.value = true
                    BtAdapter().bAdapter(context, false)
                }

            })
    }
}