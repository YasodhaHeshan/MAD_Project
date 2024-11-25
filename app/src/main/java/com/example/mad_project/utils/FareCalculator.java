package com.example.mad_project.utils;

import com.example.mad_project.data.Bus;

public class FareCalculator {
    public static class FareBreakdown {
        public double baseFare;
        public double bookingFee;
        public double totalFare;
    }

    public static FareBreakdown calculateFare(Bus bus) {
        FareBreakdown breakdown = new FareBreakdown();
        
        // Base fare from bus
        breakdown.baseFare = bus.getBaseFare();
        
        // Booking fee
        breakdown.bookingFee = breakdown.baseFare * 0.05; // 5% booking fee
        
        // Calculate total
        breakdown.totalFare = breakdown.baseFare + breakdown.bookingFee;
                            
        return breakdown;
    }
}
