package com.example.nichlasanynes.mindstormapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button tryBluetoothButton;
    BluetoothSocket mmSocket = null;
    BluetoothDevice mmDevice = null;
    BluetoothAdapter mBluetoothAdapter;

    public void tryBT(View view) {
        Log.i("myStuff", "Button Clicked");
        sendBtMsg("a");
    }

    public void sendBtMsg(String msg2send) {

        UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

        try {
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            Log.i("myStuff", "Conected OK!");

        } catch (IOException e) { }{

            Log.i("myStuff", "EXCEPTION THROWN");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tryBluetoothButton = (Button) findViewById(R.id.tryBluetoothButton);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
            Log.i("myStuff", "Bluetooth Enabled");
        } else {
            Log.i("myStuff", "Bluetooth Already Enabled");
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("raspberrypi"))
                {
                    mmDevice = device;
                    Log.i("myStuff", "Device equals " + device.getName());
                    break;
                }
            }
        }
    }
}