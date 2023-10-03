@file:Suppress("DEPRECATION")

package com.example.final_project.main

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothClass
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.btmodule.mylib.viewmodel.BtViewModel
import com.example.final_project.R

@Composable
fun Navigation(cont:Context, viewModel: BtViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "ConnectScreen" ){
        composable(route = "ConnectScreen") {
            ConnectScreen(viewModel, navController = navController)
        }
        composable(route = "editDeviceName/{deviceName}",
            arguments = listOf(
                navArgument("deviceName"){
                    type = NavType.StringType
                }
            )
        )
            {entry ->
                entry.arguments?.getString("deviceName")?.let { EditName(deviceName = it,viewModel, navController) }

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
            if(state.value.btState){
                Spacer(modifier = Modifier.weight(1f))
                Log.d("SCAN", state.value.discoverState.toString())
                if(!state.value.discoverState) {
                    Button(onClick = { viewModel.fetchAvailList()
                    }) {
                        Text(text = "Scan")
                    }

                }else{
                    Button(onClick = {
                        viewModel.stopScan()
                    }) {
                        Text(text = "Stop")
                    }
                }
            }
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
        if (state.value.btState){
            MakeDeviceDiscoverable(viewModel, navController)
            GetPairedDevice(controller = controller, viewModel, navController)
            GetAvailableDevice(controller = controller, viewModel)
        }

    }
}

@Composable
fun MakeDeviceDiscoverable(vModel: BtViewModel, navController: NavController){
    val state = vModel.state.collectAsState()
    Row(modifier = Modifier
        .padding(start = 20.dp, top = 30.dp, end = 20.dp)) {
        Column(modifier = Modifier
            .clickable { navController.navigate("editDeviceName/${state.value.deviceName}") }) {
            Text(text = "Your Device")
            Text(text = state.value.deviceName,
                fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { vModel.discoverable() }) {
            Text(text = "Make Discoverable")
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun GetPairedDevice(controller: Context, vModel: BtViewModel, navController: NavController){
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
                    ) {
                        DisplayIcon(device = vModel.state.value.pairedDevicesList
                            .elementAt(it).bluetoothClass.deviceClass ,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterVertically)
                                .clickable { TODO() })
                        Text(
                            text = state.value.pairedDevicesList.elementAt(it).name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Log.d("type",vModel.state.value.pairedDevicesList
                            .elementAt(it).bluetoothClass.toString())

                        Image(
                            painter = painterResource(id = R.drawable.delete_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(26.dp)
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    try {
                                        vModel.deletePaired(
                                            state.value.pairedDevicesList.elementAt(
                                                it
                                            )
                                        )
                                    } catch (e: Exception) {
                                        Log.d("Tag", "Delete paired device")
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun GetAvailableDevice(controller: Context, vModel: BtViewModel) {
    val state = vModel.state.collectAsState()
//    vModel.fetchAvailList()
    if (ActivityCompat.checkSelfPermission(
            controller,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    ) {

            Text(
                text = "Available Devices",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 20.dp, top = 10.dp, bottom = 15.dp),
                fontWeight = FontWeight.Bold
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
                                vModel.connect(state.value.availableDeviceList.elementAt(it))
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
                                .elementAt(it).bluetoothClass.deviceClass,
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
fun DisplayIcon(device: Int, modifier: Modifier){
    when (device) {
        BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES -> Image(
            painter = painterResource(id = R.drawable.round_headphones_24),
            contentDescription = null,
            modifier = modifier
        )

        BluetoothClass.Device.COMPUTER_LAPTOP -> Image(
            painter = painterResource(id = R.drawable.laptop_24),
            contentDescription = null,
            modifier = modifier
        )

        BluetoothClass.Device.WEARABLE_WRIST_WATCH -> Image(
            painter = painterResource(id = R.drawable.watch_24),
            contentDescription = null,
            modifier = modifier
        )

        BluetoothClass.Device.PHONE_SMART -> Image(
            painter = painterResource(id = R.drawable.phone_android_24),
            contentDescription = null,
            modifier = modifier
        )
        else -> Image(painter = painterResource(id = R.drawable.device_unknown_24), contentDescription = null,
            modifier = modifier
        )
    }
}

@Composable
fun deviceDetails( context: Context, name : String?,classType : Int?, navController: NavController, viewModel: BtViewModel){
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    )
        name?.let { Log.d("Detail", it) }
    Column(modifier = Modifier
        .fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.keyboard_arrow_left_24),
            contentDescription = null,
            modifier = Modifier
                .size(46.dp)
                .padding(start = 4.dp, top = 3.dp)
                .align(Alignment.Start)
                .clickable {
                    navController.navigate("ConnectScreen")
                }
        )

        Text(
            text = "$name",
            fontSize = 30.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(25.dp)

        )
        if (classType != null) {
            DisplayIcon(
                device = classType,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }


            Row (modifier = Modifier
                .padding(
                    start = 20.dp, top = 60.dp
                )
                .height(50.dp)){
                Button(onClick = { navController.navigate("editDeviceName/${name}") }) {
                    Icon(painter = painterResource(id = R.drawable.edit_24), contentDescription = null)
                    Text(text = "Rename")
                }

                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.bluetooth_24), contentDescription = null)
                    Text(text = "Connect")
                }

//                Spacer(modifier = Modifier.weight(1f))
//                Button(onClick = { /*TODO*/ }) {
//                    Icon(painter = painterResource(id = R.drawable.arrow_outward_24), contentDescription = null)
//                    Text(text = "Unpair")
//                }

            }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditName(deviceName : String, viewModel: BtViewModel, navController: NavController){
    var text by remember{
        mutableStateOf(deviceName)
    }

    TextField(value = text ,
        onValueChange = {
            text = it
        },
        modifier = Modifier
            .fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    Button(onClick = {
        viewModel.editName(text)
        navController.navigate("ConnectScreen")
    }, modifier = Modifier
        .padding(50.dp)) {
        Text(text = "Set Name")
    }
}