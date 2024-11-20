package com.example.mad_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_project.R;
import com.example.mad_project.data.Bus;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {
    private List<Bus> buses;
    private OnBusClickListener listener;

    public interface OnBusClickListener {
        void onBusClick(Bus bus);
    }

    public BusAdapter(List<Bus> buses, OnBusClickListener listener) {
        this.buses = buses;
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
        Bus bus = buses.get(position);
        holder.bind(bus, listener);
    }

    @Override
    public int getItemCount() {
        return buses.size();
    }

    static class BusViewHolder extends RecyclerView.ViewHolder {
        TextView busNumber;
        TextView route;
        TextView seats;

        BusViewHolder(View itemView) {
            super(itemView);
            busNumber = itemView.findViewById(R.id.busId);
            route = itemView.findViewById(R.id.startLocationText);
            seats = itemView.findViewById(R.id.seatNumberText);
        }

        void bind(Bus bus, OnBusClickListener listener) {
            busNumber.setText(bus.getRegistrationNumber());
            route.setText(String.format("%s → %s", bus.getRouteFrom(), bus.getRouteTo()));
            seats.setText(String.format("%d seats available", bus.getTotalSeats()));
            itemView.setOnClickListener(v -> listener.onBusClick(bus));
        }
    }
}
