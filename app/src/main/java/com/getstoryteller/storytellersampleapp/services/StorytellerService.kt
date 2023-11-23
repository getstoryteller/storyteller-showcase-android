package com.getstoryteller.storytellersampleapp.services

import com.storyteller.Storyteller
import com.storyteller.domain.entities.UserInput
import com.storyteller.domain.usecases.attributes.UserAttributes
import timber.log.Timber
import javax.inject.Inject

interface StorytellerService {
    fun initStoryteller()
    fun updateCustomAttributes()
}

class StorytellerServiceImpl @Inject constructor(
    private val sessionService: SessionService
) : StorytellerService {

    companion object {
        private const val LANGUAGE_ATTRIBUTE_KEY = "language"
        private const val FAV_TEAM_ATTRIBUTE_KEY = "favoriteTeam"
        private const val HAS_ACCOUNT_ATTRIBUTE_KEY = "hasAccount"
    }

    override fun initStoryteller() {
        Storyteller.apply {
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
    }
}