package farmdirect.service;

import java.util.ArrayList;
import java.util.List;

import farmdirect.dao.ProductDAO;
import farmdirect.model.Product;

public class ProductStore {

    private static final List<Product> products =
            new ArrayList<>();

    private ProductStore() {
    }

    // =========================================
    // ADD PRODUCT
    // =========================================

    public static void addProduct(Product product) {

        products.add(product);
    }

    // =========================================
    // GET PRODUCTS
    // =========================================

    public static List<Product> getProducts() {

        return products;
    }

    // =========================================
    // LOAD PRODUCTS FROM DATABASE
    // =========================================

    public static void loadFromDatabase() {

        ProductDAO productDAO =
                new ProductDAO();

        List<Product> databaseProducts =
                productDAO.getAllProducts();

        products.clear();

        products.addAll(databaseProducts);

        System.out.println(
                "Products loaded from database: "
                + products.size()
        );
    }

    // =========================================
    // CLEAR PRODUCTS
    // =========================================

    public static void clearProducts() {

        products.clear();
    }
}