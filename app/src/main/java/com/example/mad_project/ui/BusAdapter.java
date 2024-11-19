package com.example.mad_project.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mad_project.R;
import com.example.mad_project.controller.TicketController;
import com.example.mad_project.data.Bus;
import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {

    private final List<Bus> busList;
    private final TicketController ticketController;

    public BusAdapter(List<Bus> busList, TicketController ticketController) {
        this.busList = busList;
        this.ticketController = ticketController;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_recycle_view, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        try {
            Bus bus = busList.get(position);
            
            holder.departureTime.setText(bus.getDepartureTime() != null ? bus.getDepartureTime() : "N/A");
            holder.arrivalTime.setText(bus.getArrivalTime() != null ? bus.getArrivalTime() : "N/A");
            holder.departureLocation.setText(bus.getStartLocation() != null ? bus.getStartLocation() : "N/A");
            holder.arrivalLocation.setText(bus.getEndLocation() != null ? bus.getEndLocation() : "N/A");
            holder.availableSeats.setText(bus.getTotalSeats() + " " + 
                holder.itemView.getContext().getString(R.string.tickets_bus_adapter));

            // Load ticket price using TicketController
            if (ticketController != null) {
                ticketController.getTicketPriceByBusId(bus.getId(), ticketPrice ->
                    holder.ticketPrice.post(() -> {
                        if (holder.ticketPrice != null) {
                            holder.ticketPrice.setText(holder.itemView.getContext().getString(R.string.ticket_price) 
                                + " " + ticketPrice);
                        }
                    })
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public static class BusViewHolder extends RecyclerView.ViewHolder {
        TextView departureTime, arrivalTime, departureLocation, arrivalLocation, ticketPrice, availableSeats;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            departureTime = itemView.findViewById(R.id.departure_time);
            arrivalTime = itemView.findViewById(R.id.arrival_time);
            departureLocation = itemView.findViewById(R.id.departure_location);
            arrivalLocation = itemView.findViewById(R.id.arrival_location);
            ticketPrice = itemView.findViewById(R.id.one_way_ticket);
            availableSeats = itemView.findViewById(R.id.available_seats);
        }
    }
}