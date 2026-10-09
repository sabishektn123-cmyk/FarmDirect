package farmdirect.service;

import java.util.ArrayList;
import java.util.List;

import farmdirect.model.BulkListing;

public class BulkListingStore {

    private static final List<BulkListing> listings =
            new ArrayList<>();

    private BulkListingStore() {
    }

    public static void addListing(
            BulkListing listing) {

        listings.add(listing);
    }

    public static List<BulkListing> getListings() {

        return listings;
    }
}