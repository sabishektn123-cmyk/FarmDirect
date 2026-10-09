package farmdirect.service;

import java.util.ArrayList;
import java.util.List;

import farmdirect.model.Feedback;

public class FeedbackStore {

    private static final List<Feedback> feedbacks =
            new ArrayList<>();

    private FeedbackStore() {
    }

    // =========================================
    // ADD FEEDBACK
    // =========================================

    public static void addFeedback(Feedback feedback) {

        feedbacks.add(feedback);
    }

    // =========================================
    // GET ALL FEEDBACK
    // =========================================

    public static List<Feedback> getFeedbacks() {

        return feedbacks;
    }

    // =========================================
    // CALCULATE AVERAGE FARMER RATING
    // =========================================

    public static double getAverageRating(int farmerId) {

        if (feedbacks.isEmpty()) {
            return 0.0;
        }

        double totalRating = 0;
        int count = 0;

        for (Feedback feedback : feedbacks) {

            if (feedback.getFarmer()
                    .getUserId() == farmerId) {

                totalRating += feedback.getRating();
                count++;
            }
        }

        if (count == 0) {
            return 0.0;
        }

        return totalRating / count;
    }
}