package com.example.storytellerSampleAndroid.settings

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.storytellerSampleAndroid.MainActivity
import com.example.storytellerSampleAndroid.SampleApp
import com.example.storytellerSampleAndroid.databinding.FragmentSettingsBinding
import com.example.storytellerSampleAndroid.preferences.SharedPreferencesManager
import com.example.storytellerSampleAndroid.ui.VerticalVideoListFragment
import com.storyteller.Storyteller
import java.util.UUID

class SettingDialogFragment : DialogFragment() {

  private var _binding: FragmentSettingsBinding? = null
  private val binding get() = _binding!!

  private val preferencesManager by lazy { SharedPreferencesManager(requireContext()) }

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
    //language spinner
    val langAdapter = ArrayAdapter(
      requireContext(),
      R.layout.simple_spinner_item,
      Language.values().map { it.displayName })
    langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    binding.languageSpinner.adapter = langAdapter
    binding.languageSpinner.onItemSelectedListener =
      object : android.widget.AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
          parent: android.widget.AdapterView<*>?,
          view: View?,
          position: Int,
          id: Long
        ) {
          preferencesManager.language = Language.values()[position].code
        }

        override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
        }
      }
    binding.languageSpinner.setSelection(
      Language.values().indexOfFirst { it.code == preferencesManager.language })
    //team spinner

    val teamAdapter = ArrayAdapter(
      requireContext(),
      R.layout.simple_spinner_item,
      Team.values().map { it.description })
    langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

    binding.teamSpinner.adapter = teamAdapter
    binding.teamSpinner.onItemSelectedListener =
      object : android.widget.AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
          parent: android.widget.AdapterView<*>?,
          view: View?,
          position: Int,
          id: Long
        ) {
          preferencesManager.team = Team.values()[position].code
        }

        override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
        }
      }
    binding.teamSpinner.setSelection(
      Team.values().indexOfFirst { it.code == preferencesManager.team }
    )
    //user ID
    binding.userId.setText(Storyteller.currentUser?.externalId.toString())
    binding.randomUser.setOnClickListener {
      binding.userId.setText("randomUser-${UUID.randomUUID()}")
    }
    binding.applyBtn.setOnClickListener {

      SampleApp.initializeStoryteller(
        userId = binding.userId.text.toString(),
        onSuccess = {
          preferencesManager.userId = binding.userId.text.toString()
          Storyteller.user.setCustomAttribute("favoriteTeam", preferencesManager.team)
          (activity as? MainActivity)?.refreshData()
          dismiss()
        },
        onFailure = {
          dismiss()
        }
      )
    }
  }
}

enum class Language(val displayName: String, val code: String) {
  EN("English", "EN"),
  FR("French", "FR"),
  ES("Spanish", "ES"),
  JA("Japan", "JA")
}

enum class Team(val code: String, val description: String) {
  NONE("None", "None"),
  ATL("ATL", "Atlanta Hawks"),
  BOS("BOS", "Boston Celtics"),
  BKN("BKN", "Brooklyn Nets"),
  CHA("CHA", "Charlotte Hornets"),
  CHI("CHI", "Chicago Bulls"),
  CLE("CLE", "Cleveland Cavaliers"),
  DAL("DAL", "Dallas Mavericks"),
  DEN("DEN", "Denver Nuggets"),
  DET("DET", "Detroit Pistons"),
  GSW("GSW", "Golden State Warriors"),
  HOU("HOU", "Houston Rockets"),
  IND("IND", "Indiana Pacers"),
  LAC("LAC", "LA Clippers"),
  LAL("LAL", "Los Angeles Lakers"),
  MEM("MEM", "Memphis Grizzlies"),
  MIA("MIA", "Miami Heat"),
  MIL("MIL", "Milwaukee Bucks"),
  MIN("MIN", "Minnesota Timberwolves"),
  NOP("NOP", "New Orleans Pelicans"),
  NYK("NYK", "New York Knicks"),
  OKC("OKC", "Oklahoma City Thunder"),
  ORL("ORL", "Orlando Magic"),
  PHI("PHI", "Philadelphia 76ers"),
  PHX("PHX", "Phoenix Suns"),
  POR("POR", "Portland Trail Blazers"),
  SAC("SAC", "Sacramento Kings"),
  SAS("SAS", "San Antonio Spurs"),
  TOR("TOR", "Toronto Raptors"),
  UTA("UTA", "Utah Jazz"),
  WAS("WAS", "Washington Wizards");
}