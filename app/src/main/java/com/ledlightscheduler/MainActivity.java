package com.ledlightscheduler;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ledlightscheduler.arduinopackets.SerialMessageHandler;
import com.ledlightscheduler.arduinopackets.packets.GetInformationSerialPacket;
import com.ledlightscheduler.ledstriputilities.Scheduler;
import com.ledlightscheduler.ledstriputilities.ledstrips.SingleColorLEDStrip;
import com.ledlightscheduler.uimanager.CreateLEDStripPopup;
import com.ledlightscheduler.uimanager.recyclerviewadapters.LEDStripDisplayAdapter;
import com.ledlightscheduler.uimanager.recyclerviewpsacer.VerticalSpaceItemDecoration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    //Bluetooth Items
    private BluetoothAdapter bluetoothAdapter;
    private ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket mConenctedSocket;
    private static final int REQUEST_BLUETOOTH_CODE = -1;
    private static final UUID BTMODULEUUID = UUID.randomUUID(); // Device UUID

    //UI elements
    private TextView connectionStatusText;
    private Button connectionSettingsButton;
    private Button addLEDStripButton;
    private Button uploadDataToArduinoButton;
    private Button downloadDataFromArduinoButton;
    private RecyclerView ledStripRecyclerView;
    private LEDStripDisplayAdapter ledStripRecyclerViewAdapter;
    private RecyclerView.LayoutManager ledStripLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        setUpUIElements();
        setUpButtons();

        setUpBluetooth();

        //Sets up Recycler View
        setUpRecyclerView(new ArrayList<>());
    }

    public void setUpUIElements(){
        connectionStatusText = findViewById(R.id.ArduinoConnectedText);
        connectionSettingsButton = findViewById(R.id.ConnectionSettingsButton);
        addLEDStripButton = findViewById(R.id.AddLEDStripButton);
        ledStripRecyclerView = findViewById(R.id.LEDStripRecyclerView);
        uploadDataToArduinoButton = findViewById(R.id.UploadToArduinoButton);
        downloadDataFromArduinoButton = findViewById(R.id.DownloadFromArduinoButton);
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
                        if(mConnectedThread != null){

                        }
                    }
                }
        );

        downloadDataFromArduinoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mConnectedThread != null){
                            mConnectedThread.write(new GetInformationSerialPacket().serialize());
                        }
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
                        modifyLEDStrip.putExtra("input-led-strip", ledStripRecyclerViewAdapter.getSchedulers().get(position).getLEDStrip());
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
            startActivityForResult(enableBluetooth, REQUEST_BLUETOOTH_CODE);
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
                    } else {
                        addLEDStrip(inputLEDStrip);
                    }
                } else if(data.getExtras().getParcelable("device") != null){
                    try {
                        mConenctedSocket = createBluetoothSocket(data.getExtras().getParcelable("device"));
                        mConenctedSocket.connect();
                        mConnectedThread = new ConnectedThread(mConenctedSocket);
                        Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT);
                        connectionStatusText.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.presence_online, 0);
                    } catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this, "Couldn't connect!", Toast.LENGTH_SHORT);
                        connectionStatusText.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.presence_away, 0);
                    }
                }
            }
        }
    }

    public void modifyLEDStrip(int index, SingleColorLEDStrip ledStrip){
        Scheduler scheduler = new Scheduler(ledStrip);
        ledStripRecyclerViewAdapter.getSchedulers().set(index, scheduler);
        ledStripRecyclerViewAdapter.notifyItemChanged(index);
    }

    public void addLEDStrip(SingleColorLEDStrip ledStrip){
        Scheduler scheduler = new Scheduler(ledStrip);
        ledStripRecyclerViewAdapter.getSchedulers().add(scheduler);
        ledStripRecyclerViewAdapter.notifyItemChanged(ledStripRecyclerViewAdapter.getItemCount()-1);
    }

    public void removeLEDStrip(int removedIndex){
        ledStripRecyclerViewAdapter.getSchedulers().remove(removedIndex);
        ledStripRecyclerViewAdapter.notifyItemRemoved(removedIndex);
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private boolean isCanceled;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;

            isCanceled = false;
        }

        public void run() {
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (!isCanceled) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if(bytes != 0) {
                        SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                        String output = new BufferedReader(
                                new InputStreamReader(mmInStream, StandardCharsets.UTF_8))
                                .lines()
                                .collect(Collectors.joining("\n"));
                        SerialMessageHandler.getHandlerInstance().handleMessage(output);
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            isCanceled = true;
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connection with BT device using UUID
    }
}
