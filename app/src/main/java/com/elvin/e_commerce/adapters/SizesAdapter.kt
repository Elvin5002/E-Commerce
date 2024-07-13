package com.elvin.e_commerce.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.elvin.e_commerce.databinding.SizeRvItemBinding

class SizesAdapter: RecyclerView.Adapter<SizesAdapter.SizesViewHolder>() {
    private var selectedPosition = -1

    inner class SizesViewHolder(private val binding: SizeRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(size: String, position: Int){
            binding.tvSize.text = size

            if (position == selectedPosition){
                binding.imageShadow.visibility = View.VISIBLE
            }else{
                binding.imageShadow.visibility = View.INVISIBLE
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizesViewHolder {
        return SizesViewHolder(SizeRvItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SizesViewHolder, position: Int) {
        val size = differ.currentList[position]
        holder.bind(size, position)

        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0)
                notifyItemChanged(selectedPosition)

            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(size)
        }
    }

    var onItemClick : ((String) -> Unit)? = null

}