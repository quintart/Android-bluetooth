@file:Suppress("DEPRECATION")

package com.example.final_project.main

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.btmodule.mylib.adapter.BtAdapter
import com.example.btmodule.mylib.SwitchViewModel.SwitchBT
import com.example.btmodule.mylib.SwitchViewModel.BtViewModel
import com.example.final_project.R

@Composable
fun ConnectScreen(context: Context, application: Application) {
    val vModel = BtViewModel(application)
    val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    val controller = LocalContext.current
    val card by vModel.list.observeAsState(emptyList())

//    vModel.fetchList()

    SwitchBT.myCheck = remember {
        mutableStateOf(
            value = if (mBluetoothAdapter.isEnabled) {
                "On"
            } else {
                "Off"
            }
        )
    }

    SwitchBT.mCheckState = remember {
        mutableStateOf(value = mBluetoothAdapter.isEnabled) }


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
    getPairedDevice(controller = controller, card = card, vModel)

    getAvailableDevice(controller = controller, card = card)

}

@Composable
fun getPairedDevice(controller: Context, card: List<BluetoothDevice>, vModel: BtViewModel){
    vModel.fetchList()
    if (ActivityCompat.checkSelfPermission(
            controller,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        LazyColumn{
            if (card.isNotEmpty() and SwitchBT.mCheckState!!.value) {
                item {
                    Text(
                        text = "Paired Devices",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(start = 20.dp, top = 30.dp, bottom = 10.dp),
                        fontWeight = FontWeight.Bold
                    )
                }

                items(card.size){

                    Row(modifier = Modifier
                        .padding(
                            start = 20.dp, top = 5.dp, bottom = 10.dp, end = 20.dp
                        )
                        .height(50.dp)
                        .clickable {
                            TODO("Add to Connect to the device")
                        }) {
                        Text(
                            text = card.elementAt(it).name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Log.d("type",card.elementAt(it).bluetoothClass.toString())
                        DisplayIcon(device = card.elementAt(it).bluetoothClass.toString() , modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterVertically) )
                    }
                }
            }
        }
    }
}

@Composable
fun getAvailableDevice(controller: Context, card: List<BluetoothDevice>) {
    if (ActivityCompat.checkSelfPermission(
            controller,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        LazyColumn {
            item {
                Text(
                    text = "Available Devices",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(start = 20.dp, top = 10.dp, bottom = 15.dp)
                )
            }

            if (card.isEmpty() and SwitchBT.mCheckState!!.value) {
                items(card.size) {
                    Row(
                        modifier = Modifier
                            .padding(
                                start = 20.dp, top = 5.dp, bottom = 10.dp, end = 20.dp
                            )
                            .height(50.dp)
                            .clickable {
                                TODO("Add The logic to pair the device")
                            }
                    ) {

                        Text(
                            text = card.elementAt(it).name,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )

                        Spacer(modifier = Modifier.weight(1f))

//                        displayIcon(
//                            device = BtObject.devices.value.elementAt(it)?.bluetoothClass.toString(),
//                            modifier = Modifier
//                                .size(24.dp)
//                                .align(Alignment.CenterVertically)
//                        )
                    }
                }
            } else {
                item {
                    Text(
                        text = "No devices found",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .padding(
                                start = 20.dp,
                            )
                            .height(50.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DisplayIcon(device: String, modifier: Modifier){
    when (device) {
        "240404" -> Image(
            painter = painterResource(id = R.drawable.round_headphones_24),
            contentDescription = null,
            modifier = modifier
        )

        "24010c" -> Image(
            painter = painterResource(id = R.drawable.laptop_24),
            contentDescription = null,
            modifier = modifier
        )

        "240704" -> Image(
            painter = painterResource(id = R.drawable.watch_24),
            contentDescription = null,
            modifier = modifier
        )

        "5a020c" -> Image(
            painter = painterResource(id = R.drawable.phone_android_24),
            contentDescription = null,
            modifier = modifier
        )

        "7a020c" -> Image(
            painter = painterResource(id = R.drawable.phone_iphone_24),
            contentDescription = null,
            modifier = modifier
        )

        else -> Image(painter = painterResource(id = R.drawable.device_unknown_24), contentDescription = null,
            modifier = modifier
        )
    }
}