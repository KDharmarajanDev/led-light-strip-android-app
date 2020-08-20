package com.ledlightscheduler.uimanager.simulation;

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
    private SimulationAction action;

    public interface SimulationAction {
        void apply(int color, ImageView background);
    }

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

        public void setupScheduler(Scheduler scheduler, SimulationAction action) {
            Runnable runnable = () -> {
                Color currentColor = scheduler.update();
                while(!Thread.currentThread().isInterrupted()){
                    Color tempColor = scheduler.update();
                    if(!tempColor.equals(currentColor)) {
                        try {
                            java.lang.Thread.sleep(30);
                            action.apply(tempColor.toAndroidColor(), simulationColorView);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        currentColor = tempColor;
                    }
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

    public SimulationRecyclerViewAdapter(ArrayList<SingleColorLEDStrip> strips, SimulationAction action){
        schedulers = new ArrayList<>();
        stopCommands = new ArrayList<>();
        for (SingleColorLEDStrip strip : strips) {
            schedulers.add(new Scheduler(strip));
        }
        this.action = action;
    }

    @NonNull
    @Override
    public SimulationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simulation_card, parent, false);
        return new SimulationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimulationViewHolder holder, int position) {
        holder.setupScheduler(schedulers.get(position), action);
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
