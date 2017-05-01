package com.example.android.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Joseph Costlow on 19-Mar-17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private ArrayList<Review> mReviewList;

    public ReviewAdapter(Context context, ArrayList<Review> reviewList) {
        this.mReviewList = reviewList;
    }

    public void swap(ArrayList<Review> reviews) {
        mReviewList.clear();
        mReviewList.addAll(reviews);
        notifyDataSetChanged();
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutId;
        layoutId = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId, viewGroup, shouldAttachToParentImmediately);
        ReviewAdapterViewHolder holder = new ReviewAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {

        String currentAuthor = mReviewList.get(position).getAuthor();
        String currentContent = mReviewList.get(position).getReview();

        holder.mAuthor.setText(currentAuthor);
        holder.mContent.setText(currentContent);

    }

    @Override
    public int getItemCount() {

        if (mReviewList.isEmpty()) return 0;

        return mReviewList.size();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView mAuthor;
        TextView mContent;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);

            mAuthor = (TextView) itemView.findViewById(R.id.tv_review_author);
            mContent = (TextView) itemView.findViewById(R.id.tv_review_text);
        }
    }
}
