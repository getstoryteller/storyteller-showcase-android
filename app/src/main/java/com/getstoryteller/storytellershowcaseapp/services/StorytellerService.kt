package com.getstoryteller.storytellershowcaseapp.services

import com.getstoryteller.storytellershowcaseapp.ads.StorytellerAdsDelegate
import com.storyteller.Storyteller
import com.storyteller.domain.entities.UserInput
import timber.log.Timber
import javax.inject.Inject

interface StorytellerService {
    fun initStoryteller()
    fun updateCustomAttributes()
}

// This class is responsible for interacting with the Storyteller SDK's main instance methods
// In particular, it is responsible for initializing the SDK when required.

class StorytellerServiceImpl @Inject constructor(
    private val sessionService: SessionService,
    private val storytellerAdsDelegate: StorytellerAdsDelegate
) : StorytellerService {

    companion object {
        private const val LANGUAGE_ATTRIBUTE_KEY = "language"
        private const val FAV_TEAM_ATTRIBUTE_KEY = "favoriteTeam"
        private const val HAS_ACCOUNT_ATTRIBUTE_KEY = "hasAccount"
    }

    override fun initStoryteller() {
        Storyteller.apply {
            this.storytellerDelegate = storytellerAdsDelegate
            this.initialize(
                apiKey = sessionService.apiKey ?: "",
                userInput = sessionService.userId?.let { UserInput(it) },
                onSuccess = {
                    Timber.i("Storyteller initialized")
                },
                onFailure = {
                    Timber.i("Storyteller error $it")
                }
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
        sessionService.language?.let {
            Storyteller.user.setCustomAttribute(LANGUAGE_ATTRIBUTE_KEY, it)
        } ?: Storyteller.user.removeCustomAttribute(LANGUAGE_ATTRIBUTE_KEY)
        sessionService.team?.let {
            Storyteller.user.setCustomAttribute(FAV_TEAM_ATTRIBUTE_KEY, it)
        } ?: Storyteller.user.removeCustomAttribute(FAV_TEAM_ATTRIBUTE_KEY)
        sessionService.hasAccount.let {
            Storyteller.user.setCustomAttribute(HAS_ACCOUNT_ATTRIBUTE_KEY, if (it) "yes" else "no")
        }

        // The code here shows to enable and disable event tracking for
        // the Storyteller SDK. The corresponding code which calls these
        // functions is visible in the AccountScreen.kt class

        if (sessionService.trackEvents) {
            Storyteller.enableEventTracking()
        } else {
            Storyteller.disableEventTracking()
        }
    }
}
