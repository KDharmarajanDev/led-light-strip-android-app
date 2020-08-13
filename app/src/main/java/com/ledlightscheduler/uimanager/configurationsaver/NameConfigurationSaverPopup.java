package com.ledlightscheduler.uimanager.configurationsaver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ledlightscheduler.R;

import java.io.File;
import java.util.Arrays;

public class NameConfigurationSaverPopup extends Activity {

    //UI Elements
    private Button cancelButton;
    private Button saveButton;
    private TextInputEditText nameInputEditText;

    private File[] files;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_configuration_name);

        setupWindow();

        files = FileSaverAndLoader.getLEDStripFiles(this);

        setupUIElements();
        setupButtons();
        setupEditText();
    }

    private void setupWindow() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(layoutParams);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (0.7 * width), (int) (0.3 *height));
    }

    private void setupUIElements() {
        cancelButton = findViewById(R.id.CancelConfigurationNameButton);
        saveButton = findViewById(R.id.SaveConfigurationNameButton);
        nameInputEditText = findViewById(R.id.SaveNameEditText);
    }

    private void setupButtons() {
        cancelButton.setOnClickListener(view -> finish());
        saveButton.setOnClickListener(view -> {
            if(nameInputEditText.getError() == null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("NameOfConfiguration", nameInputEditText.getText().toString().trim());
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Configuration name has an error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupEditText() {
        nameInputEditText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(s.toString().trim().length() != 0) {
                            nameInputEditText.setError("Configuration name is blank!");
                        } else if(isNameDuplicated(s.toString().trim())) {
                            nameInputEditText.setError("Configuration name is taken!");
                        } else {
                            nameInputEditText.setError(null);
                        }
                    }
                }
        );
    }

    private boolean isNameDuplicated(String name) {
        return Arrays.stream(files)
                .anyMatch(file -> file.getName().equals(name));
    }
}
