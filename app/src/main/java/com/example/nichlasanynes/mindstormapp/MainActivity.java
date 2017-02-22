package com.example.nichlasanynes.mindstormapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    Button tryBluetoothButton;
    BluetoothSocket mmSocket = null;
    BluetoothSocket fallbackSocket;
    BluetoothDevice mmDevice = null;
    BluetoothAdapter mBluetoothAdapter;

    //buttons to control the robot
    ImageView forward;
    ImageView backward;
    ImageView turnRight;
    ImageView turnLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* showLoading();
        hideControls();

        Utils.wait(3000);

        init();
        initListeners();
        hideLoading();
        showControls();
        */

    }

    public void init() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
            Log.i("myStuff", "Bluetooth Enabled");
        } else {
            Log.i("myStuff", "Bluetooth Already Enabled");
        }

        mmDevice = Utils.getDevice(mBluetoothAdapter);
        fallbackSocket = Utils.createFallbackSocket(mmSocket, mmDevice);
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
                        Utils.sendCommand(getResources().getString(R.string.forward), "", fallbackSocket);
                        break;
                    case MotionEvent.ACTION_UP:
                        Utils.sendCommand(getResources().getString(R.string.stopMotors), "", fallbackSocket);
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
                        Utils.sendCommand(getResources().getString(R.string.turnRight), "", fallbackSocket);
                        break;
                    case MotionEvent.ACTION_UP:
                        Utils.sendCommand(getResources().getString(R.string.stopMotors), "", fallbackSocket);
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
                        Utils.sendCommand(getResources().getString(R.string.backward), "", fallbackSocket);
                        break;
                    case MotionEvent.ACTION_UP:
                        Utils.sendCommand(getResources().getString(R.string.stopMotors), "", fallbackSocket);
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
                        Utils.sendCommand(getResources().getString(R.string.turnLeft), "", fallbackSocket);
                        break;
                    case MotionEvent.ACTION_UP:
                        Utils.sendCommand(getResources().getString(R.string.stopMotors), "", fallbackSocket);
                        break;
                }

                return true;
            }
        });
    }


    public void controlRobot(View view) {
        ImageView controlButton = (ImageView) view;
        String command = controlButton.getTag().toString();
        EditText textToSpeech = (EditText) findViewById(R.id.textToSpeechText);
        if(textToSpeech.getText() == null) {
            textToSpeech.setText("..");
        }
        Utils.sendCommand(command, textToSpeech.getText().toString(), fallbackSocket);
    }
}