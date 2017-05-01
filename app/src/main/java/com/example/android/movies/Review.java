package com.example.android.movies;

/**
 * Created by Joseph Costlow on 19-Mar-17.
 */

public class Review {

    private static final String TAG = Review.class.getSimpleName();

    public String mAuthor;
    public String mReviewText;

    public Review(String author, String review) {
        this.mAuthor = author;
        this.mReviewText = review;
    }

    public String getAuthor() {
        return this.mAuthor;
    }

    public String getReview() {
        return this.mReviewText;
    }
}
