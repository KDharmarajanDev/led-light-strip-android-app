package com.ledlightscheduler.uimanager.recyclerviewadapters;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledlightscheduler.R;
import com.ledlightscheduler.ledstriputilities.Scheduler;
import com.ledlightscheduler.ledstriputilities.ledstrips.SingleColorLEDStrip;
import com.ledlightscheduler.uimanager.recyclerviewpsacer.VerticalSpaceItemDecoration;

import java.util.ArrayList;

public class LEDStripDisplayAdapter extends RecyclerView.Adapter<LEDStripDisplayAdapter.LEDStripDisplayViewHolder> {

    private OnItemClickListener listener;
    private ArrayList<Scheduler> schedulers;
    private boolean shouldPassListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public static class LEDStripDisplayViewHolder extends RecyclerView.ViewHolder{

        public Button deleteButton;
        public TextView redPinText;
        public TextView greenPinText;
        public TextView bluePinText;
        public RecyclerView sequentialGeneratorRecyclerView;
        public SequentialGeneratorDisplayAdapter sequentialGeneratorViewAdapter;
        public RecyclerView.LayoutManager sequentialGeneratorLayoutManager;

        public LEDStripDisplayViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            setUpUIElements();
            setUpButton(listener);
            setUpBackground();

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemClick(getAdapterPosition());
                        }
                    }
            );
        }

        public void setUpUIElements(){
            deleteButton = itemView.findViewById(R.id.LEDStripDeleteButton);
            redPinText = itemView.findViewById(R.id.LEDStripCardRedPinText);
            greenPinText = itemView.findViewById(R.id.LEDStripCardGreenPinText);
            bluePinText = itemView.findViewById(R.id.LEDStripCardBluePinText);
            sequentialGeneratorRecyclerView = itemView.findViewById(R.id.SequentialGeneratorRecyclerView);
        }

        public void setUpButton(final OnItemClickListener listener){
            deleteButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onDeleteClick(getAdapterPosition());
                        }
                    }
            );
        }

        public void setUpRecyclerView(SingleColorLEDStrip ledStrip, boolean shouldPassListener, final OnItemClickListener listener){
            sequentialGeneratorViewAdapter = new SequentialGeneratorDisplayAdapter(ledStrip, false, true, getAdapterPosition());
            sequentialGeneratorLayoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
            sequentialGeneratorRecyclerView.setLayoutManager(sequentialGeneratorLayoutManager);
            sequentialGeneratorRecyclerView.setAdapter(sequentialGeneratorViewAdapter);
            sequentialGeneratorRecyclerView.setNestedScrollingEnabled(false);
            sequentialGeneratorRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10));
            sequentialGeneratorRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    int action = e.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_MOVE:
                            rv.getParent().requestDisallowInterceptTouchEvent(true);
                            break;
                    }
                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                }
            });
            if(shouldPassListener) {
                sequentialGeneratorViewAdapter.setOnItemClickListener(
                        new SequentialGeneratorDisplayAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                listener.onItemClick(getAdapterPosition());
                            }

                            @Override
                            public void onDeleteClick(int position) {
                            }

                            @Override
                            public void onApplyClick(int position) {
                            }
                        }
                );
                sequentialGeneratorRecyclerView.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.onItemClick(getAdapterPosition());
                            }
                        }
                );
            }
        }

        public void setUpPinText(int redPin, int greenPin, int bluePin){
            redPinText.setText("Red Pin: " + redPin);
            greenPinText.setText("Green Pin: " + greenPin);
            bluePinText.setText("Blue Pin: " + bluePin);
        }

        public void setUpBackground(){
            itemView.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.blue_border));
        }
    }

    public LEDStripDisplayAdapter(ArrayList<SingleColorLEDStrip> strips, boolean shouldPassListener){
        schedulers = new ArrayList<>();
        for(SingleColorLEDStrip strip : strips){
            schedulers.add(new Scheduler(strip));
        }
        this.shouldPassListener = shouldPassListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public ArrayList<Scheduler> getSchedulers(){
        return schedulers;
    }

    public void setSchedulers(ArrayList<Scheduler> schedulers){
        this.schedulers = schedulers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LEDStripDisplayAdapter.LEDStripDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.led_strip_card, parent, false);
        LEDStripDisplayViewHolder viewHolder = new LEDStripDisplayViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LEDStripDisplayViewHolder holder, int position) {
        SingleColorLEDStrip ledStrip = schedulers.get(position).getLEDStrip();
        displayLEDStrip(holder, ledStrip);
    }

    @Override
    public int getItemCount() {
        return schedulers.size();
    }

    public void displayLEDStrip(LEDStripDisplayViewHolder holder, SingleColorLEDStrip ledStrip){
        holder.setUpRecyclerView(ledStrip, shouldPassListener, listener);
        holder.setUpPinText(ledStrip.getRedPin(), ledStrip.getGreenPin(), ledStrip.getBluePin());
    }

}
