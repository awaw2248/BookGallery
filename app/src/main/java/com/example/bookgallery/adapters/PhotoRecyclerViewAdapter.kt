package com.example.bookgallery.adapters
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class PhotoRecyclerViewAdapter(private val list: List<Photo>) :
  RecyclerView.Adapter<PhotoRecyclerViewAdapter.PhotoViewHolder>(){
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoRecyclerViewAdapter.PhotoViewHolder {
      return PhotoViewHolder(
          LayoutInflater.from(parent.context).inflate(
              R.layout.item_layout,
              parent,
              false
          )
      )
  }
  override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
  val item = list[position]
            TODO("bind view with data")
  }
  override fun getItemCount(): Int {
      return list.size
  }
  class PhotoViewHolder(itemView: View)
  : RecyclerView.ViewHolder(itemView){
  }
}