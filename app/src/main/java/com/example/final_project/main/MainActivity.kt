package com.example.final_project.main

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.btmodule.mylib.adapter.BtAdapter
import com.example.btmodule.mylib.broadcast.AvailableReceiver
import com.example.btmodule.mylib.broadcast.MyBtReceiver
import com.example.final_project.ui.theme.FinalprojectTheme

class MainActivity : ComponentActivity() {
    private val bluetoothAdapter : BtAdapter? = null
    private val bAdapter : BluetoothAdapter? = null
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(MyBtReceiver(),intentFilter)

        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        registerReceiver(AvailableReceiver(), filter)

        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN),0)

        setContent {
            FinalprojectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
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
                                Button(onClick = { bluetoothAdapter?.getAvail(applicationContext) }) {
                                    Text(text = "test")
                                }
                            }

                        }
                        ConnectScreen(applicationContext, application)
                    }
                }
            }
        }
    }

    override fun onPause() {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        )
        if (bAdapter!=null){
            if (bAdapter.isDiscovering){
                bAdapter.cancelDiscovery()
            }
        }
        super.onPause()
    }

    override fun onDestroy() {
        unregisterReceiver(MyBtReceiver())
        super.onDestroy()
    }


}

