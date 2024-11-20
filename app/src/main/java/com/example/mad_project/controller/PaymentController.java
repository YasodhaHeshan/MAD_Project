package com.example.mad_project.controller;

import android.content.Context;
import android.util.Log;
import androidx.room.Room;

import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.Payment;
import com.example.mad_project.data.PaymentDao;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class PaymentController {
    private final AppDatabase db;
    private final PaymentDao paymentDao;
    private final ExecutorService executorService;
    private static final String TAG = "PaymentController";

    public PaymentController(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(), 
            AppDatabase.class, "mad_project_db").build();
        paymentDao = db.paymentDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void processPayment(Payment payment, Consumer<Boolean> callback) {
        executorService.execute(() -> {
            try {
                // Generate unique transaction ID
                payment.setTransactionId("TXN" + System.currentTimeMillis());
                
                // Insert payment record
                long paymentId = paymentDao.insert(payment);
                
                // Simulate payment processing
                boolean paymentSuccess = processPaymentWithGateway(payment);
                
                if (paymentSuccess) {
                    payment.setStatus("COMPLETED");
                } else {
                    payment.setStatus("FAILED");
                }
                
                payment.setUpdatedAt(System.currentTimeMillis());
                paymentDao.update(payment);
                
                callback.accept(paymentSuccess);
            } catch (Exception e) {
                Log.e(TAG, "Error processing payment", e);
                callback.accept(false);
            }
        });
    }

    private boolean processPaymentWithGateway(Payment payment) {
        // Simulate payment gateway integration
        try {
            Thread.sleep(2000); // Simulate network delay
            return Math.random() > 0.1; // 90% success rate
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void getPaymentHistory(int userId, Consumer<List<Payment>> callback) {
        executorService.execute(() -> {
            try {
                List<Payment> payments = paymentDao.getPaymentsByUserId(userId);
                callback.accept(payments);
            } catch (Exception e) {
                Log.e(TAG, "Error getting payment history", e);
                callback.accept(Collections.emptyList());
            }
        });
    }

    public void verifyPayment(String transactionId, Consumer<Payment> callback) {
        executorService.execute(() -> {
            try {
                Payment payment = paymentDao.getPaymentByTransactionId(transactionId);
                callback.accept(payment);
            } catch (Exception e) {
                Log.e(TAG, "Error verifying payment", e);
                callback.accept(null);
            }
        });
    }
}
