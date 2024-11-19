package com.example.mad_project.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_project.R;
import com.example.mad_project.data.Bus;
import com.example.mad_project.ui.MapActivity;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {
    private List<Bus> busList;
    private OnBusSelectedListener listener;
    private String origin;
    private String destination;

    public BusAdapter(List<Bus> busList, String origin, String destination) {
        this.busList = busList;
        this.origin = origin;
        this.destination = destination;
    }

    public interface OnBusSelectedListener {
        void onBusSelected(Bus bus);
    }

    public BusAdapter(List<Bus> busList, OnBusSelectedListener listener) {
        this.busList = busList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bus_list_item, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        Bus bus = busList.get(position);
        holder.busNumberText.setText(bus.getBusNumber());
        holder.startLocationText.setText(bus.getStartLocation());
        holder.endLocationText.setText(bus.getEndLocation());
        holder.departureTimeText.setText(String.valueOf(bus.getDepartureTime()));
        holder.arrivalTimeText.setText(String.valueOf(bus.getArrivalTime()));
        
        // Handle select button click
        holder.selectButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBusSelected(bus);
            }
            Intent intent = new Intent(v.getContext(), MapActivity.class);
            intent.putExtra("origin", origin);
            intent.putExtra("destination", destination);
            v.getContext().startActivity(intent);
        });

        // Handle card click (excluding select button)
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MapActivity.class);
            intent.putExtra("origin", bus.getStartLocation());
            intent.putExtra("destination", bus.getEndLocation());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return busList != null ? busList.size() : 0;
    }

    public void updateBusList(List<Bus> newBusList) {
        this.busList = newBusList;
        notifyDataSetChanged();
    }

    static class BusViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView busNumberText, startLocationText, endLocationText;
        TextView departureTimeText, arrivalTimeText;
        Button selectButton;

        BusViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            busNumberText = itemView.findViewById(R.id.busNumberText);
            startLocationText = itemView.findViewById(R.id.startLocationText);
            endLocationText = itemView.findViewById(R.id.endLocationText);
            departureTimeText = itemView.findViewById(R.id.departureTimeText);
            arrivalTimeText = itemView.findViewById(R.id.arrivalTimeText);
            selectButton = itemView.findViewById(R.id.selectButton);
        }
    }
}
