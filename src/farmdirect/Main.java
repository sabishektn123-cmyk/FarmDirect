package farmdirect;

import javax.swing.SwingUtilities;

import farmdirect.database.DatabaseInitializer;
import farmdirect.service.ProductStore;
import farmdirect.ui.LoginFrame;

public class Main {

    public static void main(String[] args) {

        System.out.println(
                "================================="
        );

        System.out.println(
                "Starting FarmDirect..."
        );

        System.out.println(
                "================================="
        );


        // =================================================
        // INITIALIZE DATABASE
        // =================================================

        DatabaseInitializer.initialize();


        System.out.println(
                "FarmDirect database is ready."
        );


        // =================================================
        // LOAD PRODUCTS
        // =================================================

        ProductStore.loadFromDatabase();


        // =================================================
        // START GUI
        // =================================================

        SwingUtilities.invokeLater(() -> {

            LoginFrame loginFrame =
                    new LoginFrame();

            loginFrame.setVisible(true);

        });
    }
}