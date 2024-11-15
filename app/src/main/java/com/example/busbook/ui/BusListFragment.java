// BusListFragment.java
package com.example.busbook.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.busbook.R;
import com.example.busbook.controller.BusController;
import com.example.busbook.controller.TicketController;
import com.example.busbook.data.Bus;
import java.util.List;

public class BusListFragment extends Fragment {

    private RecyclerView busRecyclerView;
    private BusAdapter busAdapter;
    private BusController busController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_list, container, false);
        busRecyclerView = view.findViewById(R.id.busRecyclerView);
        busRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        busController = new BusController(getContext());
        busController.getAllBuses(new BusController.BusCallback() {
            @Override
            public void onBusesLoaded(List<Bus> busList) {
                TicketController ticketController = new TicketController(getContext());
                busAdapter = new BusAdapter(busList, ticketController);
                busRecyclerView.setAdapter(busAdapter);
            }
        });

        return view;
    }
}