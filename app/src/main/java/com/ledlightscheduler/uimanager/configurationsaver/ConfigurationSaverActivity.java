package com.ledlightscheduler.uimanager.configurationsaver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ledlightscheduler.R;

public class ConfigurationSaverActivity extends Activity {

    //UI Elements
    private Button saveConfigButton;
    private Button goBackToMainActivityButton;

    private static final int REQUEST_SAVE_NAME_CODE = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_selection);

        setupUIElements();
        setupButtons();

    }

    public void setupUIElements(){
        saveConfigButton = findViewById(R.id.SaveThisConfigurationButton);
        goBackToMainActivityButton = findViewById(R.id.GoBackToMainActivityButton);
    }

    public void setupButtons(){
        saveConfigButton.setOnClickListener(view -> {
            Intent sendIntent = new Intent(ConfigurationSaverActivity.this, NameConfigurationSaverPopup.class);
            startActivityForResult(sendIntent, REQUEST_SAVE_NAME_CODE);
        });
        goBackToMainActivityButton.setOnClickListener(view -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == REQUEST_SAVE_NAME_CODE) {
                data.getStringExtra("NameOfConfiguration");
            }
        }
    }
}
