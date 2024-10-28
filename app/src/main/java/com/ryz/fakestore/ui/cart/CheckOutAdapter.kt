package com.ryz.fakestore.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ryz.fakestore.R
import com.ryz.fakestore.data.local.model.CartProductEntity
import com.ryz.fakestore.databinding.ItemCheckoutBinding
import com.ryz.fakestore.utils.loadImageUrl

class CheckOutAdapter :
    ListAdapter<CartProductEntity, CheckOutAdapter.CartViewHolder>(DIFF_CALLBACK) {
    inner class CartViewHolder(private val binding: ItemCheckoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CartProductEntity) = with(binding) {
            ivProduct.loadImageUrl(data.image)
            tvTitle.text = data.title
            tvPrice.text = root.context.getString(R.string.format_price, data.price.toString())
            tvQuantity.text = root.context.getString(R.string.format_quantity, data.quantity.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            ItemCheckoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartProductEntity>() {
            override fun areItemsTheSame(
                oldItem: CartProductEntity,
                newItem: CartProductEntity
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CartProductEntity,
                newItem: CartProductEntity
            ): Boolean = oldItem == newItem
        }
    }
}