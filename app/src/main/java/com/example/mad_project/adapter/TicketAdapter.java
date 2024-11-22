package com.example.mad_project.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_project.R;
import com.example.mad_project.data.Ticket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private final List<Ticket> tickets;
    private final OnTicketClickListener listener;

    public interface OnTicketClickListener {
        void onTicketClick(Ticket ticket);
    }

    public TicketAdapter(List<Ticket> tickets, OnTicketClickListener listener) {
        this.tickets = tickets;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_item, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        holder.bind(tickets.get(position));
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    class TicketViewHolder extends RecyclerView.ViewHolder {
        private final TextView ticketNumberText;
        private final TextView startLocationText;
        private final TextView endLocationText;
        private final TextView ticketStatusText;
        private final TextView journeyDateText;
        private final TextView busDetailsText;
        private final TextView seatNumberText;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketNumberText = itemView.findViewById(R.id.ticketNumberText);
            startLocationText = itemView.findViewById(R.id.startLocationText);
            endLocationText = itemView.findViewById(R.id.endLocationText);
            ticketStatusText = itemView.findViewById(R.id.ticketStatusText);
            journeyDateText = itemView.findViewById(R.id.journeyDateText);
            busDetailsText = itemView.findViewById(R.id.busDetailsText);
            seatNumberText = itemView.findViewById(R.id.seatNumberText);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onTicketClick(tickets.get(position));
                }
            });
        }

        public void bind(Ticket ticket) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault());
            
            ticketNumberText.setText("Ticket #" + ticket.getId());
            startLocationText.setText(ticket.getSource());
            endLocationText.setText(ticket.getDestination());
            ticketStatusText.setText(ticket.getStatus());
            journeyDateText.setText(dateFormat.format(new Date(ticket.getJourneyDate())));
            seatNumberText.setText("Seat: " + ticket.getSeatNumber());
            
            // Set status background color based on ticket status
            GradientDrawable background = (GradientDrawable) ticketStatusText.getBackground();
            int color;
            switch (ticket.getStatus().toLowerCase()) {
                case "booked":
                    color = ContextCompat.getColor(itemView.getContext(), R.color.green_dark);
                    break;
                case "cancelled":
                    color = ContextCompat.getColor(itemView.getContext(), R.color.red);
                    break;
                default:
                    color = ContextCompat.getColor(itemView.getContext(), R.color.gray);
                    break;
            }
            background.setColor(color);
            
            // Get bus details if needed
            // busDetailsText.setText(String.format("Bus: %s (%s)", bus.getRegistrationNumber(), bus.getModel()));
        }
    }
}
