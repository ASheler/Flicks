package com.glaserproject.flicks.RvAdapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glaserproject.flicks.Movie.Movie;
import com.glaserproject.flicks.R;

/**
 * Tile Adapter for Main RecyclerView
 */

public class TileAdapter extends  RecyclerView.Adapter<TileAdapter.TileViewHolder>{

    Movie[] mMovies;

    public TileAdapter (Movie[] movies){
        mMovies = movies;
    }

    public TileAdapter (){

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

    public class TileViewHolder extends RecyclerView.ViewHolder{
        TextView text1;

        //initialize IDs in ViewHolder
        public TileViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
        }

        //Bind data - set content
        void bind (int index){
            text1.setText(mMovies[index].getMovieTitle());
        }

    }

    //set whole new set of data
    public void setMovieData (Movie[] movies){
        mMovies = movies;
        notifyDataSetChanged();
    }
}
