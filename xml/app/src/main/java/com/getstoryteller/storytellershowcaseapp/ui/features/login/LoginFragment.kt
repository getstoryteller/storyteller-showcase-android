package com.getstoryteller.storytellershowcaseapp.ui.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.getstoryteller.storytellershowcaseapp.databinding.FragmentLoginBinding
import com.getstoryteller.storytellershowcaseapp.ui.utils.observeOnState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

  private var nullableBinding: FragmentLoginBinding? = null
  private val binding get() = nullableBinding!!

  private val viewModel by viewModels<LoginViewModel>()

  private val navigation by lazy { findNavController() }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    nullableBinding = FragmentLoginBinding.inflate(inflater, container, false)
    setKeyboardListener()
    setButtonListener()
    observeEffects()
    observeState()
    return binding.root
  }

  private fun setKeyboardListener() {
    ViewCompat.setOnApplyWindowInsetsListener(binding.dialog) { _, insets ->
      val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
      binding.dialog.updateLayoutParams<ConstraintLayout.LayoutParams> {
        verticalBias = if (imeVisible) 0.3f else 0.5f
      }
      insets
    }
  }

  private fun setButtonListener() {
    binding.button.setOnClickListener {
      viewModel.verifyCode(binding.accessCodeEditText.text.toString())
    }
  }

  private fun observeState() {
    observeOnState(state = Lifecycle.State.STARTED) {
      viewModel.state.collect { state ->
        binding.progress.isVisible = state.isLoading
        binding.button.isVisible = !state.isLoading
        binding.error.apply {
          isVisible = state.isError
          text = state.errorMessage
        }
      }
    }
  }

  private fun observeEffects() {
    observeOnState(state = Lifecycle.State.STARTED) {
      viewModel.effects.collect { effect ->
        when (effect) {
          is LoginContract.Effect.NavigateToMainScreen -> {
            navigation.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
          }
        }
      }
    }
  }
}
