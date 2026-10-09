package farmdirect.service;

import farmdirect.exception.InvalidBidException;
import farmdirect.model.Bid;
import farmdirect.model.BulkListing;

public class BidTask implements Runnable {

    private BiddingService biddingService;
    private BulkListing listing;
    private Bid bid;

    public BidTask(
            BiddingService biddingService,
            BulkListing listing,
            Bid bid) {

        this.biddingService = biddingService;
        this.listing = listing;
        this.bid = bid;
    }

    @Override
    public void run() {

        try {

            biddingService.placeBid(
                    listing,
                    bid
            );

        } catch (InvalidBidException e) {

            System.out.println(
                    "Bid Error: "
                            + e.getMessage()
            );
        }
    }
}