package com.ledlightscheduler.uimanager.configurationsaver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledlightscheduler.R;
import com.ledlightscheduler.ledstriputilities.ledstrips.SingleColorLEDStrip;
import com.ledlightscheduler.uimanager.recyclerviewadapters.ConfigurationsDisplayAdapter;
import com.ledlightscheduler.uimanager.recyclerviewpsacer.VerticalSpaceItemDecoration;

import java.util.ArrayList;

public class ConfigurationSaverActivity extends Activity {

    //UI Elements
    private Button saveConfigButton;
    private Button goBackToMainActivityButton;
    private ConfigurationsDisplayAdapter adapter;
    private RecyclerView configurationRecyclerView;
    private LinearLayoutManager configurationLinearLayoutManager;

    private ArrayList<SingleColorLEDStrip> ledStrips;

    private static final int REQUEST_SAVE_NAME_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_selection);

        Intent currIntent = getIntent();
        if (currIntent != null && currIntent.getExtras() != null && currIntent.getExtras().getParcelableArrayList("LEDStrips") != null) {
            ledStrips = currIntent.getExtras().getParcelableArrayList("LEDStrips");
        } else {
            ledStrips = new ArrayList<>();
        }

        setupUIElements();
        setupButtons();
    }

    public void setupUIElements(){
        saveConfigButton = findViewById(R.id.SaveThisConfigurationButton);
        goBackToMainActivityButton = findViewById(R.id.GoBackToMainActivityButton);

        configurationRecyclerView = findViewById(R.id.ConfigurationRecyclerView);
        adapter = new ConfigurationsDisplayAdapter(FileSaverAndLoader.getLEDStripFileNames(this));
        configurationLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        configurationRecyclerView.setLayoutManager(configurationLinearLayoutManager);
        adapter.setOnItemClickListener(
                new ConfigurationsDisplayAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("SelectedConfigurationName", adapter.getConfigNames().get(position));
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        removeName(position);
                    }
                }
        );

        configurationRecyclerView.setAdapter(adapter);
        configurationRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(7));
    }

    public void setupButtons(){
        saveConfigButton.setOnClickListener(view -> {
            Intent sendIntent = new Intent(ConfigurationSaverActivity.this, NameConfigurationSaverPopup.class);
            sendIntent.putExtra("ConfigurationNames", adapter.getConfigNames());
            startActivityForResult(sendIntent, REQUEST_SAVE_NAME_CODE);
        });
        goBackToMainActivityButton.setOnClickListener(view -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == REQUEST_SAVE_NAME_CODE) {
                adapter.addName(data.getStringExtra("NameOfConfiguration"));
                FileSaverAndLoader.saveLEDStrips(ledStrips, this, data.getStringExtra("NameOfConfiguration"));
            }
        }
    }

    public void removeName(int position) {
        FileSaverAndLoader.removeLEDStrips(this, adapter.getConfigNames().get(position));
        adapter.removeName(position);
    }
}
