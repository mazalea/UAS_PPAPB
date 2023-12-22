package com.example.uas_ppapb

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.uas_ppapb.database.MovieRoom

class MovieAdapterUser (private val movieList: ArrayList<MovieRoom>) :
    RecyclerView.Adapter<MovieAdapterUser.MovieViewHolder>()
{
    class MovieViewHolder(movieList: View) : RecyclerView.ViewHolder(movieList){
        val imgPoster: ImageView = itemView.findViewById(R.id.imageView)
        val txtTitle: TextView = itemView.findViewById(R.id.edt_title)
        val txtDirector: TextView = itemView.findViewById(R.id.edt_director)
        val txtTime: TextView = itemView.findViewById(R.id.edt_time)
        val txtRate: TextView = itemView.findViewById(R.id.edt_rate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        val viewHolder = MovieViewHolder(view)

        return viewHolder
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]

        holder.txtTitle.text = movie.title
        holder.txtDirector.text = movie.director
        holder.txtTime.text = movie.time
        holder.txtRate.text = movie.rate

        // Load and display the image using Glide
        Glide.with(holder.itemView.context)
            .load(movie.imageUrl)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(holder.imgPoster)

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailMovieActivity::class.java)
            intent.putExtra("title",movie.title)
            intent.putExtra("director",movie.director)
            intent.putExtra("time",movie.time)
            intent.putExtra("rate",movie.rate)
            intent.putExtra("imgId", movie.imageUrl)
            intent.putExtra("synopsis",movie.synopsis)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}