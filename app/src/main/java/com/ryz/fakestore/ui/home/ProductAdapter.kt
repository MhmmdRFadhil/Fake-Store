package com.ryz.fakestore.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ryz.fakestore.R
import com.ryz.fakestore.data.model.response.ProductResponse
import com.ryz.fakestore.databinding.ItemProductBinding
import com.ryz.fakestore.utils.ImageType
import com.ryz.fakestore.utils.loadImageUrl

class ProductAdapter(private val onClick: (Int?) -> Unit) :
    ListAdapter<ProductResponse, ProductAdapter.ProductViewHolder>(DIFF_CALLBACK) {
    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductResponse) = with(binding) {
            ivProduct.loadImageUrl(data.image)
            tvProductName.text = data.title
            tvRating.text = data.rating?.rate.toString()
            tvCountRating.text = root.context.getString(R.string.count_rating, data.rating?.count.toString())
            tvPrice.text = root.context.getString(R.string.format_price, data.price.toString())

            root.setOnClickListener {
                onClick.invoke(data.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductResponse>() {
            override fun areItemsTheSame(
                oldItem: ProductResponse,
                newItem: ProductResponse
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ProductResponse,
                newItem: ProductResponse
            ): Boolean = oldItem == newItem
        }
    }
}