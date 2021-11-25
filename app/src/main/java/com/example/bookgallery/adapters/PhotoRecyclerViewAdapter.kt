package com.example.bookgallery.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.bookgallery.R
import com.example.bookgallery.datamodels.Photo
import com.squareup.picasso.Picasso


class PhotoRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<PhotoRecyclerViewAdapter.PhotoViewHolder>() {
    val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }


    }

    private val differ = AsyncListDiffer(this, DIFF_CALL_BACK)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoRecyclerViewAdapter.PhotoViewHolder {
        return PhotoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.itemView.clipToOutline = true

            Glide.with(context)
                .load(item.urlS)
                .into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Photo>) {
        differ.submitList(list)
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)


    }
}