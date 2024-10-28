package com.ryz.fakestore.ui.cart

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ryz.fakestore.databinding.FragmentSuccessOrderBinding
import com.ryz.fakestore.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuccessOrderFragment :
    BaseFragment<FragmentSuccessOrderBinding>(FragmentSuccessOrderBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvBackToHome.setOnClickListener {
            findNavController().navigate(SuccessOrderFragmentDirections.actionSuccessOrderFragmentToHomeFragment())
        }
    }
}