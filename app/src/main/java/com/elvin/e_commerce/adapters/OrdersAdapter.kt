package com.elvin.e_commerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.elvin.e_commerce.R
import com.elvin.e_commerce.data.order.Order
import com.elvin.e_commerce.data.order.OrderStatus
import com.elvin.e_commerce.data.order.getOrderStatus
import com.elvin.e_commerce.databinding.OrderItemBinding

class OrdersAdapter: RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    inner class OrdersViewHolder(val binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(order: Order?) {
            binding.apply {
                tvOrderId.text = order?.orderId.toString()
                tvOrderDate.text = order?.date
                val resources = itemView.resources
                val colorDrawable = when (getOrderStatus(order?.orderStatus!!)) {
                    is OrderStatus.Ordered -> {
                        resources.getColor(R.color.g_orange)
                    }
                    is OrderStatus.Confirmed -> {
                        resources.getColor(R.color.g_green)
                    }
                    is OrderStatus.Delivered -> {
                        resources.getColor(R.color.g_green)
                    }
                    is OrderStatus.Shipped -> {
                        resources.getColor(R.color.g_green)
                    }
                    is OrderStatus.Canceled -> {
                        resources.getColor(R.color.g_red)
                    }
                    else -> {
                        resources.getColor(R.color.g_red)
                    }

                }
                imageOrderState.drawable.setTint(colorDrawable)

            }

        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.products == newItem.products
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder(OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val order = differ.currentList[position]
        holder.bind(order)
    }

    val onClick: ((Order) -> Unit)? = null


}