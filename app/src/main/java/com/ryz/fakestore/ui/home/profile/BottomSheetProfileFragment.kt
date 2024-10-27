package com.ryz.fakestore.ui.home.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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
    private val args by navArgs<BottomSheetProfileFragmentArgs>()

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                BottomSheetBehavior.from(sheet).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    peekHeight = bottomSheet.height
                    isFitToContents = false
                    halfExpandedRatio = 0.7f
                }
            }
        }

        return super.onCreateDialog(savedInstanceState)
    }

    private fun initUI() {
        viewModel.getUserById(args.userId)
    }

    private fun initCollector() = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.userData.collectUiState(
                fragment = this@BottomSheetProfileFragment,
                progressBar = binding.progressBar
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
}