@file:Suppress("DEPRECATION")

package com.example.final_project.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.btmodule.mylib.adapter.BtAdapter
import com.example.btmodule.mylib.SwitchViewModel.BtViewModel
import com.example.btmodule.mylib.SwitchViewModel.ScreenState
import com.example.final_project.R

@Composable
fun Navigation(cont:Context, viewModel: BtViewModel) {
    val navController = rememberNavController()
    val device : BluetoothDevice? = null

    NavHost(navController = navController, startDestination = "ConnectScreen" ){
        composable(route = "ConnectScreen") {
            ConnectScreen(viewModel, navController = navController)
        }
        composable(route = "deviceDetail/{device}"){
            deviceDetails(device = device, context = cont)
        }

    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ConnectScreen( viewModel: BtViewModel = hiltViewModel(), navController: NavController) {
    val controller = LocalContext.current

    val state = viewModel.state.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 20.dp, bottom = 20.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Bluetooth",
                fontSize = 35.sp, fontWeight = FontWeight.Medium
            )
        }

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
                text = if (viewModel.state.value.btState) "On" else "Off",
                fontSize = 25.sp,
                modifier = Modifier.align(Alignment.CenterVertically),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = state.value.btState,
                onCheckedChange = {
                    viewModel.changeBtAction()
                })
        }

        getPairedDevice(controller = controller, viewModel, navController)

    getAvailableDevice(controller = controller, viewModel)
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun getPairedDevice(controller: Context, vModel: BtViewModel, navController: NavController){
    val state = vModel.state.collectAsState()
    vModel.fetchList()
    if (ActivityCompat.checkSelfPermission(
            controller,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    ) {

            Text(
                text = "Paired Devices",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 20.dp, top = 30.dp, bottom = 10.dp),
                fontWeight = FontWeight.Bold
            )
        LazyColumn{
            if (state.value.btState and
                state.value.pairedDevicesList.isNotEmpty()) {

                items(state.value.pairedDevicesList.size){

                    Row(modifier = Modifier
                        .padding(
                            start = 20.dp, top = 5.dp, bottom = 10.dp, end = 20.dp
                        )
                        .height(50.dp)
                        .clickable {
                            navController.navigate(
                                "deviceDetail/${
                                    state.value.pairedDevicesList.elementAt(
                                        it
                                    )
                                }"
                            )
                        }) {
                        Text(
                            text = state.value.pairedDevicesList.elementAt(it).name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Log.d("type",vModel.state.value.pairedDevicesList
                            .elementAt(it).bluetoothClass.toString())
                        DisplayIcon(device = vModel.state.value.pairedDevicesList
                            .elementAt(it).bluetoothClass.toString() ,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterVertically)
                                .clickable { TODO() })
                    }
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun getAvailableDevice(controller: Context, vModel: BtViewModel) {
    val state = vModel.state.collectAsState()
    vModel.fetchAvailList()
    if (ActivityCompat.checkSelfPermission(
            controller,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    ) {

            Text(
                text = "Available Devices",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 20.dp, top = 10.dp, bottom = 15.dp)
            )
        LazyColumn {
            if (state.value.btState and
                state.value.availableDeviceList.isNotEmpty()) {
                items(state.value.availableDeviceList.size) {
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
                            text = state.value.availableDeviceList.elementAt(it).name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        DisplayIcon(
                            device = vModel.state.value.availableDeviceList
                                .elementAt(it).bluetoothClass.toString(),
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            } else {
                item {
                    Text(
                        text = "No devices found",
                        fontSize = 20.sp,
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

@Composable
fun deviceDetails(device: BluetoothDevice?, context: Context){
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    )
    Log.d("Detail", device!!.name)
    Text(text = "hello",
        fontSize = 30.sp,
        )
}