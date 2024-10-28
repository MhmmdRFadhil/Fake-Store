package com.ryz.fakestore.ui.cart

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ryz.fakestore.R
import com.ryz.fakestore.databinding.FragmentCartBinding
import com.ryz.fakestore.utils.BaseFragment
import com.ryz.fakestore.utils.collectUiState
import com.ryz.fakestore.utils.initToolbar
import com.ryz.fakestore.utils.showMessage
import com.ryz.fakestore.utils.toProductResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {
    private val viewModel by viewModels<CartViewModel>()
    private lateinit var cartAdapter: CartAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initListener()
        initAdapter()
        initCollector()
    }

    private fun initUI() {
        initToolbar(getString(R.string.cart), binding.toolbarLayout.toolbar)

        viewModel.getCartProducts()
    }

    private fun initListener() {
        binding.tvCheckout.setOnClickListener {
            findNavController().navigate(CartFragmentDirections.actionCartFragmentToCheckOutFragment())
        }
    }

    private fun initAdapter() = with(binding.rvCart) {
        cartAdapter = CartAdapter(
            onClick = { data ->
                findNavController().navigate(
                    CartFragmentDirections.actionCartFragmentToHomeDetailFragment(
                        data.toProductResponse()
                    )
                )
            }, onDelete = { id ->
                viewModel.deleteItem(id)
            }, onIncrement = { data ->
                viewModel.updateItem(data.copy(quantity = data.quantity + 1))
            }, onDecrement = { data ->
                if (data.quantity == 1) {
                    viewModel.deleteItem(data.id)
                } else {
                    viewModel.updateItem(data.copy(quantity = data.quantity - 1))
                }
            })
        adapter = cartAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initCollector() = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                viewModel.allProduct.collectUiState(
                    fragment = this@CartFragment,
                    progressBar = binding.progressBar,
                    onSuccess = { data ->
                        binding.emptyLayout.root.isVisible = data.isEmpty()
                        binding.tvCheckout.isVisible = data.isNotEmpty()
                        cartAdapter.submitList(data)
                    }
                )
            }

            launch {
                viewModel.updateItem.collectUiState(
                    fragment = this@CartFragment,
                    progressBar = binding.progressBar,
                    onSuccess = {
                        showMessage(getString(R.string.item_updated_successfully))
                    }
                )
            }

            launch {
                viewModel.deleteItem.collectUiState(
                    fragment = this@CartFragment,
                    progressBar = binding.progressBar,
                    onSuccess = {
                        showMessage(getString(R.string.item_deleted_successfully))
                    }
                )
            }
        }
    }
}