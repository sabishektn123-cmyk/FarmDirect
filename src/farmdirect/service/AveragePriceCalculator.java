package farmdirect.service;

import java.util.List;

public class AveragePriceCalculator
        implements PriceCalculator {

    @Override
    public double calculateFairPrice(List<Double> farmerPrices) {

        if (farmerPrices == null || farmerPrices.isEmpty()) {
            return 0.0;
        }

        double total = 0;

        for (double price : farmerPrices) {
            total += price;
        }

        return total / farmerPrices.size();
    }
}