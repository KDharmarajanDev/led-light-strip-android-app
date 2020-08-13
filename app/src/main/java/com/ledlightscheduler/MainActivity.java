package com.ledlightscheduler;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ledlightscheduler.arduinopackets.packets.GetInformationSerialPacket;
import com.ledlightscheduler.arduinopackets.packets.LEDStripsInformationPacket;
import com.ledlightscheduler.arduinopackets.packets.SerialPacket;
import com.ledlightscheduler.ledstriputilities.ledstrips.SingleColorLEDStrip;
import com.ledlightscheduler.uimanager.configurationsaver.ConfigurationSaverActivity;
import com.ledlightscheduler.uimanager.configurationsaver.FileSaverAndLoader;
import com.ledlightscheduler.uimanager.createpopups.CreateLEDStripPopup;
import com.ledlightscheduler.uimanager.recyclerviewadapters.LEDStripDisplayAdapter;
import com.ledlightscheduler.uimanager.recyclerviewpsacer.VerticalSpaceItemDecoration;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Bluetooth Items
    private BluetoothAdapter bluetoothAdapter;

    //UI elements
    private TextView connectionStatusText;
    private Button connectionSettingsButton;
    private Button addLEDStripButton;
    private Button uploadDataToArduinoButton;
    private Button downloadDataFromArduinoButton;
    private Button goToConfigurationSaverButton;
    private RecyclerView ledStripRecyclerView;
    private LEDStripDisplayAdapter ledStripRecyclerViewAdapter;
    private RecyclerView.LayoutManager ledStripLayoutManager;

    private static MainActivity instance;

    private static final int CONFIGURATION_SAVER_REQUEST_CODE = 1000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        setUpUIElements();
        setUpButtons();

        setUpBluetooth();

        //Sets up Recycler View
        setUpRecyclerView(FileSaverAndLoader.getLEDStrips(this, "Default"));

        instance = this;
    }

    public void setLEDStrips(ArrayList<SingleColorLEDStrip> ledStrips){
        ledStripRecyclerViewAdapter.setStrips(ledStrips);
    }

    public ArrayList<SingleColorLEDStrip> getLEDStrips(){
        return ledStripRecyclerViewAdapter.getStrips();
    }

    public void setUpUIElements(){
        connectionStatusText = findViewById(R.id.ArduinoConnectedText);
        connectionSettingsButton = findViewById(R.id.ConnectionSettingsButton);
        addLEDStripButton = findViewById(R.id.AddLEDStripButton);
        ledStripRecyclerView = findViewById(R.id.LEDStripRecyclerView);
        uploadDataToArduinoButton = findViewById(R.id.UploadToArduinoButton);
        downloadDataFromArduinoButton = findViewById(R.id.DownloadFromArduinoButton);
        goToConfigurationSaverButton = findViewById(R.id.GoToConfigurationSaverButton);
    }

    public void setUpButtons(){
        connectionSettingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this, ConnectionDebugActivity.class));
            }
        });

        addLEDStripButton.setOnClickListener(view -> {
            Intent getLEDStrip = new Intent(MainActivity.this, CreateLEDStripPopup.class);
            startActivityForResult(getLEDStrip, ledStripRecyclerViewAdapter.getItemCount());
        });

        uploadDataToArduinoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendPacket(new LEDStripsInformationPacket(getLEDStrips()));
                        setConnectionStatus(ConnectionDebugActivity.isConnected());
                    }
                }
        );

        downloadDataFromArduinoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendPacket(new GetInformationSerialPacket());
                        setConnectionStatus(ConnectionDebugActivity.isConnected());
                    }
                }
        );

        goToConfigurationSaverButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent startIntent = new Intent(MainActivity.this, ConfigurationSaverActivity.class);
                        startIntent.putExtra("LEDStrips", getLEDStrips());
                        startActivityForResult(startIntent, CONFIGURATION_SAVER_REQUEST_CODE);
                    }
                }
        );
    }

    public void setUpRecyclerView(ArrayList<SingleColorLEDStrip> ledStrips){
        ledStripRecyclerViewAdapter = new LEDStripDisplayAdapter(ledStrips, true);
        ledStripLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ledStripRecyclerView.setLayoutManager(ledStripLayoutManager);
        ledStripRecyclerView.setAdapter(ledStripRecyclerViewAdapter);
        ledStripRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(20));
        ledStripRecyclerViewAdapter.setOnItemClickListener(
                new LEDStripDisplayAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent modifyLEDStrip = new Intent(MainActivity.this, CreateLEDStripPopup.class);
                        modifyLEDStrip.putExtra("input-led-strip", ledStripRecyclerViewAdapter.getStrips().get(position));
                        startActivityForResult(modifyLEDStrip, position);
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        removeLEDStrip(position);
                    }
                }
        );
    }

    public void setUpBluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.isEnabled()){
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, -1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(data != null && data.getExtras() != null) {
                if(data.getExtras().getParcelable("led-strip") != null) {
                    SingleColorLEDStrip inputLEDStrip = data.getExtras().getParcelable("led-strip");
                    if (requestCode >= 0 && requestCode < ledStripRecyclerViewAdapter.getItemCount()) {
                        modifyLEDStrip(requestCode, inputLEDStrip);
                    } else if (requestCode >= ledStripRecyclerViewAdapter.getItemCount()){
                        addLEDStrip(inputLEDStrip);
                    }
                } else if(requestCode == CONFIGURATION_SAVER_REQUEST_CODE && data.getStringExtra("SelectedConfigurationName") != null) {
                    ledStripRecyclerViewAdapter.setStrips(FileSaverAndLoader.getLEDStrips(this, data.getStringExtra("SelectedConfigurationName")));
                }
            }
        }
        setConnectionStatus(ConnectionDebugActivity.isConnected());
    }

    public void modifyLEDStrip(int index, SingleColorLEDStrip ledStrip){
        ledStripRecyclerViewAdapter.getStrips().set(index, ledStrip);
        ledStripRecyclerViewAdapter.notifyItemChanged(index);
    }

    public void addLEDStrip(SingleColorLEDStrip ledStrip){
        ledStripRecyclerViewAdapter.getStrips().add(ledStrip);
        ledStripRecyclerViewAdapter.notifyItemChanged(ledStripRecyclerViewAdapter.getItemCount()-1);
    }

    public void removeLEDStrip(int removedIndex){
        ledStripRecyclerViewAdapter.getStrips().remove(removedIndex);
        ledStripRecyclerViewAdapter.notifyItemRemoved(removedIndex);
    }

    public void sendPacket(SerialPacket packet){
        if(ConnectionDebugActivity.isConnected()){
            ConnectionDebugActivity.getConnectedThread().sendPacket(packet);
        } else {
            Toast.makeText(MainActivity.this, "Device not connected", Toast.LENGTH_SHORT).show();
        }
    }

    public static MainActivity getInstance(){
        return instance;
    }

    public void setConnectionStatus(boolean connected){
        if(connected){
            Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT).show();
            connectionStatusText.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.presence_online, 0);
        } else {
            Toast.makeText(this, "Couldn't connect!", Toast.LENGTH_SHORT).show();
            connectionStatusText.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.presence_away, 0);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        FileSaverAndLoader.saveLEDStrips(getLEDStrips(), this, "Default");
    }
}
