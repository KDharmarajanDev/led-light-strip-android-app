package com.ledlightscheduler.uimanager.simulation;

import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ledlightscheduler.R;
import com.ledlightscheduler.ledstriputilities.Scheduler;
import com.ledlightscheduler.ledstriputilities.ledstates.Color;
import com.ledlightscheduler.ledstriputilities.ledstrips.SingleColorLEDStrip;

import java.util.ArrayList;

public class SimulationRecyclerViewAdapter extends RecyclerView.Adapter<SimulationRecyclerViewAdapter.SimulationViewHolder>{

    private ArrayList<Scheduler> schedulers;
    private ArrayList<Runnable> stopCommands;
    private Handler handler;

    public static class SimulationViewHolder extends RecyclerView.ViewHolder{

        public ImageView simulationColorView;
        public Thread schedulerThread;

        public SimulationViewHolder(View itemView) {
            super(itemView);
            setupUIElements(itemView);
        }

        public void setupUIElements(View itemView) {
            simulationColorView = itemView.findViewById(R.id.SimulationColorImageView);
        }

        public void setupScheduler(Scheduler scheduler, Handler handler) {
            Runnable runnable = () -> {
                Color currentColor = scheduler.update();
                while(!Thread.currentThread().isInterrupted()){
                    Color tempColor = scheduler.update();
                    if(!tempColor.equals(currentColor)) {
                        handler.post(() -> {
                            GradientDrawable drawable = (GradientDrawable) simulationColorView.getBackground().mutate();
                            drawable.setColor(tempColor.toAndroidColor());
                        });
                        currentColor = tempColor;
                    }
                    Log.v("Test", "Running!");
                }
            };
            schedulerThread = new Thread(runnable);
            schedulerThread.start();
        }

        public void stopScheduler(){
            if(schedulerThread != null){
                schedulerThread.interrupt();
            }
        }

    }

    public SimulationRecyclerViewAdapter(ArrayList<SingleColorLEDStrip> strips){
        schedulers = new ArrayList<>();
        stopCommands = new ArrayList<>();
        for (SingleColorLEDStrip strip : strips) {
            schedulers.add(new Scheduler(strip));
        }
        handler = new Handler();
    }

    @NonNull
    @Override
    public SimulationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simulation_card, parent, false);
        return new SimulationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimulationViewHolder holder, int position) {
        holder.setupScheduler(schedulers.get(position), handler);
        stopCommands.add(holder::stopScheduler);
    }

    @Override
    public int getItemCount() {
        return schedulers.size();
    }

    public void stopSchedulers() {
        for (Runnable runnable : stopCommands){
            runnable.run();
        }
    }
}
