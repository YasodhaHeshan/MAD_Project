package com.example.mad_project.ui;

import android.os.Bundle;

import com.example.mad_project.R;

public class PaymentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Setup actionbar with title "Payment"
        setupActionBar("Payment", true, true, true);
    }
}