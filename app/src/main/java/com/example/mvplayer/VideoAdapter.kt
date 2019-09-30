package com.example.mvplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class VideoAdapter(val videoList: ArrayList<VideoModel>, val listener: RecyclerViewClickListener) :
    RecyclerView.Adapter<VideoAdapter.VideoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.video_view,parent, false)
        return VideoHolder(view)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        if (videoList[position].videoThumbnail !=null){
            Glide.with(holder.imageThumbnail)
                .load(videoList[position].videoThumbnail)
                .centerCrop()
                .into(holder.imageThumbnail)
        }
        holder.tvVideoName.text = videoList[position].videoName!!.replace("/storage/8427-1f1d/","",ignoreCase = true)
        holder.cardView.setOnClickListener {
            listener.onClick(EnumClicks.CELL,position)
        }
    }

    inner class VideoHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val cardView:CardView = itemView.findViewById(R.id.cardView)
        val tvVideoName:TextView = itemView.findViewById(R.id.tvVideoName)
        val imageThumbnail:ImageView = itemView.findViewById(R.id.imageThumbnail)
    }
}
