package com.ledlightscheduler.uimanager.recyclerviewadapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ledlightscheduler.R;
import com.ledlightscheduler.ledstriputilities.generators.SequentialGenerator;
import com.ledlightscheduler.ledstriputilities.ledstates.LEDState;
import com.ledlightscheduler.ledstriputilities.ledstates.TransitionLEDState;

public class LEDStateDisplayAdapter extends RecyclerView.Adapter<LEDStateDisplayAdapter.LEDStateDisplayViewHolder> {

    private SequentialGenerator generator;
    private OnItemClickListener listener;
    private boolean isMutable;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static class LEDStateDisplayViewHolder extends RecyclerView.ViewHolder{

        public ImageView ledColorDisplay;
        public TextView durationDisplay;
        public Button deleteButton;

        public LEDStateDisplayViewHolder(View itemView, final OnItemClickListener listener, boolean isMutable){
            super(itemView);
            ledColorDisplay = itemView.findViewById(R.id.LEDStateColorDisplayImageView);
            durationDisplay = itemView.findViewById(R.id.LEDStateDurationCardText);
            deleteButton = itemView.findViewById(R.id.LEDStateDeleteButton);
            if(isMutable){
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener != null){
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION){
                                listener.onDeleteClick(position);
                            }
                        }
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        if(listener != null){
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION){
                                listener.onItemClick(position);
                            }
                        }
                    }
                });
            } else {
                deleteButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    public LEDStateDisplayAdapter(SequentialGenerator generator, boolean isMutable){
        this.generator = generator;
        this.isMutable = isMutable;
    }

    @NonNull
    @Override
    public LEDStateDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.led_state_card, parent, false);
        LEDStateDisplayViewHolder viewHolder = new LEDStateDisplayViewHolder(view, listener, isMutable);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LEDStateDisplayViewHolder holder, int position) {
        LEDState currentState = generator.getStates().get(position);
        holder.durationDisplay.setText(String.format("Duration (s): \n %s", (currentState.getDuration()/1000.0)));
        displayLEDState(currentState, holder);
    }

    @Override
    public int getItemCount() {
        return generator.getStates().size();
    }

    public SequentialGenerator getGenerator(){
        return generator;
    }

    public void setGenerator(SequentialGenerator generator){
        this.generator = generator;
    }

    public void displayLEDState(LEDState ledState, LEDStateDisplayViewHolder holder){
        //Sets background color appropriately
        GradientDrawable gradientDrawable;
        if(ledState instanceof TransitionLEDState){
            TransitionLEDState transitionLEDState = (TransitionLEDState) ledState;
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{transitionLEDState.getColor().toAndroidColor(),transitionLEDState.getEndColor().toAndroidColor()});
        } else {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{ledState.getColor().toAndroidColor(),ledState.getColor().toAndroidColor()});
        }

        //Sets drawable border and rounded corners
        gradientDrawable.setStroke(8, Color.BLACK);
        gradientDrawable.setCornerRadius(5);
        holder.ledColorDisplay.setBackground(gradientDrawable);
    }
}
