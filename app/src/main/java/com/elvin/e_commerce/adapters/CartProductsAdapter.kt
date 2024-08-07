package com.elvin.e_commerce.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elvin.e_commerce.data.CartProduct
import com.elvin.e_commerce.data.Product
import com.elvin.e_commerce.databinding.CartProductItemBinding
import com.elvin.e_commerce.helper.getProductPrice

class CartProductsAdapter : RecyclerView.Adapter<CartProductsAdapter.CartProductsViewHolder>() {

    inner class CartProductsViewHolder(val binding: CartProductItemBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(cartProduct: CartProduct){
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images[0]).into(imageCartProduct)
                tvProductCartName.text = cartProduct.product.name
                tvCartProductQuantity.text = cartProduct.quantity.toString()

                val priceAfterOffer = cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
                tvProductCartPrice.text = String.format("$%.2f", priceAfterOffer)

                imageCartProductColor.setImageDrawable(ColorDrawable(cartProduct.selectedColor?: Color.TRANSPARENT))
                tvCartProductSize.text = cartProduct.selectedSize?: "".also { imageCartProductSize.setImageDrawable(ColorDrawable(Color.TRANSPARENT)) }

            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductsViewHolder {
        return CartProductsViewHolder(
            CartProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartProductsViewHolder, position: Int) {
        val cartProduct = differ.currentList[position]
        holder.bind(cartProduct)

        holder.itemView.setOnClickListener {
            onProductClick?.invoke(cartProduct)
            println("Clicked")
        }

        holder.binding.imagePlus.setOnClickListener {
            onPlusClick?.invoke(cartProduct)
        }

        holder.binding.imageMinus.setOnClickListener {
            onMinusClick?.invoke(cartProduct)
            println(cartProduct.quantity)
        }
    }

    var onProductClick: ((CartProduct) -> Unit)? = null
    var onPlusClick: ((CartProduct) -> Unit)? = null
    var onMinusClick: ((CartProduct) -> Unit)? = null

}