package com.ledlightscheduler.uimanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.google.android.material.textfield.TextInputEditText;
import com.ledlightscheduler.R;
import com.ledlightscheduler.ledstriputilities.ledstates.Color;

public class CreateColorPopup extends Activity {

    //UI elements
    private ImageView colorPickerDisplay;
    private Button saveButton;
    private Button cancelButton;

    private SeekBar redSeekBar;
    private TextInputEditText redValueInputText;
    private SeekBar greenSeekBar;
    private TextInputEditText greenValueInputText;
    private SeekBar blueSeekBar;
    private TextInputEditText blueValueInputText;

    private com.ledlightscheduler.ledstriputilities.ledstates.Color color;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        //Sets up how the window looks when this is created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(layoutParams);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width*0.95), (int) (0.45*height));

        //Sets up UI elements
        colorPickerDisplay = findViewById(R.id.ColorPickerDisplay);
        saveButton = findViewById(R.id.SaveColorButton);
        cancelButton = findViewById(R.id.CancelColorButton);

        redSeekBar = findViewById(R.id.RedSeekBar);
        redValueInputText = findViewById(R.id.RedValueEditText);
        greenSeekBar = findViewById(R.id.GreenSeekBar);
        greenValueInputText = findViewById(R.id.GreenValueEditText);
        blueSeekBar = findViewById(R.id.BlueSeekBar);
        blueValueInputText = findViewById(R.id.BlueValueEditText);

        Intent inputColorIntent = getIntent();
        if(inputColorIntent != null && inputColorIntent.getExtras() !=null && inputColorIntent.getExtras().getParcelable("input-color") != null){
            color = inputColorIntent.getExtras().getParcelable("input-color");
            updateFromGivenColor(color);
        } else {
            color = new Color(255,255,255);
        }

        //Sets up Listeners
        redSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser){
                            updateText(redValueInputText, String.valueOf(seekBar.getProgress()));
                            color.setRed(seekBar.getProgress());
                            changeColorPickerColor(color);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

        redValueInputText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(s != null && s.length() > 0) {
                            long value = Long.valueOf(s.toString());
                            if (value > 255) {
                                value = 255;
                                updateText(redValueInputText, String.valueOf(value));
                            } else if (value < 0) {
                                value = 0;
                                updateText(redValueInputText, String.valueOf(value));
                            }
                            redSeekBar.setProgress((int) value);
                            color.setRed((int) value);
                            changeColorPickerColor(color);
                        }
                    }
                }
        );

        greenSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser){
                            updateText(greenValueInputText, String.valueOf(seekBar.getProgress()));
                            color.setGreen(seekBar.getProgress());
                            changeColorPickerColor(color);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

        greenValueInputText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(s != null && s.length() > 0) {
                            long value = Long.valueOf(s.toString());
                            if (value > 255) {
                                value = 255;
                                updateText(greenValueInputText, String.valueOf(value));
                            } else if (value < 0) {
                                value = 0;
                                updateText(greenValueInputText, String.valueOf(value));
                            }
                            greenSeekBar.setProgress((int) value);
                            color.setGreen((int) value);
                            changeColorPickerColor(color);
                        }
                    }
                }
        );

        blueSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser){
                            updateText(blueValueInputText, String.valueOf(seekBar.getProgress()));
                            color.setBlue(seekBar.getProgress());
                            changeColorPickerColor(color);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

        blueValueInputText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(s != null && s.length() > 0) {
                            long value = Long.valueOf(s.toString());
                            if (value > 255) {
                                value = 255;
                                updateText(blueValueInputText, String.valueOf(value));
                            } else if (value < 0) {
                                value = 0;
                                updateText(blueValueInputText, String.valueOf(value));
                            }
                            blueSeekBar.setProgress((int) value);
                            color.setBlue((int) value);
                            changeColorPickerColor(color);
                        }
                    }
                }
        );

        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent savedColor = new Intent();
                        savedColor.putExtra("color",color);
                        setResult(Activity.RESULT_OK, savedColor);
                        finish();
                    }
                }
        );

        cancelButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    public void updateFromGivenColor(Color color){
        changeAllSeekBarProgresses(color);
        changeAllTexts(color);
        changeColorPickerColor(color);
    }

    public void changeAllSeekBarProgresses(Color color){
        redSeekBar.setProgress(color.getRed());
        blueSeekBar.setProgress(color.getBlue());
        greenSeekBar.setProgress(color.getGreen());
    }

    public void changeAllTexts(Color color){
        updateText(redValueInputText, String.valueOf(color.getRed()));
        updateText(blueValueInputText, String.valueOf(color.getBlue()));
        updateText(greenValueInputText, String.valueOf(color.getGreen()));
    }

    public void changeColorPickerColor(com.ledlightscheduler.ledstriputilities.ledstates.Color color){
        GradientDrawable drawable = (GradientDrawable) colorPickerDisplay.getBackground().mutate();
        drawable.setColor(color.toAndroidColor());
    }

    void updateText(EditText editText, String text) {
        boolean focused = editText.hasFocus();
        if (focused) {
            editText.clearFocus();
        }
        editText.setText(text);
        if (focused) {
            editText.requestFocus();
        }
    }
}
