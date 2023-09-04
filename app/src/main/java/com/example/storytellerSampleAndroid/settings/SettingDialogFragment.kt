package com.example.storytellerSampleAndroid.settings

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.storytellerSampleAndroid.databinding.FragmentSettingsBinding
import com.example.storytellerSampleAndroid.databinding.FragmentVerticalVideoBinding

class SettingDialogFragment : DialogFragment() {

  private var _binding: FragmentSettingsBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSettingsBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val langAdapter  = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, Language.values().map { it.displayName })
    langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

    binding.languageSpinner.adapter = langAdapter
  }
}

enum class Language(val displayName: String, val code: String) {
  EN("English", "EN"),
  FR("French", "FR"),
  ES("Spanish", "ES"),
  JA("Japan", "JA")
}