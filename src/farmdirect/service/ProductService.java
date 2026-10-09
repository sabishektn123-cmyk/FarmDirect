package farmdirect.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import farmdirect.model.Product;

public class ProductService {

    private List<Product> products;

    public ProductService() {
        products = new ArrayList<>();
    }

    // Add product
    public void addProduct(Product product) {
        products.add(product);
    }

    // Get all products
    public List<Product> getProducts() {
        return products;
    }

    // Lambda expression for sorting by price
    public void sortByPrice() {

        products.sort(
                (p1, p2) ->
                        Double.compare(
                                p1.getPricePerKg(),
                                p2.getPricePerKg()
                        )
        );
    }

    // Lambda expression for sorting by quantity
    public void sortByQuantity() {

        products.sort(
                (p1, p2) ->
                        Double.compare(
                                p1.getQuantity(),
                                p2.getQuantity()
                        )
        );
    }

    // Lambda expression for finding fresh products
    public List<Product> getFreshProducts() {

        List<Product> freshProducts =
                new ArrayList<>();

        products.forEach(product -> {

            if (product.getFreshness()
                    .toString()
                    .equals("FRESH")) {

                freshProducts.add(product);
            }
        });

        return freshProducts;
    }
}