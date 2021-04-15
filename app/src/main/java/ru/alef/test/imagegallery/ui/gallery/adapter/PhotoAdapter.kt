package ru.alef.test.imagegallery.ui.gallery.adapter

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.alef.test.imagegallery.R
import ru.alef.test.imagegallery.common.inflater

class PhotoAdapter(
    val photoClickListener: OnPhotoClickListener
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private val asyncPhotoListDiffer = AsyncListDiffer<String>(this, PhotoDiffer)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = parent.inflater(R.layout.item_image)
        return PhotoViewHolder(view)
    }

    override fun getItemCount() = asyncPhotoListDiffer.currentList.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = asyncPhotoListDiffer.currentList[position]
        holder.bind(photo)
    }

    fun submitList(photos: List<String>) {
        asyncPhotoListDiffer.submitList(photos)
    }

    fun getItem(position: Int): String = asyncPhotoListDiffer.currentList[position]

    inner class PhotoViewHolder(val imageView: View) : RecyclerView.ViewHolder(imageView),
        View.OnClickListener {
        private val imageViewPhoto = this.imageView.findViewById<AppCompatImageView>(R.id.iv_photo)
        private var photo: String? = null

        fun bind(photo: String) {
            this.photo = photo
            imageViewPhoto.setOnClickListener(this)
            Glide.with(imageView.context)
                .load(photo)
                .centerCrop()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(imageViewPhoto)
        }

        override fun onClick(v: View) {
            photoClickListener.onPhotoClicked(adapterPosition, imageViewPhoto)
        }
    }

    object PhotoDiffer : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = false

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }

    interface OnPhotoClickListener {
        fun onPhotoClicked(position: Int, imageView: View)
    }
}