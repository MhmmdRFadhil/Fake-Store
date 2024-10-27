package com.ryz.fakestore.utils

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.RoundedCorner
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ryz.fakestore.R
import com.ryz.fakestore.data.remote.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

suspend inline fun <T> StateFlow<GenericUiState<T>>.collectUiState(
    fragment: Fragment,
    progressBar: View? = null,
    crossinline onLoading: (Boolean) -> Unit = {},
    crossinline onSuccess: (T) -> Unit
) {
    collect { state ->
        when (state) {
            is GenericUiState.Error -> {
                Snackbar.make(fragment.requireView(), state.message, Snackbar.LENGTH_LONG).show()
            }

            is GenericUiState.Loading -> {
                onLoading(state.isLoading)
                progressBar?.isVisible = state.isLoading
            }

            is GenericUiState.Success -> onSuccess(state.data)
        }
    }
}

fun <T> MutableStateFlow<GenericUiState<T>>.updateFromResource(
    resource: Resource<T>,
    onSuccess: ((T) -> Unit)? = null
) {
    when (resource) {
        is Resource.Error -> update {
            GenericUiState.Error(resource.error)
        }

        is Resource.Loading -> update {
            GenericUiState.Loading(resource.isLoading)
        }

        is Resource.Success -> {
            onSuccess?.invoke(resource.data) ?: update {
                GenericUiState.Success(resource.data)
            }
        }
    }
}

inline fun TextInputEditText.doOnTextChangeListener(crossinline onTextChanged: (text: String?) -> Unit) {
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            onTextChanged.invoke(p0.toString())
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

    onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            addTextChangedListener(textWatcher)
        } else {
            removeTextChangedListener(textWatcher)
        }
    }
}

fun TextInputLayout.validateNotNullOrEmpty() {
    error = if (editText?.text.isNullOrEmpty()) {
        context.getString(R.string.field_is_empty)
    } else {
        isErrorEnabled = false
        null
    }
}

fun TextInputLayout.validateTextShouldGreaterThan(minLength: Int) {
    error = if (editText?.text.toString().length < minLength) {
        context.getString(R.string.min_char, minLength)
    } else {
        isErrorEnabled = false
        null
    }
}

fun areAllTextInputLayoutValid(vararg fields: TextInputLayout?): Boolean {
    var isValid = true
    for (field in fields) {
        if (!field?.error.isNullOrEmpty()) {
            isValid = false
            break
        }
    }
    return isValid
}

fun Fragment.showMessage(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
}

fun Fragment.hideSoftKeyboard() {
    try {
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            view?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    } catch (ignored: Exception) {
    }
}

fun String.toCapitalizeWords(): String {
    return split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { it.uppercase() }
    }
}

fun ImageView.loadImageUrl(url: String?, imageType: ImageType = ImageType.DEFAULT) {
    val glide = Glide.with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .fitCenter()

    when (imageType) {
        ImageType.DEFAULT -> glide.into(this)
        ImageType.CIRCLE -> glide.circleCrop().into(this)
        ImageType.ROUNDED -> glide.transform(CenterCrop(), RoundedCorners(20)).into(this)
    }
}

fun Fragment.initToolbar(
    title: String,
    toolbar: MaterialToolbar,
    onNavigationUp: (() -> Unit)? = null
) {
    val toolbarColor = requireContext().getColor( R.color.black)
    val toolbarBackground = requireContext().getColor(R.color.white )

    toolbar.apply {
        this.title = title
        setNavigationOnClickListener {
            onNavigationUp?.invoke() ?: findNavController().navigateUp()
        }
        setTitleTextColor(toolbarColor)
        setNavigationIconTint(toolbarColor)
        setBackgroundColor(toolbarBackground)
    }
}
