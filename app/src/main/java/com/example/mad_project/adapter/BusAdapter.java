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
    private final List<Bus> buses;
    private final OnBusClickListener listener;

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
        
        holder.busNumberText.setText(bus.getRegistrationNumber());
        holder.startLocationText.setText(bus.getRouteFrom());
        holder.endLocationText.setText(bus.getRouteTo());
        holder.busModelText.setText(bus.getModel());
        holder.amenitiesText.setText(bus.getAmenities());
        holder.seatsText.setText(String.format("%d seats available", bus.getCapacity()));
        holder.departureTimeText.setText(bus.getFormattedDepartureTime());
        holder.arrivalTimeText.setText(bus.getFormattedArrivalTime());
        
        holder.itemView.setOnClickListener(v -> listener.onBusClick(bus));
    }

    @Override
    public int getItemCount() {
        return buses.size();
    }

    class BusViewHolder extends RecyclerView.ViewHolder {
        private final TextView busNumberText;
        private final TextView startLocationText;
        private final TextView endLocationText;
        private final TextView busModelText;
        private final TextView amenitiesText;
        private final TextView seatsText;
        private final TextView departureTimeText;
        private final TextView arrivalTimeText;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            busNumberText = itemView.findViewById(R.id.busNumberText);
            startLocationText = itemView.findViewById(R.id.startLocationText);
            endLocationText = itemView.findViewById(R.id.endLocationText);
            busModelText = itemView.findViewById(R.id.busModelText);
            amenitiesText = itemView.findViewById(R.id.amenitiesText);
            seatsText = itemView.findViewById(R.id.seatsText);
            departureTimeText = itemView.findViewById(R.id.departureTimeText);
            arrivalTimeText = itemView.findViewById(R.id.arrivalTimeText);
        }
    }
}