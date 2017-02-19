package com.example.nichlasanynes.mindstormapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by Stig on 17.02.2017.
 */

public class Utils {

    public static void wait(int millis) {
            try {
                Thread.sleep(millis);
            } catch(InterruptedException e) {
                Log.e("EXCEPTION", "Failed to sleep");
            }
    }

    public static BluetoothSocket createFallbackSocket(BluetoothSocket socket, BluetoothDevice device) {
        try {
            socket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Class<?> clazz = socket.getRemoteDevice().getClass();
        Class<?>[] paramTypes = new Class<?>[]{Integer.TYPE};
        Method m = null;

        try {
            m = clazz.getMethod("createRfcommSocket", paramTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Object[] params = new Object[]{Integer.valueOf(1)};

        BluetoothSocket fallbackSocket = null;
        try {
            fallbackSocket = (BluetoothSocket) m.invoke(socket.getRemoteDevice(), params);
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

        return fallbackSocket;
    }

    public static BluetoothDevice getDevice(BluetoothAdapter mBluetoothAdapter) {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("13516c2499dc")) {
                    Log.i("myStuff", "Device equals " + device.getName());
                    return device;
                } else {
                    Log.i("INFO", "Robot not paired");
                }
            }
        }
        return null;
    }

    public static void sendCommand(String command, String textToSpeech, BluetoothSocket fallbackSocket) {
        if(command.equals("textToSpeech")) {
            command = "#"+textToSpeech;
        }

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
        }

    }
}
