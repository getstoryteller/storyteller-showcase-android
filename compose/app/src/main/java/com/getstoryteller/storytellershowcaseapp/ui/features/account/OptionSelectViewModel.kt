package com.getstoryteller.storytellershowcaseapp.ui.features.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.getstoryteller.storytellershowcaseapp.domain.Config
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.domain.ports.StorytellerService
import com.getstoryteller.storytellershowcaseapp.remote.entities.AttributeDto
import com.getstoryteller.storytellershowcaseapp.remote.entities.AttributeValueDto
import com.storyteller.Storyteller
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OptionSelectViewModel @Inject constructor(
  private val sessionRepository: SessionRepository,
  private val storytellerService: StorytellerService,
) : ViewModel() {
  private val _uiState = MutableStateFlow(OptionSelectUIModel())
  val uiState: StateFlow<OptionSelectUIModel> = _uiState.asStateFlow()

  private val _reloadMainScreen = MutableLiveData<String?>()
  val reloadMainScreen: LiveData<String?> = _reloadMainScreen

  private val eventTrackingDto = AttributeDto(
    title = "Allow Event Tracking",
    urlName = "allow_event_tracking",
    sortOrder = 0,
    allowMultiple = false,
    nullable = false,
    defaultValue = "no",
    type = "event_tracking",
    isFollowable = false,
  )

  private val eventTrackingOptions = listOf(
    AttributeValueDto(
      title = "Yes",
      urlName = "yes",
      sortOrder = 0,
    ),
    AttributeValueDto(
      title = "No",
      urlName = "no",
      sortOrder = 1,
    ),
  )

  fun setupOptionType(
    config: Config,
    optionUrlName: String,
  ) {
    val attribute = config.attributes.keys.find { it.urlName == optionUrlName } ?: eventTrackingDto
    val storedAttributes = sessionRepository.attributes
    val keyValues = config.attributes[attribute] ?: eventTrackingOptions
    val selectedOption = storedAttributes[attribute.urlName] ?: attribute.defaultValue ?: ""
    val selectedOptions = if (attribute.allowMultiple) {
      selectedOption.split(",")
    } else {
      emptyList()
    }
    _uiState.value = OptionSelectUIModel(
      title = attribute.title,
      type = attribute.urlName,
      allowMultiple = attribute.allowMultiple,
      isFollowable = attribute.isFollowable,
      selectedOption = selectedOption,
      selectedOptions = selectedOptions,
      options = keyValues.map {
        KeyValueUiModel(
          key = it.title,
          value = it.urlName,
        )
      },
    )
  }

  fun selectOption(
    value: String?,
  ) {
    val isEventTracking = _uiState.value.type == eventTrackingDto.type
    if (isEventTracking) {
      _uiState.update { it.copy(selectedOption = value) }
      sessionRepository.trackEvents = value == "yes"
      storytellerService.updateCustomAttributes()
      _reloadMainScreen.value = value
      return
    }
    val isFollowable = _uiState.value.isFollowable
    if (isFollowable) {
      val previouslySelected = _uiState.value.selectedOption
      previouslySelected?.let {
        Storyteller.user.removeFollowedCategory(previouslySelected)
      }
      Storyteller.user.addFollowedCategory(value ?: "")
    }
    _uiState.update { it.copy(selectedOption = value) }
    val storedAttributes = sessionRepository.attributes.toMutableMap()
    val type = _uiState.value.type
    storedAttributes[type] = value
    sessionRepository.attributes = storedAttributes
    storytellerService.updateCustomAttributes()
    _reloadMainScreen.value = value
  }

  fun selectOptionMultiple(
    value: String,
    selected: Boolean,
  ) {
    val isFollowable = _uiState.value.isFollowable
    val selectedOptions = _uiState.value.selectedOptions.toMutableList()
    if (isFollowable) {
      selectedOptions.forEach {
        Storyteller.user.removeFollowedCategory(it)
      }
    }
    if (selected) {
      selectedOptions.add(value)
    } else {
      selectedOptions.remove(value)
    }
    if (isFollowable) {
      Storyteller.user.addFollowedCategories(selectedOptions)
    }
    _uiState.update { it.copy(selectedOptions = selectedOptions) }
    val storedAttributes = sessionRepository.attributes.toMutableMap()
    val type = _uiState.value.type
    val newSelectedOptions = selectedOptions.joinToString(",")
    storedAttributes[type] = newSelectedOptions
    sessionRepository.attributes = storedAttributes
    storytellerService.updateCustomAttributes()
    _reloadMainScreen.value = newSelectedOptions
  }

  fun onReloadingDone() {
    _reloadMainScreen.value = null
  }
}

data class OptionSelectUIModel(
  val title: String = "",
  val type: String = "",
  val allowMultiple: Boolean = false,
  val isFollowable: Boolean = false,
  val selectedOption: String? = null,
  val selectedOptions: List<String> = emptyList(),
  val options: List<KeyValueUiModel> = listOf(),
)

data class KeyValueUiModel(
  val key: String?,
  val value: String,
)
