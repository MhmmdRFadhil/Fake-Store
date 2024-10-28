package com.ryz.fakestore.ui.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ryz.fakestore.R
import com.ryz.fakestore.data.local.model.CartProductEntity
import com.ryz.fakestore.databinding.FragmentCheckOutBinding
import com.ryz.fakestore.utils.BaseFragment
import com.ryz.fakestore.utils.collectUiState
import com.ryz.fakestore.utils.initToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckOutFragment : BaseFragment<FragmentCheckOutBinding>(FragmentCheckOutBinding::inflate) {

    private lateinit var checkOutAdapter: CheckOutAdapter
    private val viewModel by viewModels<CartViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initListener()
        initAdapter()
        initCollector()
    }

    private fun initUi() {
        initToolbar(getString(R.string.check_out), binding.toolbarLayout.toolbar)

        viewModel.getCartProducts()
    }

    private fun initListener() {
        binding.tvOrder.setOnClickListener {
            viewModel.deleteAll()
        }
    }

    private fun initAdapter() = with(binding.rvCart) {
        checkOutAdapter = CheckOutAdapter()
        adapter = checkOutAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initCollector() = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                viewModel.allProduct.collectUiState(
                    fragment = this@CheckOutFragment,
                    progressBar = binding.progressBar,
                    onSuccess = { data ->
                        calculatePayment(data)
                        checkOutAdapter.submitList(data)
                    }
                )
            }

            launch {
                viewModel.deleteAll.collectUiState(
                    fragment = this@CheckOutFragment,
                    progressBar = binding.progressBar,
                    onSuccess = {
                        findNavController().navigate(CheckOutFragmentDirections.actionCheckOutFragmentToSuccessOrderFragment())
                    }
                )
            }
        }
    }

    private fun calculatePayment(it: List<CartProductEntity>) = with(binding) {
        val subTotal = it.sumOf { product -> product.price * product.quantity }
        val fee = 2500
        val total = subTotal.plus(fee)
        tvSubtotal.text = getString(R.string.format_price, subTotal.toString())
        tvFee.text = getString(R.string.format_price, fee.toString())
        tvTotal.text = getString(R.string.format_price, total.toString())
    }
}