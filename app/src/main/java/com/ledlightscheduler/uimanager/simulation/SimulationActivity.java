package com.ledlightscheduler.uimanager.simulation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.ledlightscheduler.R;

public class SimulationActivity extends Activity {

    //UI Elements
    private Button goBackButton;
    private RecyclerView simulationRecyclerView;

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
    }

    public void setupButton() {
        goBackButton.setOnClickListener(view -> finish());
    }

}
