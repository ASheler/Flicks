package com.glaserproject.flicks.RvAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glaserproject.flicks.MyObjects.Trailer;
import com.glaserproject.flicks.R;

/**
 * Created by ondra on 3/18/2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private final TrailersAdapterOnClickHandler mClickHandler;
    private Trailer[] mTrailers;

    //initialize TileAdapter w/ click handler
    public TrailersAdapter(TrailersAdapterOnClickHandler onClickHandler) {
        mClickHandler = onClickHandler;
    }

    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.trailers_tile, parent, false);
        return new TrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {
        holder.bind(position);
    }

    //set whole new set of data
    public void setTrailersData(Trailer[] trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mTrailers == null) {
            return 0;
        }
        return mTrailers.length;
    }

    //click Interface
    public interface TrailersAdapterOnClickHandler {
        void onTrailerClick(String videoKey);

        void onTrailerLongClick(String videoName, String videoKey);
    }

    public class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView trailerName;
        View lowerLine;

        //initialize IDs in ViewHolder
        public TrailersViewHolder(View itemView) {
            super(itemView);

            trailerName = itemView.findViewById(R.id.trailer_name);
            lowerLine = itemView.findViewById(R.id.trailer_separator);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        //Bind data - set content
        void bind(int index) {
            trailerName.setText(mTrailers[index].getName());
            //hide last line in view
            if (index == getItemCount() - 1) {
                lowerLine.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onTrailerClick(mTrailers[adapterPosition].getKey());
        }

        @Override
        public boolean onLongClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onTrailerLongClick(mTrailers[adapterPosition].getName(), mTrailers[adapterPosition].getKey());
            return true;
        }
    }
}
