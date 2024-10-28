package com.ryz.fakestore.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ryz.fakestore.R
import com.ryz.fakestore.data.local.model.CartProductEntity
import com.ryz.fakestore.databinding.ItemCartBinding
import com.ryz.fakestore.utils.loadImageUrl

class CartAdapter(
    private val onClick: (CartProductEntity) -> Unit,
    private val onDelete: (Int) -> Unit,
    private val onIncrement: (CartProductEntity) -> Unit,
    private val onDecrement: (CartProductEntity) -> Unit
) : ListAdapter<CartProductEntity, CartAdapter.CartViewHolder>(DIFF_CALLBACK) {
    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CartProductEntity) = with(binding) {
            ivProduct.loadImageUrl(data.image)
            tvTitle.text = data.title
            tvPrice.text = root.context.getString(R.string.format_price, data.price.toString())
            tvQuantity.text = data.quantity.toString()

            clDelete.setOnClickListener { onDelete.invoke(data.id) }
            ivIncrement.setOnClickListener { onIncrement.invoke(data) }
            ivDecrement.setOnClickListener { onDecrement.invoke(data) }
            root.setOnClickListener { onClick.invoke(data) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            ItemCartBinding.inflate(
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