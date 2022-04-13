package com.profabanoub.unsplashapi.ui.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.profabanoub.unsplashapi.R
import com.profabanoub.unsplashapi.data.remote.model.PhotoModel
import com.profabanoub.unsplashapi.databinding.ItemPhotoBinding

class UnsplashPhotoAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<PhotoModel, UnsplashPhotoAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentPhoto = getItem(position)

        if (currentPhoto != null) {
            holder.bind(currentPhoto)
        }

    }


    inner class PhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(photo: PhotoModel) {
            binding.apply {
                Glide.with(itemView)
                    .load(photo.urls.regular)
                    .error(R.drawable.ic_error)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemPhotoImage)

                itemPhotoUsername.text = photo.user.username
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(photo: PhotoModel)
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<PhotoModel>() {
            override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean =
                oldItem == newItem
        }
    }
}