package com.ledlightscheduler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConfigurationSaverActivity extends Activity {

    private Button saveConfigButton;
    private Button goBackToMainActivityButton;

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
        saveConfigButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );

        goBackToMainActivityButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
    }

}
