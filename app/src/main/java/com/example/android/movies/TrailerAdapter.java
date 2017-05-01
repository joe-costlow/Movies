package com.example.android.movies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Joseph Costlow on 20-Mar-17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    ArrayList<Trailer> mTrailerList;
    Context mContext;

    public TrailerAdapter(Context context, ArrayList<Trailer> trailerList) {
        this.mContext = context;
        this.mTrailerList = trailerList;
    }

    public void swap(ArrayList<Trailer> data) {
        mTrailerList.clear();
        mTrailerList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context;
        context = viewGroup.getContext();
        int layoutId = R.layout.trailer_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId, viewGroup, shouldAttachToParentImmediately);
        TrailerAdapterViewHolder holder = new TrailerAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {

        Picasso.with(mContext).load(mTrailerList.get(position).mThumbUrl).into(holder.mThumb);
    }

    @Override
    public int getItemCount() {

        if (mTrailerList.isEmpty()) return 0;

        return mTrailerList.size();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView mThumb;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);

            mThumb = (ImageView) itemView.findViewById(R.id.iv_trailer_thumb);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    int adapterPosition = getAdapterPosition();
                    Uri trailerUrl = Uri.parse(mTrailerList.get(adapterPosition).mTrailerUrl);

                    Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
                    trailerIntent.setData(trailerUrl);

                    mContext.startActivity(trailerIntent);
                }
            });
        }
    }
}
