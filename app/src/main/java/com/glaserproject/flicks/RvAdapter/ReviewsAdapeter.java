package com.glaserproject.flicks.RvAdapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glaserproject.flicks.MyObjects.Review;
import com.glaserproject.flicks.R;

/**
 * Adapter for Reviews RV
 */

public class ReviewsAdapeter extends RecyclerView.Adapter<ReviewsAdapeter.ReviewsViewHolder> {

    private Review[] mReviews;

    //initialize empty
    public ReviewsAdapeter() {
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.reviews_tile, parent, false);
        return new ReviewsViewHolder(view);

    }

    //set whole new set of data
    public void setReviewsData(Review[] reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) {
            return 0;
        }
        return mReviews.length;
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {

        TextView reviewName;
        TextView reviewBody;

        //initialize IDs in ViewHolder
        public ReviewsViewHolder(View itemView) {
            super(itemView);

            reviewName = itemView.findViewById(R.id.review_name_tv);
            reviewBody = itemView.findViewById(R.id.review_body_tv);
        }

        //Bind data - set content
        void bind(int index) {
            reviewName.setText(mReviews[index].getName());
            reviewBody.setText(mReviews[index].getBody());
        }

    }


}
