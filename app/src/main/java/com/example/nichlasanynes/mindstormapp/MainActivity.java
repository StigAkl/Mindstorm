package com.example.nichlasanynes.mindstormapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button tryBluetoothButton;
    BluetoothSocket mmSocket = null;
    BluetoothSocket fallbackSocket;
    BluetoothDevice mmDevice = null;
    BluetoothAdapter mBluetoothAdapter;

    ImageView forward;
    ImageView backward;
    ImageView turnRight;
    ImageView turnLeft;

    //Buttons
    //ImageView forward = (ImageView) findViewById(R.id.forward);
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public void controlRobot(View view) {

        ImageView controlButton = (ImageView) view;
        String command = controlButton.getTag().toString();
        Toast.makeText(getApplicationContext(), command, Toast.LENGTH_SHORT).show();
        sendCommand(command);
    }

    public void sendCommand(String command) {
        // mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
//            mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(uuid);
//            Toast.makeText(this, "createRfcommSocketToServiceRecord " + uuid, Toast.LENGTH_SHORT).show();
//            mmSocket.connect();
//            Log.i("myStuff", "Conected OK!");
//            Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();

        //Protocol: If the robot receives a message starting with "#", the message is sent to
        //the robots Text to Speech module
        if(command.equals("textToSpeech")) {
            EditText textToSpeech = (EditText) findViewById(R.id.textToSpeechText);
            command = "#"+textToSpeech.getText().toString();
            Toast.makeText(getApplicationContext(), command, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "FallbackSocket is null", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showLoading();
        hideControls();

        try {
            Thread.sleep(3000);
        } catch(InterruptedException e) {
            Log.e("EXCEPTION", "Failed to sleep");
        }
        init();

        initListeners();
        hideLoading();
        showControls();

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
        //tryBluetoothButton = (Button) findViewById(R.id.);
       // initListeners();
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
                if (device.getName().equals("13516c2499dc")) {
                    mmDevice = device;
                    Log.i("myStuff", "Device equals " + device.getName());
                    Toast toast = Toast.makeText(this, "Device equals " + device.getName(), Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                } else {
                    Toast toast = Toast.makeText(this, "Penistrakter", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }


        try {
            mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(mmDevice, 1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Class<?> clazz = mmSocket.getRemoteDevice().getClass();
        Class<?>[] paramTypes = new Class<?>[]{Integer.TYPE};
        Method m = null;

        try {
            m = clazz.getMethod("createRfcommSocket", paramTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Object[] params = new Object[]{Integer.valueOf(1)};

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void hideControls(){
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.controls);
        layout.setVisibility(View.GONE);
    }

    public void showControls(){
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.controls);
        layout.setVisibility(View.VISIBLE);
    }

    public void showLoading(){
        ImageView ImageView = (ImageView) findViewById(R.id.forward);
        ImageView.setVisibility(View.VISIBLE);
    }

    public void hideLoading(){
        ImageView ImageView = (ImageView) findViewById(R.id.forward);
        //ImageView.setVisibility(View.GONE);
    }

   /* public void loadLoadingGif(){
        ImageView ImageView = (ImageView) findViewById(R.id.imageView3);
        ImageView.getSettings().setJavaScriptEnabled(true);
        ImageView.setImageViewClient(new ImageViewClient());
        ImageView.loadUrl("http://www.lettersmarket.com/uploads/lettersmarket/blog/loaders/common_blue/ajax_loader_blue_128.gif");
        ImageView.setVisibility(View.VISIBLE);
    }*/

    public void initListeners() {
        forward = (ImageView) findViewById(R.id.forward);
        backward = (ImageView) findViewById(R.id.backward);
        turnRight = (ImageView) findViewById(R.id.turnRight);
        turnLeft = (ImageView) findViewById(R.id.turnLeft);

        forward.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action  = event.getAction();
                switch(action) {
                    case MotionEvent.ACTION_DOWN:
                        sendCommand(getResources().getString(R.string.forward));
                        Toast.makeText(getApplicationContext(), "Forward", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_UP:
                        sendCommand(getResources().getString(R.string.stopMotors));
                        Toast.makeText(getApplicationContext(), "Stop motors", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });

        turnRight.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action  = event.getAction();
                switch(action) {
                    case MotionEvent.ACTION_DOWN:
                        sendCommand(getResources().getString(R.string.turnRight));
                        Toast.makeText(getApplicationContext(), "Forward", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_UP:
                        sendCommand(getResources().getString(R.string.stopMotors));
                        Toast.makeText(getApplicationContext(), "Stop motors", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });

        backward.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action  = event.getAction();
                switch(action) {
                    case MotionEvent.ACTION_DOWN:
                        sendCommand(getResources().getString(R.string.backward));
                        Toast.makeText(getApplicationContext(), "backward", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_UP:
                        sendCommand(getResources().getString(R.string.stopMotors));
                        Toast.makeText(getApplicationContext(), "Stop motors", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });

        turnLeft.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action  = event.getAction();
                switch(action) {
                    case MotionEvent.ACTION_DOWN:
                        sendCommand(getResources().getString(R.string.turnLeft));
                        Toast.makeText(getApplicationContext(), "Forward", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_UP:
                        sendCommand(getResources().getString(R.string.stopMotors));
                        Toast.makeText(getApplicationContext(), "Stop motors", Toast.LENGTH_SHORT).show();
                        sendCommand(getResources().getString(R.string.stopMotors));
                        break;
                }

                return true;
            }
        });


    }
}