package com.ledlightscheduler.uimanager.createpopups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.ledlightscheduler.R;
import com.ledlightscheduler.ledstriputilities.generators.SequentialGenerator;
import com.ledlightscheduler.ledstriputilities.ledstrips.SingleColorLEDStrip;
import com.ledlightscheduler.uimanager.recyclerviewadapters.SequentialGeneratorDisplayAdapter;
import com.ledlightscheduler.uimanager.recyclerviewpsacer.VerticalSpaceItemDecoration;

import java.util.ArrayList;

public class CreateLEDStripPopup extends Activity {

    //UI Elements
    private TextInputEditText redPinEditText;
    private TextInputEditText greenPinEditText;
    private TextInputEditText bluePinEditText;
    private Button addSequentialGeneratorButton;
    private Button saveLEDStripButton;
    private Button cancelLEDStripButton;
    private RecyclerView sequentialGeneratorRecyclerView;
    private LinearLayoutManager sequentialGeneratorRecyclerViewLinearLayoutManager;
    private SequentialGeneratorDisplayAdapter sequentialGeneratorDisplayAdapter;

    private SingleColorLEDStrip ledStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setUpDisplay();
        setUpLEDStrip();

        setUpUIElements();
        setUpButtons();
        setUpTextDisplay();
    }

    public void setUpDisplay(){
        setContentView(R.layout.activity_led_strip_create);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(layoutParams);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (0.95 * width), (int) (0.8 *height));
    }

    public void setUpLEDStrip(){
        Intent inputLEDStrip = getIntent();

        if(inputLEDStrip != null && inputLEDStrip.getExtras() != null && inputLEDStrip.getExtras().getParcelable("input-led-strip") != null){
            ledStrip = inputLEDStrip.getExtras().getParcelable("input-led-strip");
        } else {
            ledStrip = new SingleColorLEDStrip(0, 0, 0, new ArrayList<>());
        }
    }

    public void setUpUIElements(){
        redPinEditText = findViewById(R.id.RedPinEditText);
        greenPinEditText = findViewById(R.id.GreenPinEditText);
        bluePinEditText = findViewById(R.id.BluePinEditText);
        addSequentialGeneratorButton = findViewById(R.id.AddSequentialGeneratorButton);
        saveLEDStripButton = findViewById(R.id.SaveLEDStripButton);
        cancelLEDStripButton = findViewById(R.id.CancelLEDStripButton);

        sequentialGeneratorRecyclerView = findViewById(R.id.CreateLEDStripSequentialGeneratorRecyclerView);
        sequentialGeneratorDisplayAdapter = new SequentialGeneratorDisplayAdapter(ledStrip, true, true);
        sequentialGeneratorRecyclerViewLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        sequentialGeneratorRecyclerView.setLayoutManager(sequentialGeneratorRecyclerViewLinearLayoutManager);

        sequentialGeneratorDisplayAdapter.setOnItemClickListener(
                new SequentialGeneratorDisplayAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(CreateLEDStripPopup.this, CreateSequentialGeneratorPopup.class);
                        intent.putExtra("input-generator", ledStrip.getGenerators().get(position));
                        startActivityForResult(intent, position);
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        removeSequentialGenerator(position);
                    }

                    @Override
                    public void onApplyClick(int position) {
                        updateActivatedStatus(sequentialGeneratorDisplayAdapter.getActiveGenerator(), false);
                        sequentialGeneratorDisplayAdapter.setActiveGenerator(position);
                        updateActivatedStatus(position, true);
                    }
                }
        );

        sequentialGeneratorRecyclerView.setAdapter(sequentialGeneratorDisplayAdapter);
        sequentialGeneratorRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(7));
    }

    public void setUpButtons(){
        addSequentialGeneratorButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CreateLEDStripPopup.this, CreateSequentialGeneratorPopup.class);
                        startActivityForResult(intent, ledStrip.getGenerators().size());
                    }
                }
        );

        saveLEDStripButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ledStrip.setCurrentGeneratorIndex(sequentialGeneratorDisplayAdapter.getActiveGenerator());
                        Intent result = new Intent();
                        result.putExtra("led-strip", ledStrip);
                        setResult(Activity.RESULT_OK, result);
                        finish();
                    }
                }
        );

        cancelLEDStripButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    public void setUpTextDisplay(){
        redPinEditText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!s.toString().equals("") && !s.toString().equals("-")){
                            ledStrip.setRedPin(Integer.parseInt(s.toString()));
                        } else {
                            ledStrip.setRedPin(0);
                        }
                    }
                }
        );

        greenPinEditText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!s.toString().equals("") && !s.toString().equals("-")){
                            ledStrip.setGreenPin(Integer.parseInt(s.toString()));
                        } else {
                            ledStrip.setGreenPin(0);
                        }
                    }
                }
        );

        bluePinEditText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!s.toString().equals("") && !s.toString().equals("-")){
                            ledStrip.setBluePin(Integer.parseInt(s.toString()));
                        } else {
                            ledStrip.setBluePin(0);
                        }
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if(data != null && data.getExtras() != null && data.getExtras().getParcelable("sequential-generator") != null) {
                if(requestCode >= 0 && requestCode < ledStrip.getGenerators().size()){
                    modifySequentialGenerator(requestCode, data.getExtras().getParcelable("sequential-generator"));
                } else {
                    addSequentialGenerator(data.getExtras().getParcelable("sequential-generator"));
                }
            }
        }
    }

    public void modifySequentialGenerator(int index, SequentialGenerator generator){
        ledStrip.getGenerators().set(index, generator);
        sequentialGeneratorDisplayAdapter.notifyItemChanged(index);
    }

    public void addSequentialGenerator(SequentialGenerator generator){
        ledStrip.getGenerators().add(generator);
        sequentialGeneratorDisplayAdapter.notifyItemInserted(ledStrip.getGenerators().size()-1);
    }

    public void removeSequentialGenerator(int removedIndex){
        ledStrip.getGenerators().remove(removedIndex);
        sequentialGeneratorDisplayAdapter.notifyItemRemoved(removedIndex);
        if(sequentialGeneratorDisplayAdapter.getActiveGenerator() == removedIndex && removedIndex != 0){
            sequentialGeneratorDisplayAdapter.setActiveGenerator(0);
            updateActivatedStatus(0, true);
        }
    }

    public void updateActivatedStatus(int index, boolean isActivated){
        View itemView = sequentialGeneratorRecyclerViewLinearLayoutManager.getChildAt(index);
        if(isActivated){
            itemView.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.activated_sequential_generator));
        } else {
            itemView.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.deactivated_sequential_generator));
        }
    }


}
