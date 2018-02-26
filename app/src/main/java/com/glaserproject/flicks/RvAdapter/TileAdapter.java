package com.glaserproject.flicks.RvAdapter;

import android.service.quicksettings.Tile;
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

        public TileViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
        }
        void bind (int index){
            text1.setText(mMovies[index].getMovieTitle());
        }

    }

    public void setMovieData (Movie[] movies){
        mMovies = movies;
        notifyDataSetChanged();
    }
}

/*

public class TileAdapter extends RecyclerView.Adapter<TileAdapter.TileViewHolder> {

    private Movie[] mMovie;
    private String stringR;

    private TileAdapterClickHandler mClickHandler;


    public TileAdapter (Movie[] movie){
        mMovie = movie;
    }

    public interface TileAdapterClickHandler {
        void onClick(String movieTitle);
    }

    public TileAdapter (String string){
        stringR = string;
    }

    public TileAdapter(TileAdapterClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public class TileViewHolder extends RecyclerView.ViewHolder{
        //public final ImageView img1;
        TextView text1;

        public TileViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
            //img1 = itemView.findViewById(R.id.imageView);
        }
        void bind (int index){
            text1.setText("hovno" + index);
        }

    }


    @Override
    public TileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_tile, parent, false);
        return new TileViewHolder(view);

    }

    @Override
    public void onBindViewHolder(TileViewHolder holder, int position) {
        holder.text1.setText("hovnoooo");
        //Picasso.with(holder.itemView.getContext()).load("http://i.imgur.com/DvpvklR.png").into(holder.img1);
    }

    @Override
    public int getItemCount() {
        if (mMovie == null){
            return 0;
        }
        return mMovie.length;
    }

    public void setMovieData (Movie[] movie){
        mMovie = movie;
        notifyDataSetChanged();
    }
}
*/

    /*
    public TileAdapter(){
    }

    @Override
    public void onBindViewHolder(TileViewHolder holder, int position) {
        ImageView image1;
        image1 = holder.itemView.findViewById(R.id.imageView);
        Picasso.with(holder.itemView.getContext()).load("http://i.imgur.com/DvpvklR.png").into(image1);
        //holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mMovie.length;
    }


    class TileViewHolder extends RecyclerView.ViewHolder {
        ImageView image1;

        public TileViewHolder (View view) {
            super(view);
            image1 = view.findViewById(R.id.imageView);
        }
        void bind (int position){
            //Picasso.with(.getContext()).load("http://i.imgur.com/DvpvklR.png").into(image1);
        }
    }


    public void setMovieData(Movie[] movie){
        mMovie = movie;
        notifyDataSetChanged();
    }

}
    */



