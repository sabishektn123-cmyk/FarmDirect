package farmdirect.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    // Write data to file
    public static void writeToFile(
            String fileName,
            String content) {

        try (FileWriter writer =
                     new FileWriter(fileName)) {

            writer.write(content);

            System.out.println(
                    "Data successfully written to "
                            + fileName
            );

        } catch (IOException e) {

            System.out.println(
                    "File writing error: "
                            + e.getMessage()
            );
        }
    }

    // Read data from file
    public static void readFromFile(
            String fileName) {

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(fileName))) {

            String line;

            System.out.println(
                    "\n--- FILE CONTENT ---"
            );

            while ((line = reader.readLine())
                    != null) {

                System.out.println(line);
            }

        } catch (IOException e) {

            System.out.println(
                    "File reading error: "
                            + e.getMessage()
            );
        }
    }
}