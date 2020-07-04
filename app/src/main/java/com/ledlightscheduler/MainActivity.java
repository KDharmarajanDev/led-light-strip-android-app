package com.ledlightscheduler;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ledlightscheduler.uimanager.CreateLEDStripPopup;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;

    public final static int REQUEST_ENABLE_BT = 1;

    //UI elements
    private TextView connectionStatusText;
    private Button connectionSettingsButton;
    private Button addLEDStripButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpUIElements();

        Log.i("[Light Strip Scheduler]", "Setting up Button Clicking Events.");
        connectionSettingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this, ConnectionDebugActivity.class));
            }
        });

        addLEDStripButton.setOnClickListener(view -> {
            Intent startColor = new Intent(MainActivity.this, CreateLEDStripPopup.class);
            startActivity(startColor);
        });

        Log.i("[Light Strip Scheduler]", "Attempting to set up bluetooth connection.");
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.isEnabled()){
            connectionStatusText.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.presence_away,0);
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
        } else {
            connectionStatusText.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.presence_online,0);
        }
    }

    public void setUpUIElements(){
        connectionStatusText = findViewById(R.id.ArduinoConnectedText);
        connectionSettingsButton = findViewById(R.id.ConnectionSettingsButton);
        addLEDStripButton = findViewById(R.id.AddLEDStripButton);
    }

}
