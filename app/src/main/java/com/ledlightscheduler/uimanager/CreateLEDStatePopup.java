package com.ledlightscheduler.uimanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.ledlightscheduler.R;
import com.ledlightscheduler.ledstriputilities.ledstates.LEDState;
import com.ledlightscheduler.ledstriputilities.ledstates.TransitionLEDState;

public class CreateLEDStatePopup extends Activity {

    //UI elements
    private TextInputEditText durationText;
    private CheckBox transitionLEDStateCheckbox;
    private ImageView startingColorDisplay;
    private TextView endingColorText;
    private ImageView endingColorDisplay;
    private Button saveLEDStateButton;
    private Button cancelLEDStateButton;

    //Color Request Codes
    private static final int START_COLOR_REQUEST_CODE = 1;
    private static final int END_COLOR_REQUEST_CODE = 2;

    private LEDState ledState;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        //Sets up how the window looks when this is created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_state_create);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(layoutParams);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (0.82*width), (int) (0.45*height));

        //Sets up LEDState
        Intent inputLEDState = getIntent();
        if(inputLEDState != null && inputLEDState.getExtras() != null && inputLEDState.getExtras().getParcelable("input-led-state") != null){
            ledState = inputLEDState.getExtras().getParcelable("input-led-state");
        } else {
            ledState = new LEDState();
        }

        //Sets up UI elements
        durationText = findViewById(R.id.DurationValueEditText);
        transitionLEDStateCheckbox = findViewById(R.id.TransitionLEDStateCheckBox);
        startingColorDisplay = findViewById(R.id.StartingColorDisplay);
        endingColorDisplay = findViewById(R.id.EndingColorDisplay);
        endingColorText = findViewById(R.id.EndingColorText);
        saveLEDStateButton = findViewById(R.id.SaveLEDStateButton);
        cancelLEDStateButton = findViewById(R.id.CancelLEDStateButton);

        displayLEDState(ledState);
        //Sets up UI element listeners
        durationText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(s.toString().length() > 0) {
                            try {
                                ledState.setDuration((long) (Double.parseDouble(s.toString()) * 1000));
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        transitionLEDStateCheckbox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            ledState = ledState.toTransitionLEDState();
                            setColorDisplay(endingColorDisplay, ledState.getColor());
                            setEndColorElementVisibilities(View.VISIBLE);
                        } else {
                            ledState = ((TransitionLEDState) ledState).toLEDState();
                            setEndColorElementVisibilities(View.INVISIBLE);
                        }
                    }
                }
        );

        startingColorDisplay.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent getColorIntent = new Intent(CreateLEDStatePopup.this, CreateColorPopup.class);
                        getColorIntent.putExtra("input-color", ledState.getColor());
                        startActivityForResult(getColorIntent, START_COLOR_REQUEST_CODE);
                    }
                }
        );

        endingColorDisplay.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ledState instanceof TransitionLEDState) {
                            Intent getColorIntent = new Intent(CreateLEDStatePopup.this, CreateColorPopup.class);
                            getColorIntent.putExtra("input-color", ((TransitionLEDState) ledState).getEndColor());
                            startActivityForResult(getColorIntent, END_COLOR_REQUEST_CODE);
                        }
                    }
                }
        );

        saveLEDStateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent result = new Intent();
                        result.putExtra("led-state", ledState);
                        setResult(Activity.RESULT_OK, result);
                        finish();
                    }
                }
        );

        cancelLEDStateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(data != null && data.getExtras() != null && data.getExtras().getParcelable("color") != null) {
                com.ledlightscheduler.ledstriputilities.ledstates.Color inputColor = data.getExtras().getParcelable("color");
                if(requestCode == START_COLOR_REQUEST_CODE) {
                    ledState.setColor(inputColor);
                    setColorDisplay(startingColorDisplay, inputColor);
                } else if(requestCode == END_COLOR_REQUEST_CODE && ledState instanceof TransitionLEDState) {
                    ((TransitionLEDState) ledState).setEndColor(inputColor);
                    setColorDisplay(endingColorDisplay, inputColor);
                }
            }
        }
    }

    public void displayLEDState(LEDState ledState){
        setColorDisplay(startingColorDisplay, ledState.getColor());
        setText(durationText, String.valueOf(ledState.getDuration()/1000.0));
        if(ledState instanceof TransitionLEDState){
            transitionLEDStateCheckbox.setChecked(true);
            setEndColorElementVisibilities(View.VISIBLE);
            setColorDisplay(endingColorDisplay, ((TransitionLEDState) ledState).getEndColor());
        } else {
            setEndColorElementVisibilities(View.INVISIBLE);
        }
    }

    public void setColorDisplay(ImageView colorDisplay, com.ledlightscheduler.ledstriputilities.ledstates.Color color){
        GradientDrawable startingColor = (GradientDrawable) colorDisplay.getBackground().mutate();
        startingColor.setColor(Color.rgb(color.getRed(),color.getGreen(),color.getBlue()));
    }

    public void setText(TextInputEditText text, String changeTo){
        durationText.setText(changeTo);
    }

    public void setEndColorElementVisibilities(int visibility){
        endingColorDisplay.setVisibility(visibility);
        endingColorText.setVisibility(visibility);
    }

}
