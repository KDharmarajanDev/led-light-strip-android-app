package com.ledlightscheduler.uimanager.simulation;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledlightscheduler.R;
import com.ledlightscheduler.ledstriputilities.Scheduler;
import com.ledlightscheduler.ledstriputilities.ledstates.Color;
import com.ledlightscheduler.ledstriputilities.ledstates.TransitionLEDState;
import com.ledlightscheduler.ledstriputilities.ledstrips.SingleColorLEDStrip;
import com.ledlightscheduler.uimanager.recyclerviewpsacer.VerticalSpaceItemDecoration;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class SimulationActivity extends Activity {

    //UI Elements
    private Button goBackButton;
    private RecyclerView simulationRecyclerView;
    private SimulationRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);

        setupUIElements();
        setupButton();
    }

    public void setupUIElements() {
        goBackButton = findViewById(R.id.BackFromSimulationButton);
        simulationRecyclerView = findViewById(R.id.SimulationRecyclerView);
        adapter = new SimulationRecyclerViewAdapter(getIntent().getParcelableArrayListExtra("LEDStrips"), (color, background) ->
            runOnUiThread(()-> {
                background.setBackgroundColor(color);
            }));
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        simulationRecyclerView.setAdapter(adapter);
        simulationRecyclerView.setLayoutManager(layoutManager);
        simulationRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(20));
    }

    public void setupButton() {
        goBackButton.setOnClickListener(view -> finish());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        adapter.stopSchedulers();
    }

}
