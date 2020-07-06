package com.ledlightscheduler;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ledlightscheduler.ledstriputilities.Scheduler;
import com.ledlightscheduler.ledstriputilities.ledstates.TransitionLEDState;
import com.ledlightscheduler.ledstriputilities.ledstrips.SingleColorLEDStrip;
import com.ledlightscheduler.uimanager.CreateLEDStripPopup;
import com.ledlightscheduler.uimanager.recyclerviewadapters.LEDStripDisplayAdapter;
import com.ledlightscheduler.uimanager.recyclerviewadapters.SequentialGeneratorDisplayAdapter;
import com.ledlightscheduler.uimanager.recyclerviewpsacer.VerticalSpaceItemDecoration;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;

    public final static int REQUEST_ENABLE_BT = 1;

    //UI elements
    private TextView connectionStatusText;
    private Button connectionSettingsButton;
    private Button addLEDStripButton;
    private RecyclerView ledStripRecyclerView;
    private LEDStripDisplayAdapter ledStripRecyclerViewAdapter;
    private RecyclerView.LayoutManager ledStripLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUIElements();

        Log.i("[Light Strip Scheduler]", "Setting up Button Clicking Events.");
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

        Log.i("[Light Strip Scheduler]", "Attempting to set up bluetooth connection.");
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.isEnabled()){
            connectionStatusText.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.presence_away,0);
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
        } else {
            connectionStatusText.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.presence_online,0);
        }

        //Sets up Recycler View
        setUpRecyclerView(new ArrayList<>());
    }

    public void setUpUIElements(){
        connectionStatusText = findViewById(R.id.ArduinoConnectedText);
        connectionSettingsButton = findViewById(R.id.ConnectionSettingsButton);
        addLEDStripButton = findViewById(R.id.AddLEDStripButton);
        ledStripRecyclerView = findViewById(R.id.LEDStripRecyclerView);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(data != null && data.getExtras() != null && data.getExtras().getParcelable("led-strip") != null) {
                SingleColorLEDStrip inputLEDStrip = data.getExtras().getParcelable("led-strip");
                if(requestCode >= 0 && requestCode < ledStripRecyclerViewAdapter.getItemCount()){
                    modifyLEDStrip(requestCode, inputLEDStrip);
                } else {
                    addLEDStrip(inputLEDStrip);
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

}
