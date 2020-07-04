package com.ledlightscheduler.uimanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledlightscheduler.R;
import com.ledlightscheduler.ledstriputilities.generators.RandomGenerator;
import com.ledlightscheduler.ledstriputilities.generators.SequentialGenerator;
import com.ledlightscheduler.ledstriputilities.ledstates.LEDState;
import com.ledlightscheduler.uimanager.recyclerviewadapters.LEDStateDisplayAdapter;
import com.ledlightscheduler.uimanager.recyclerviewpsacer.HorizontalSpaceItemDecoration;

import java.util.ArrayList;

public class CreateSequentialGeneratorPopup extends Activity {

    //UI Elements
    private CheckBox randomGeneratorCheckBox;
    private RecyclerView ledStateDisplayView;
    private LEDStateDisplayAdapter ledStateDisplayViewAdapter;
    private RecyclerView.LayoutManager ledStateDisplayLayoutManager;
    private Button addLEDStateButton;
    private Button cancelSequentialGeneratorButton;
    private Button saveSequentialGeneratorButton;

    private SequentialGenerator generator;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequential_generator_create);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(layoutParams);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (0.9*width), (int) (0.5*height));

        //Sets up UI Elements
        randomGeneratorCheckBox = findViewById(R.id.RandomGeneratorCheckBox);
        ledStateDisplayView = findViewById(R.id.LEDStateDisplayView);
        addLEDStateButton = findViewById(R.id.AddLEDStateButton);
        cancelSequentialGeneratorButton = findViewById(R.id.CancelSequentialGeneratorButton);
        saveSequentialGeneratorButton = findViewById(R.id.SaveSequentialGeneratorButton);

        //Sets up SequentialGenerator
        Intent inputGenerator = getIntent();
        if(inputGenerator != null && inputGenerator.getExtras() != null && inputGenerator.getExtras().getParcelable("input-generator") != null){
            generator =inputGenerator.getExtras().getParcelable("input-generator");
        } else {
            generator = new SequentialGenerator(new ArrayList<>());
        }

        //Sets up RecyclerView
        ledStateDisplayViewAdapter = new LEDStateDisplayAdapter(generator, true);
        ledStateDisplayLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ledStateDisplayView.setLayoutManager(ledStateDisplayLayoutManager);
        ledStateDisplayView.setAdapter(ledStateDisplayViewAdapter);
        ledStateDisplayView.addItemDecoration(new HorizontalSpaceItemDecoration(7));
        //Sets up UI Element Listeners
        randomGeneratorCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            generator = generator.toRandomGenerator();
                        } else {
                            generator = ((RandomGenerator) generator).toSequentialGenerator();
                        }
                        ledStateDisplayViewAdapter.setGenerator(generator);
                    }
                }
        );

        addLEDStateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CreateSequentialGeneratorPopup.this, CreateLEDStatePopup.class);
                        startActivityForResult(intent, generator.getStates().size());
                    }
                }
        );

        saveSequentialGeneratorButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent result = new Intent();
                        result.putExtra("sequential-generator", generator);
                        setResult(Activity.RESULT_OK, result);
                        finish();
                    }
                }
        );

        cancelSequentialGeneratorButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        ledStateDisplayViewAdapter.setOnItemClickListener(
                new LEDStateDisplayAdapter.OnItemClickListener(){
                    @Override
                    public void onItemClick(int position){
                        Intent intent = new Intent(CreateSequentialGeneratorPopup.this, CreateLEDStatePopup.class);
                        intent.putExtra("input-led-state", generator.getState(position));
                        startActivityForResult(intent, position);
                    }

                    @Override
                    public void onDeleteClick(int position){
                        removeLEDState(position);
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(data != null && data.getExtras() != null && data.getExtras().getParcelable("led-state") != null) {
                if(requestCode >= 0 && requestCode < generator.getStates().size()){
                    modifyLEDState(requestCode, data.getExtras().getParcelable("led-state"));
                } else {
                    addLEDState(data.getExtras().getParcelable("led-state"));
                }
            }
        }
    }

    public void modifyLEDState(int index, LEDState ledState){
        generator.getStates().set(index, ledState);
        ledStateDisplayViewAdapter.notifyItemChanged(index);
    }

    public void addLEDState(LEDState ledState){
        generator.addState(ledState);
        ledStateDisplayViewAdapter.notifyItemInserted(generator.getStates().size()-1);
    }

    public void removeLEDState(int position){
        generator.getStates().remove(position);
        ledStateDisplayViewAdapter.notifyItemRemoved(position);
    }

}
