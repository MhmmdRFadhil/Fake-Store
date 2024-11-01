package com.ryz.fakestore.ui.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ryz.fakestore.data.model.response.UserResponse
import com.ryz.fakestore.databinding.FragmentBottomSheetProfileBinding
import com.ryz.fakestore.ui.auth.LoginViewModel
import com.ryz.fakestore.utils.collectUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BottomSheetProfileFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()
    private val userId by lazy { arguments?.getInt(USER_ID) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initCollector()
    }

    private fun initUI() {
        userId?.let { viewModel.getUserById(it) }
    }

    private fun initCollector() = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.userData.collectUiState(
                fragment = this@BottomSheetProfileFragment,
                progressBar = binding.progressBar,
                onLoading = { isLoading ->
                    binding.content.isVisible = !isLoading
                }
            ) { data ->
                populateData(data)
            }
        }
    }

    private fun populateData(data: UserResponse) {
        with(binding) {
            edtUsername.setText(data.username)
            edtFirstname.setText(data.name?.firstname)
            edtLastname.setText(data.name?.lastname)
            edtEmail.setText(data.email)
            edtPhone.setText(data.phone)
            edtAddress.setText(data.address?.street)
            edtCity.setText(data.address?.city)
            edtNumber.setText(data.address?.number.toString())
            edtZipCode.setText(data.address?.zipcode)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val USER_ID = "userId"

        fun newInstance(userId: Int) = BottomSheetProfileFragment().apply {
            arguments = Bundle().apply {
                putInt(USER_ID, userId)
            }
        }
    }
}