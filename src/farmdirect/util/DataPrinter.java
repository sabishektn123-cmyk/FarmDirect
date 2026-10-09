package farmdirect.util;

import java.util.List;

public class DataPrinter {

    public static <T> void printList(
            List<T> data) {

        for (T item : data) {
            System.out.println(item);
        }
    }
}