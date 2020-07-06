package com.ledlightscheduler.uimanager.recyclerviewadapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledlightscheduler.R;
import com.ledlightscheduler.ledstriputilities.generators.RandomGenerator;
import com.ledlightscheduler.ledstriputilities.generators.SequentialGenerator;
import com.ledlightscheduler.ledstriputilities.ledstrips.SingleColorLEDStrip;
import com.ledlightscheduler.uimanager.recyclerviewpsacer.HorizontalSpaceItemDecoration;

public class SequentialGeneratorDisplayAdapter extends RecyclerView.Adapter<SequentialGeneratorDisplayAdapter.SequentialGeneratorDisplayViewHolder>{

    private SingleColorLEDStrip ledStrip;
    private OnItemClickListener listener;
    private boolean isMutable;
    private boolean shouldPassListener;
    private int desiredPosition;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onApplyClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static class SequentialGeneratorDisplayViewHolder extends RecyclerView.ViewHolder{

        private Button activateButton;
        private Button deleteButton;
        private TextView generatorTypeText;
        private RecyclerView ledStateRecyclerView;
        private LEDStateDisplayAdapter ledStateDisplayViewAdapter;
        private RecyclerView.LayoutManager ledStateDisplayLayoutManager;

        public SequentialGeneratorDisplayViewHolder(View itemView, final OnItemClickListener listener, boolean isMutable) {
            super(itemView);

            setUpUIElements(isMutable);
            setUpButtons(listener);
        }

        public void setUpUIElements(boolean isMutable){
            activateButton = itemView.findViewById(R.id.ApplySequentialGeneratorButton);
            deleteButton = itemView.findViewById(R.id.DeleteSequentialGeneratorButton);
            generatorTypeText = itemView.findViewById(R.id.GeneratorTypeText);
            ledStateRecyclerView = itemView.findViewById(R.id.SequentialGeneratorLEDStatesRecyclerView);
            if(!isMutable){
                activateButton.setVisibility(View.INVISIBLE);
                deleteButton.setVisibility(View.INVISIBLE);
            }
        }

        public void setUpButtons(final OnItemClickListener listener){
            activateButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(listener != null){
                                int position = getAdapterPosition();
                                if(position != RecyclerView.NO_POSITION){
                                    listener.onApplyClick(position);
                                }
                            }
                        }
                    }
            );

            deleteButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(listener != null){
                                int position = getAdapterPosition();
                                if(position != RecyclerView.NO_POSITION){
                                    listener.onDeleteClick(position);
                                }
                            }
                        }
                    }
            );

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(listener != null){
                                int position = getAdapterPosition();
                                if(position != RecyclerView.NO_POSITION){
                                    listener.onItemClick(position);
                                }
                            }
                        }
                    }
            );
        }

        public void setGeneratorTypeText(String text){
            generatorTypeText.setText(text);
        }

        public void setUpLEDStateDisplayRecyclerView(SequentialGenerator generator, boolean shouldPassListener, OnItemClickListener listener, int desiredPosition){
            ledStateDisplayViewAdapter = new LEDStateDisplayAdapter(generator, false);
            ledStateDisplayLayoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            ledStateRecyclerView.setLayoutManager(ledStateDisplayLayoutManager);
            ledStateRecyclerView.setAdapter(ledStateDisplayViewAdapter);
            ledStateRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(7));
            if(shouldPassListener){
                ledStateDisplayViewAdapter.setOnItemClickListener(
                        new LEDStateDisplayAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                if(desiredPosition >= 0) {
                                    listener.onItemClick(desiredPosition);
                                } else {
                                    listener.onItemClick(getAdapterPosition());
                                }
                            }

                            @Override
                            public void onDeleteClick(int position) {
                            }
                        }
                );
            }
        }
    }

    public SequentialGeneratorDisplayAdapter(SingleColorLEDStrip ledStrip, boolean isMutable, boolean shouldPassListener){
        this(ledStrip, isMutable, shouldPassListener, -1);
    }

    public SequentialGeneratorDisplayAdapter(SingleColorLEDStrip ledStrip, boolean isMutable, boolean shouldPassListener, int desiredPosition){
        this.desiredPosition = desiredPosition;
        this.ledStrip = ledStrip;
        this.isMutable = isMutable;
        this.shouldPassListener = shouldPassListener;
    }

    @NonNull
    @Override
    public SequentialGeneratorDisplayAdapter.SequentialGeneratorDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sequential_generator_card, parent, false);
        SequentialGeneratorDisplayViewHolder viewHolder = new SequentialGeneratorDisplayViewHolder(view, listener, isMutable);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SequentialGeneratorDisplayAdapter.SequentialGeneratorDisplayViewHolder holder, int position) {
        SequentialGenerator generator = ledStrip.getGenerators().get(position);
        displayGeneratorTypeText(holder, generator);
        holder.setUpLEDStateDisplayRecyclerView(generator, shouldPassListener, listener, desiredPosition);
        if(getItemCount() <= 1){
            ledStrip.setCurrentGeneratorIndex(0);
            holder.itemView.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.activated_sequential_generator));
        } else if(position == ledStrip.getActivatedGeneratorIndex()){
            holder.itemView.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.activated_sequential_generator));
        }
    }

    @Override
    public int getItemCount() {
        return ledStrip.getGenerators().size();
    }

    public int getActiveGenerator(){
        return ledStrip.getActivatedGeneratorIndex();
    }

    public void setActiveGenerator(int index){
        ledStrip.setCurrentGeneratorIndex(index);
    }

    public void displayGeneratorTypeText(@NonNull SequentialGeneratorDisplayAdapter.SequentialGeneratorDisplayViewHolder holder, SequentialGenerator generator){
        if(generator instanceof RandomGenerator){
            holder.setGeneratorTypeText("Generator Type: Random");
        } else {
            holder.setGeneratorTypeText("Generator Type: Sequential");
        }
    }
}
