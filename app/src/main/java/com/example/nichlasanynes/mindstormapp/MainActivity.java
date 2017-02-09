package com.example.nichlasanynes.mindstormapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button tryBluetoothButton;
    BluetoothSocket mmSocket = null;
    BluetoothDevice mmDevice = null;
    BluetoothAdapter mBluetoothAdapter;

    public void controlRobot(View view) {

        Button btn = (Button) view; 
        String command = btn.getTag().toString(); 
        
        sendCommand(command);
    }

    public void sendCommand(String command) {
        UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

        try {
           // mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
//            mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(uuid);
//            Toast.makeText(this, "createRfcommSocketToServiceRecord " + uuid, Toast.LENGTH_SHORT).show();
//            mmSocket.connect();
//            Log.i("myStuff", "Conected OK!");
//            Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();

            mmSocket =(BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,1);
            Log.d("DEBUG CRASH", "mmSocket");
            Class<?> clazz = mmSocket.getRemoteDevice().getClass();
            Log.d("DEBUG CRASH", "clazz");
            Class<?>[] paramTypes = new Class<?>[] {Integer.TYPE};
            Log.d("DEBUG CRASH", "paramTypes");
            Method m = clazz.getMethod("createRfcommSocket", paramTypes);
            Log.d("DEBUG CRASH", "getMethod");
            Object[] params = new Object[] {Integer.valueOf(1)};
            Log.d("DEBUG CRASH", "newobject[]");

            BluetoothSocket fallbackSocket = (BluetoothSocket) m.invoke(mmSocket.getRemoteDevice(), params);
            Log.d("DEBUG CRASH", "fallbackSocket");
            fallbackSocket.connect();

            if(fallbackSocket != null) {

                try {
                    fallbackSocket.getOutputStream().write(command.getBytes());
                    fallbackSocket.getOutputStream().flush();
                    fallbackSocket.close();
                } catch(IOException e) {
                    Log.e("IOExceptoin", e.getMessage().toString());
                }

            } else {
                Log.e("ERROR", "fallbackSocket is null");
            }

        } catch (IOException e) {

            Log.i("myStuff", "EXCEPTION THROWN: " + e.getMessage().toString());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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
                if (device.getName().equals("13516c2499dc"))
                {
                    mmDevice = device;
                    Log.i("myStuff", "Device equals " + device.getName());
                    Toast toast = Toast.makeText(this, "Device equals " + device.getName(), Toast.LENGTH_LONG);
                    toast.show();
                    break;
                } else {
                    Toast toast = Toast.makeText(this, "Penistrakter", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
    }
}