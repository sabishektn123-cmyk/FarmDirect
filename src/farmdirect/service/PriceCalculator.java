package farmdirect.service;

import java.util.List;

public interface PriceCalculator {

    double calculateFairPrice(List<Double> farmerPrices);
}