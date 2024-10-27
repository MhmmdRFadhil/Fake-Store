package com.ryz.fakestore.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ryz.fakestore.R
import com.ryz.fakestore.databinding.FragmentHomeBinding
import com.ryz.fakestore.utils.BaseFragment
import com.ryz.fakestore.utils.collectUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private var currentCategory: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initListener()
        initAdapter()
        initCollector()
    }

    private fun initUI() {
        viewModel.getAllCategory()
        viewModel.getProduct()
    }

    private fun initListener() = with(binding) {
        swipeLayout.setOnRefreshListener {
            currentCategory?.let { category ->
                viewModel.getProductByCategory(category)
            } ?: run {
                viewModel.getProduct()
            }
            swipeLayout.isRefreshing = false
        }
    }

    private fun initCollector() = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                viewModel.username.collect { data ->
                    val username = data?.let { username ->
                        getString(R.string.greeting, username)
                    } ?: run {
                        getString(R.string.default_greeting)
                    }

                    binding.tvUsername.text = username
                }
            }

            launch {
                viewModel.category.collectUiState(
                    fragment = this@HomeFragment,
                    progressBar = binding.progressBar,
                ) { data ->
                    categoryAdapter.submitList(data)
                }
            }

            launch {
                viewModel.product.collectUiState(
                    fragment = this@HomeFragment,
                    progressBar = binding.progressBar
                ) { data ->
                    productAdapter.submitList(data)
                }
            }
        }
    }

    private fun initAdapter() {
        with(binding.rvCategory) {
            categoryAdapter = CategoryAdapter { category ->
                if (currentCategory == category) {
                    currentCategory = null
                    viewModel.getProduct()
                } else {
                    currentCategory = category
                    viewModel.getProductByCategory(category)
                }
            }
            adapter = categoryAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        with(binding.rvProduct) {
            productAdapter = ProductAdapter { data ->
                data?.let { productId ->
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToHomeDetailFragment(
                            productId
                        )
                    )
                }
            }

            adapter = productAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    override fun onDestroyView() {
        binding.rvCategory.adapter = null
        binding.rvProduct.adapter = null
        super.onDestroyView()
    }
}