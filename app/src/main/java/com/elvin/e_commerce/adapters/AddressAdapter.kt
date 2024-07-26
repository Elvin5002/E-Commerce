package com.elvin.e_commerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.elvin.e_commerce.R
import com.elvin.e_commerce.data.Address
import com.elvin.e_commerce.databinding.AddressRvItemBinding

class AddressAdapter: RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    inner class AddressViewHolder(val binding : AddressRvItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(address: Address, isSelected: Boolean) {
            binding.apply {
                buttonAddress.text = address.addressTitle
                if(isSelected){
                    buttonAddress.setBackgroundColor(itemView.context.getColor(R.color.g_blue))
                }else{
                    buttonAddress.setBackgroundColor(itemView.context.getColor(R.color.g_white))
                }
            }
        }

    }

    private val diffUtil = object : DiffUtil.ItemCallback<Address>(){
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.addressTitle == newItem.addressTitle && oldItem.fullName == newItem.fullName
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(AddressRvItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    init {
        differ.addListListener { _, _ ->
            notifyItemChanged(selectedAddress)
        }
    }

    var selectedAddress = -1
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = differ.currentList[position]
        holder.bind(address, selectedAddress == position)

        holder.binding.buttonAddress.setOnClickListener {
            if(selectedAddress >= 0)
                notifyItemChanged(selectedAddress)
            selectedAddress = holder.adapterPosition
            notifyItemChanged(selectedAddress)
            onClick?.invoke(address)
        }
    }

    var onClick: ((Address) -> Unit)? = null
}