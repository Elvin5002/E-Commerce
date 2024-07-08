package com.elvin.e_commerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elvin.e_commerce.databinding.ViewpagerImageItemBinding

class ViewPagerImages: RecyclerView.Adapter<ViewPagerImages.ViewPagerImagesViewHolder>() {
    inner class ViewPagerImagesViewHolder(val binding: ViewpagerImageItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(image: String){
            binding.apply {
                Glide.with(itemView).load(image).into(imageProductDetails)
            }
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerImagesViewHolder {
        return ViewPagerImagesViewHolder(ViewpagerImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewPagerImagesViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
    }
}