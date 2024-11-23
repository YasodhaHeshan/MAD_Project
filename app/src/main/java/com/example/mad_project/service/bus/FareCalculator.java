package com.example.mad_project.service.bus;

import com.example.mad_project.data.Bus;

public class FareCalculator {
    public static class FareBreakdown {
        public double baseFare;
        public double premiumCharge;
        public double bookingFee;
        public double totalFare;
    }

    public static FareBreakdown calculateFare(Bus bus, String seatType) {
        FareBreakdown breakdown = new FareBreakdown();
        
        // Base fare from bus
        breakdown.baseFare = bus.getBaseFare();
        
        // Premium seats cost more
        if (seatType.equals("PREMIUM")) {
            breakdown.premiumCharge = bus.getPremiumFare() - bus.getBaseFare();
        }
        
        // Booking fee
        breakdown.bookingFee = breakdown.baseFare * 0.05; // 5% booking fee
        
        // Calculate total
        breakdown.totalFare = breakdown.baseFare + 
                            breakdown.premiumCharge + 
                            breakdown.bookingFee;
                            
        return breakdown;
    }
}
