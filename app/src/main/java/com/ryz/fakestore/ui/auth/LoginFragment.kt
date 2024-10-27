package com.ryz.fakestore.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ryz.fakestore.R
import com.ryz.fakestore.data.model.request.LoginRequest
import com.ryz.fakestore.databinding.FragmentLoginBinding
import com.ryz.fakestore.utils.BaseFragment
import com.ryz.fakestore.utils.areAllTextInputLayoutValid
import com.ryz.fakestore.utils.collectUiState
import com.ryz.fakestore.utils.doOnTextChangeListener
import com.ryz.fakestore.utils.hideSoftKeyboard
import com.ryz.fakestore.utils.showMessage
import com.ryz.fakestore.utils.validateNotNullOrEmpty
import com.ryz.fakestore.utils.validateTextShouldGreaterThan
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initListener()
        initCollector()
    }

    private fun initUI() = with(binding) {
        edtUsername.doOnTextChangeListener {
            tilUsername.validateNotNullOrEmpty()
        }

        edtPassword.doOnTextChangeListener {
            tilPassword.validateTextShouldGreaterThan(6)
        }
    }

    private fun initListener() = with(binding) {
        tvLogin.setOnClickListener {
            validateInput()

            if (!isRequired()) {
                showMessage(getString(R.string.required_field))
                return@setOnClickListener
            }

            // request data
            val username = edtUsername.text.toString()
            val password = edtPassword.text.toString()

            val loginRequest = LoginRequest(
                username = username,
                password = password
            )
            viewModel.login(request = loginRequest)
            hideSoftKeyboard()
        }
    }

    private fun initCollector() = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.loginState.collectUiState(
                fragment = this@LoginFragment,
                progressBar = binding.progressBar,
                onSuccess = {
                    viewModel.getUser()
                    showMessage(getString(R.string.login_success_message))
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                })
        }
    }

    private fun validateInput() = with(binding) {
        tilUsername.validateNotNullOrEmpty()
        tilPassword.validateTextShouldGreaterThan(6)
    }

    private fun isRequired(): Boolean =
        areAllTextInputLayoutValid(binding.tilUsername, binding.tilPassword)
}