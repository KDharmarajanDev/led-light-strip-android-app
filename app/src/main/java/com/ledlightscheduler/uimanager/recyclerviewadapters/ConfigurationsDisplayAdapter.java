package com.ledlightscheduler.uimanager.recyclerviewadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ledlightscheduler.R;

import java.util.ArrayList;

public class ConfigurationsDisplayAdapter extends RecyclerView.Adapter<ConfigurationsDisplayAdapter.ConfigurationDisplayViewHolder> {

    private ArrayList<String> configNames;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    public static class ConfigurationDisplayViewHolder extends RecyclerView.ViewHolder{

        //UI Elements
        private Button deleteButton;
        private Button selectButton;
        private TextView nameText;

        public ConfigurationDisplayViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);

            setupUIElements(itemView);
            setupButtons(listener);
        }

        public void setupUIElements(View itemView) {
            deleteButton = itemView.findViewById(R.id.ConfigurationDeleteButton);
            selectButton = itemView.findViewById(R.id.SelectConfigurationButton);
            nameText = itemView.findViewById(R.id.ConfigurationNameText);
        }

        public void setupButtons(final OnItemClickListener listener) {
            deleteButton.setOnClickListener(view -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onDeleteClick(position);
                    }
                }
            });
            selectButton.setOnClickListener(view -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
        }

        public void changeText(String text) {
            nameText.setText(text);
        }
    }

    public ConfigurationsDisplayAdapter(ArrayList<String> configNames){
        this.configNames = configNames;
    }

    public ArrayList<String> getConfigNames() {
        return configNames;
    }

    @NonNull
    @Override
    public ConfigurationDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.configuration_card, parent, false);
        return new ConfigurationDisplayViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigurationDisplayViewHolder holder, int position) {
        holder.changeText(configNames.get(position));
    }

    @Override
    public int getItemCount() {
        return configNames.size();
    }

    public void addName(String name) {
        configNames.add(name);
        notifyItemInserted(configNames.size()-1);
    }

    public void removeName(int position) {
        configNames.remove(position);
        notifyItemRemoved(position);
    }

}
