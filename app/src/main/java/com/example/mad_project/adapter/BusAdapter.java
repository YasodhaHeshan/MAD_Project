package com.example.mad_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mad_project.R;
import com.example.mad_project.data.Bus;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {
    private final List<Bus> buses;
    private final OnBusClickListener listener;
    private final NumberFormat currencyFormat;

    public interface OnBusClickListener {
        void onBusClick(Bus bus);
        void onBookClick(Bus bus);
    }

    public BusAdapter(List<Bus> buses, OnBusClickListener listener) {
        this.buses = buses;
        this.listener = listener;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "LK"));
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
        
        String basePrice = currencyFormat.format(bus.getBaseFare());

        holder.priceText.setText(basePrice);
        
        holder.baseFareText.setText(basePrice);

        holder.itemView.setOnClickListener(v -> {
            boolean isExpanded = holder.priceBreakdownLayout.getVisibility() == View.VISIBLE;
            holder.priceBreakdownLayout.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
            holder.priceText.setText(basePrice);
            
            listener.onBusClick(bus);
        });
        
        holder.bookButton.setOnClickListener(v -> {
            listener.onBookClick(bus);
        });
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
        private final TextView priceText;
        private final TextView baseFareText;
        private final View priceBreakdownLayout;
        private final Button bookButton;

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
            priceText = itemView.findViewById(R.id.priceText);
            baseFareText = itemView.findViewById(R.id.baseFareText);
            priceBreakdownLayout = itemView.findViewById(R.id.priceBreakdownLayout);
            bookButton = itemView.findViewById(R.id.bookButton);
        }
    }
}