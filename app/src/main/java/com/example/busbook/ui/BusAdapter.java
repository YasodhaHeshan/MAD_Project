package com.example.busbook.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.busbook.R;
import com.example.busbook.controller.TicketController;
import com.example.busbook.data.Bus;
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
        Bus bus = busList.get(position);
        holder.departureTime.setText(bus.getDepartureTime());
        holder.arrivalTime.setText(bus.getArrivalTime());
        holder.departureLocation.setText(bus.getDepartureLocation());
        holder.arrivalLocation.setText(bus.getArrivalLocation());
        holder.availableSeats.setText(bus.getAvailableSeats() + R.string.tickets_bus_adapter);

        // Load ticket price using TicketController
        ticketController.getTicketPriceByBusId(bus.getId(), ticketPrice ->
            holder.ticketPrice.post(() -> holder.ticketPrice.setText(R.string.ticket_price + ticketPrice))
        );
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