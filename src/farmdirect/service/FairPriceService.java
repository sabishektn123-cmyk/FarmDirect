package farmdirect.service;

import java.util.ArrayList;
import java.util.List;

import farmdirect.model.Product;

public class FairPriceService {

    private FairPriceService() {
        // Prevent object creation
    }

    public static double calculateFairPrice(String cropName) {

        List<Double> farmerPrices = new ArrayList<>();

        for (Product product : ProductStore.getProducts()) {

            if (product.getCrop()
                    .getCropName()
                    .equalsIgnoreCase(cropName)) {

                farmerPrices.add(
                        product.getPricePerKg()
                );
            }
        }

        AveragePriceCalculator calculator =
                new AveragePriceCalculator();

        return calculator.calculateFairPrice(
                farmerPrices
        );
    }
}