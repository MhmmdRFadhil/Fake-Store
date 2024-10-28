package com.ryz.fakestore.ui.home.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.ryz.fakestore.R
import com.ryz.fakestore.data.model.response.ProductResponse
import com.ryz.fakestore.databinding.FragmentHomeDetailBinding
import com.ryz.fakestore.ui.cart.CartViewModel
import com.ryz.fakestore.ui.home.HomeViewModel
import com.ryz.fakestore.utils.BaseFragment
import com.ryz.fakestore.utils.collectUiState
import com.ryz.fakestore.utils.initToolbar
import com.ryz.fakestore.utils.loadImageUrl
import com.ryz.fakestore.utils.showMessage
import com.ryz.fakestore.utils.toCapitalizeWords
import com.ryz.fakestore.utils.toCartProductEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeDetailFragment :
    BaseFragment<FragmentHomeDetailBinding>(FragmentHomeDetailBinding::inflate) {
    private val viewModel by viewModels<HomeViewModel>()
    private val cartViewModel by viewModels<CartViewModel>()
    private val args by navArgs<HomeDetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initListener()
        initCollector()
    }

    private fun initUI() {
        initToolbar(getString(R.string.details), binding.toolbarLayout.toolbar)

        args.data.id?.let { viewModel.getProductDetail(it) }
    }

    private fun initListener() {
        binding.tvAddToCart.setOnClickListener {
            val requestData = args.data.toCartProductEntity(localDataSource.getUserId())
            cartViewModel.insertItem(requestData)
        }
    }

    private fun initCollector() = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                viewModel.productDetail.collectUiState(
                    fragment = this@HomeDetailFragment,
                    progressBar = binding.progressBar,
                    onLoading = { isLoading ->
                        binding.clContent.isVisible = !isLoading
                    },
                    onSuccess = { data ->
                        populateUI(data)
                    }
                )
            }

            launch {
                cartViewModel.insertItem.collectUiState(
                    fragment = this@HomeDetailFragment,
                    progressBar = binding.progressBar,
                    onLoading = { isLoading ->
                        binding.tvAddToCart.isVisible = !isLoading
                    },
                    onSuccess = {
                        showMessage(getString(R.string.item_added_successfully))
                    })
            }
        }
    }

    private fun populateUI(data: ProductResponse) = with(binding) {
        ivProduct.loadImageUrl(data.image)
        tvProductName.text = data.title
        tvCategory.text = data.category?.toCapitalizeWords()
        tvRating.text = data.rating?.rate.toString()
        tvCountRating.text = getString(R.string.count_rating, data.rating?.count.toString())
        tvDescription.text = data.description
        tvPrice.text = getString(R.string.format_price, data.price.toString())
    }
}