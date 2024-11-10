// BusListAdapter.java
package com.example.mad_project.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_project.R;
import com.example.mad_project.data.Bus;

import java.util.List;

public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.BusViewHolder> {

    private List<Bus> busList;

    public BusListAdapter(List<Bus> busList) {
        this.busList = busList;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_recycle_view, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        Bus bus = busList.get(position);
        holder.departureTime.setText(bus.getDepartureTime());
        holder.arrivalTime.setText(bus.getArrivalTime());
        holder.departureLocation.setText(bus.getDepartureLocation());
        holder.arrivalLocation.setText(bus.getArrivalLocation());
        holder.availableSeats.setText(String.valueOf(bus.getAvailableSeats()));
        holder.ticketPrice.setText(String.valueOf(bus.getTicketPrice()));
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    static class BusViewHolder extends RecyclerView.ViewHolder {
        TextView departureTime, arrivalTime, departureLocation, arrivalLocation, availableSeats, ticketPrice;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            departureTime = itemView.findViewById(R.id.departure_time);
            arrivalTime = itemView.findViewById(R.id.arrival_time);
            departureLocation = itemView.findViewById(R.id.departure_location);
            arrivalLocation = itemView.findViewById(R.id.arrival_location);
            availableSeats = itemView.findViewById(R.id.available_seats);
            ticketPrice = itemView.findViewById(R.id.book_now_button);
        }
    }
}