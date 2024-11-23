package com.example.mad_project.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_project.R;
import com.example.mad_project.adapter.TicketAdapter;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.PaymentDao;
import com.example.mad_project.data.Ticket;
import com.example.mad_project.data.TicketDao;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.text.NumberFormat;
import java.util.Locale;

public class TicketsActivity extends BaseActivity {
    private RecyclerView ticketsRecyclerView;
    private TextView emptyStateText;
    private AppDatabase db;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        setupActionBar("My Tickets", true, true, true);
        
        // Initialize database
        db = AppDatabase.getDatabase(this);
        initializeViews();
        loadTickets();
    }

    private void initializeViews() {
        ticketsRecyclerView = findViewById(R.id.ticketsRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);
        
        ticketsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadTickets() {
        executor.execute(() -> {
            TicketDao ticketDao = db.ticketDao();
            PaymentDao paymentDao = db.paymentDao();
            List<Ticket> tickets = ticketDao.getAllTickets();

            runOnUiThread(() -> {
                if (tickets.isEmpty()) {
                    showEmptyState();
                } else {
                    displayTickets(tickets, paymentDao);
                }
            });
        });
    }

    private void showEmptyState() {
        ticketsRecyclerView.setVisibility(View.GONE);
        emptyStateText.setVisibility(View.VISIBLE);
    }

    private void displayTickets(List<Ticket> tickets, PaymentDao paymentDao) {
        ticketsRecyclerView.setVisibility(View.VISIBLE);
        emptyStateText.setVisibility(View.GONE);
        
        TicketAdapter adapter = new TicketAdapter(tickets, this::showTicketDetails, paymentDao);
        ticketsRecyclerView.setAdapter(adapter);
    }

    private void showTicketDetails(Ticket ticket) {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.ticket_details_sheet, null);
        
        // Set up ticket details in the bottom sheet
        // TODO: Add ticket details implementation
        
        bottomSheet.setContentView(bottomSheetView);
        bottomSheet.show();
    }
}
