package farmdirect.service;

import farmdirect.exception.InvalidBidException;
import farmdirect.model.Bid;
import farmdirect.model.BulkListing;

public class BiddingService {

    // synchronized prevents two threads from
    // changing the bidding system at the same time
    public synchronized void placeBid(
            BulkListing listing,
            Bid bid) throws InvalidBidException {

        if (bid.getBidPricePerKg()
                < listing.getMinimumPrice()) {

            throw new InvalidBidException(
                    "Bid must be equal to or greater than "
                    + "the minimum price of ₹"
                    + listing.getMinimumPrice()
            );
        }

        System.out.println(
                Thread.currentThread().getName()
                        + " is processing bid..."
        );

        listing.addBid(bid);

        System.out.println(
                "Bid placed successfully: ₹"
                        + bid.getBidPricePerKg()
                        + "/kg"
        );
    }
}