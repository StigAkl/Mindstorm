package com.example.nichlasanynes.mindstormapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button tryBluetoothButton;
    BluetoothSocket mmSocket = null;
    BluetoothSocket fallbackSocket;
    BluetoothDevice mmDevice = null;
    BluetoothAdapter mBluetoothAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public void controlRobot(View view) {

        Button btn = (Button) view;
        String command = btn.getTag().toString();
        Toast.makeText(getApplicationContext(), command, Toast.LENGTH_SHORT).show();

        sendCommand(command);
    }

    public void sendCommand(String command) {
        UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

        // mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
//            mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(uuid);
//            Toast.makeText(this, "createRfcommSocketToServiceRecord " + uuid, Toast.LENGTH_SHORT).show();
//            mmSocket.connect();
//            Log.i("myStuff", "Conected OK!");
//            Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();

        if (fallbackSocket != null) {

            try {
                fallbackSocket.getOutputStream().write(command.getBytes());
                fallbackSocket.getOutputStream().flush();
               // fallbackSocket.close();
            } catch (IOException e) {
                Log.e("IOExceptoin", "Error message: " + e.getMessage().toString());
            }

        } else {
            Log.e("ERROR", "fallbackSocket is null");
            Toast.makeText(this, "FallbackSocket is null", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void init() {
        tryBluetoothButton = (Button) findViewById(R.id.tryBluetoothButton);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        boolean connectedToRobot = false;

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
            Log.i("myStuff", "Bluetooth Enabled");
            Toast toast = Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_LONG);
            toast.show();
        } else {
            Log.i("myStuff", "Bluetooth Already Enabled");
            Toast toast = Toast.makeText(this, "Bluetooth Already Enabled", Toast.LENGTH_LONG);
            toast.show();

        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("13516c2499dc")) {
                    mmDevice = device;
                    Log.i("myStuff", "Device equals " + device.getName());
                    connectedToRobot = true;
                    Toast toast = Toast.makeText(this, "Device equals " + device.getName(), Toast.LENGTH_LONG);
                    toast.show();
                    break;
                } else {
                    Toast toast = Toast.makeText(this, "Penistrakter", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }


        if(connectedToRobot) {

            try {
                mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(mmDevice, 1);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Log.d("DEBUG CRASH", "mmSocket");
            Class<?> clazz = mmSocket.getRemoteDevice().getClass();
            Log.d("DEBUG CRASH", "clazz");
            Class<?>[] paramTypes = new Class<?>[]{Integer.TYPE};
            Log.d("DEBUG CRASH", "paramTypes");
            Method m = null;
            try {
                m = clazz.getMethod("createRfcommSocket", paramTypes);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Log.d("DEBUG CRASH", "getMethod");
            Object[] params = new Object[]{Integer.valueOf(1)};
            Log.d("DEBUG CRASH", "newobject[]");

            fallbackSocket = null;
            try {
                fallbackSocket = (BluetoothSocket) m.invoke(mmSocket.getRemoteDevice(), params);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            Log.d("DEBUG CRASH", "fallbackSocket");
            try {
                fallbackSocket.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
}