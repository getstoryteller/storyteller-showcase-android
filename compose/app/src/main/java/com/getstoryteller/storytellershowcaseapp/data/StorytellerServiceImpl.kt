package com.getstoryteller.storytellershowcaseapp.data

import com.getstoryteller.storytellershowcaseapp.BuildConfig
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.domain.ports.StorytellerService
import com.storyteller.Storyteller
import com.storyteller.domain.entities.UserInput
import timber.log.Timber
import javax.inject.Inject

/**
 * This class is responsible for interacting with the Storyteller SDK's main instance methods
 * In particular, it is responsible for initializing the SDK when required.
 */
class StorytellerServiceImpl @Inject constructor(
  private val sessionRepository: SessionRepository,
  private val showcaseStorytellerDelegate: ShowcaseStorytellerDelegate,
) : StorytellerService {
  companion object {
    private const val LANGUAGE_ATTRIBUTE_KEY = "language"
    private const val FAV_TEAM_ATTRIBUTE_KEY = "favoriteTeam"
    private const val HAS_ACCOUNT_ATTRIBUTE_KEY = "hasAccount"
  }

  override fun initStoryteller() {
    Storyteller.apply {
      storytellerDelegate = showcaseStorytellerDelegate
      initialize(
        apiKey = sessionRepository.apiKey ?: "",
        userInput = sessionRepository.userId?.let { UserInput(it) },
        onSuccess = {
          Timber.i("Storyteller initialized")
        },
        onFailure = {
          Timber.i("Storyteller error $it")
        },
      )
    }
  }

  // This functions below show how to pass User Attributes to the Storyteller SDK
  // for the purposes of personalization and targeting of stories.
  // The corresponding code which calls these functions is available in the
  // AccountScreen.kt
  // There is more information available about this feature in our
  // documentation here https://www.getstoryteller.com/documentation/android/custom-attributes

  override fun updateCustomAttributes() {
    sessionRepository.language?.let {
      Storyteller.user.setCustomAttribute(LANGUAGE_ATTRIBUTE_KEY, it)
    } ?: Storyteller.user.removeCustomAttribute(LANGUAGE_ATTRIBUTE_KEY)
    sessionRepository.team?.let {
      Storyteller.user.setCustomAttribute(FAV_TEAM_ATTRIBUTE_KEY, it)
    } ?: Storyteller.user.removeCustomAttribute(FAV_TEAM_ATTRIBUTE_KEY)
    sessionRepository.hasAccount.let {
      Storyteller.user.setCustomAttribute(HAS_ACCOUNT_ATTRIBUTE_KEY, if (it) "yes" else "no")
    }

    // The code here shows to enable and disable event tracking for
    // the Storyteller SDK. The corresponding code which calls these
    // functions is visible in the AccountScreen.kt class

    if (sessionRepository.trackEvents) {
      BuildConfig.SHOWCASE_ACCESS_CODES
      Storyteller.enableEventTracking()
    } else {
      Storyteller.disableEventTracking()
    }
  }
}
