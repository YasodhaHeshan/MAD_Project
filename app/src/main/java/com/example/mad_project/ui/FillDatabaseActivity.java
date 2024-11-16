package com.example.mad_project.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.BusDao;
import com.example.mad_project.data.BusDriver;
import com.example.mad_project.data.BusDriverDao;
import com.example.mad_project.data.BusOwner;
import com.example.mad_project.data.BusOwnerDao;
import com.example.mad_project.data.Ticket;
import com.example.mad_project.data.TicketDao;
import com.example.mad_project.data.User;
import com.example.mad_project.data.UserDao;

public class FillDatabaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "mad_project_db").build();

        // Insert Users
        UserDao userDao = db.userDao();
        User user1 = new User("John", "Doe", "john.doe@example.com", "1234567890", "password123");
        User user2 = new User("Jane", "Smith", "jane.smith@example.com", "0987654321", "password456");
        User user3 = new User("Alice", "Johnson", "alice.johnson@example.com", "1122334455", "password789");
        User user4 = new User("Bob", "Brown", "bob.brown@example.com", "2233445566", "password012");
        userDao.insert(user1);
        userDao.insert(user2);
        userDao.insert(user3);
        userDao.insert(user4);

        // Insert Bus Owners
        BusOwnerDao busOwnerDao = db.busOwnerDao();
        BusOwner busOwner1 = new BusOwner(user1.getId(), "Company A", "LIC123", "NIC123");
        BusOwner busOwner2 = new BusOwner(user2.getId(), "Company B", "LIC456", "NIC456");
        BusOwner busOwner3 = new BusOwner(user3.getId(), "Company C", "LIC789", "NIC789");
        BusOwner busOwner4 = new BusOwner(user4.getId(), "Company D", "LIC012", "NIC012");
        busOwnerDao.insert(busOwner1);
        busOwnerDao.insert(busOwner2);
        busOwnerDao.insert(busOwner3);
        busOwnerDao.insert(busOwner4);

        // Insert Bus Drivers
        BusDriverDao busDriverDao = db.busDriverDao();
        BusDriver busDriver1 = new BusDriver(user1.getId(), "LIC789", "NIC789");
        BusDriver busDriver2 = new BusDriver(user2.getId(), "LIC012", "NIC012");
        BusDriver busDriver3 = new BusDriver(user3.getId(), "LIC345", "NIC345");
        BusDriver busDriver4 = new BusDriver(user4.getId(), "LIC678", "NIC678");
        busDriverDao.insert(busDriver1);
        busDriverDao.insert(busDriver2);
        busDriverDao.insert(busDriver3);
        busDriverDao.insert(busDriver4);

        // Insert Buses
        BusDao busDao = db.busDao();
        Bus bus1 = new Bus("Bus001", busOwner1.getId(), busDriver1.getId(), 1, 1, "Location A", "Location B", "08:00", "10:00", 40);
        Bus bus2 = new Bus("Bus002", busOwner2.getId(), busDriver2.getId(), 2, 2, "Location C", "Location D", "09:00", "11:00", 35);
        Bus bus3 = new Bus("Bus003", busOwner3.getId(), busDriver3.getId(), 3, 3, "Location E", "Location F", "10:00", "12:00", 30);
        Bus bus4 = new Bus("Bus004", busOwner4.getId(), busDriver4.getId(), 4, 4, "Location G", "Location H", "11:00", "13:00", 25);
        busDao.insert(bus1);
        busDao.insert(bus2);
        busDao.insert(bus3);
        busDao.insert(bus4);

        // Insert Tickets
        TicketDao ticketDao = db.ticketDao();
        Ticket ticket1 = new Ticket(user1.getId(), bus1.getId(), 40, "T001", "2023-01-01", "2023-01-02");
        Ticket ticket2 = new Ticket(user2.getId(), bus2.getId(), 35, "T002", "2023-01-01", "2023-01-02");
        Ticket ticket3 = new Ticket(user3.getId(), bus3.getId(), 30, "T003", "2023-01-01", "2023-01-02");
        Ticket ticket4 = new Ticket(user4.getId(), bus4.getId(), 25, "T004", "2023-01-01", "2023-01-02");
        ticketDao.insert(ticket1);
        ticketDao.insert(ticket2);
        ticketDao.insert(ticket3);
        ticketDao.insert(ticket4);

        System.out.println("Database populated with test data.");
    }
}