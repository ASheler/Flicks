package com.glaserproject.flicks.RvAdapter;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glaserproject.flicks.Movie.Movie;
import com.glaserproject.flicks.R;
import com.squareup.picasso.Picasso;

/**
 * Tile Adapter for Main RecyclerView
 */

public class TileAdapter extends  RecyclerView.Adapter<TileAdapter.TileViewHolder>{

    Movie[] mMovies;

    public TileAdapter (Movie[] movies, TileAdapterOnClickHandler onClickHandler){
        mMovies = movies;
        mClickHandler = onClickHandler;
    }

    public TileAdapter (){
        mClickHandler = null;
    }


    private final TileAdapterOnClickHandler mClickHandler;

    public interface TileAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public TileAdapter (TileAdapterOnClickHandler onClickHandler){
        mClickHandler = onClickHandler;
    }



    @Override
    public TileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //set and inflate layout for ViewHolder
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_tile, parent, false);
        return new TileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TileViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null){
            return 0;
        }
        return mMovies.length;
    }

    public class TileViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
        TextView text1;
        ImageView backgroundImage;
        View bckgView;

        //initialize IDs in ViewHolder
        public TileViewHolder(View itemView) {
            super(itemView);

            bckgView = itemView.findViewById(R.id.view);

            text1 = itemView.findViewById(R.id.text1);
            backgroundImage = itemView.findViewById(R.id.backgroundImage);
            itemView.setOnClickListener(this);
        }

        //Bind data - set content
        void bind (int index){
            //text1.setLetterSpacing(0.2f);
            Picasso.with(itemView.getContext())
                    .load("http://image.tmdb.org/t/p/w500/" + mMovies[index].getPosterPath())
                    .into(backgroundImage);
            text1.setText(mMovies[index].getMovieTitle());
        }
        
        @Override
        public void onClick (View v){
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mMovies[adapterPosition]);
        }

    }

    //set whole new set of data
    public void setMovieData (Movie[] movies){
        mMovies = movies;
        notifyDataSetChanged();
    }
}
