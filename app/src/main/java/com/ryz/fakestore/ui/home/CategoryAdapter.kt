package com.ryz.fakestore.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ryz.fakestore.R
import com.ryz.fakestore.databinding.ItemCategoryBinding
import com.ryz.fakestore.utils.toCapitalizeWords

class CategoryAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<String, CategoryAdapter.CategoryViewHolder>(DIFF_CALLBACK) {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: String) = with(binding) {
            tvCategory.text = category.toCapitalizeWords()

            val bgColor =
                if (adapterPosition == selectedPosition) R.color.md_theme_primaryContainer else R.color.white
            root.setCardBackgroundColor(ContextCompat.getColor(root.context, bgColor))

            root.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = if (adapterPosition == selectedPosition) {
                    RecyclerView.NO_POSITION
                } else {
                    adapterPosition
                }
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

                onClick.invoke(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }
}