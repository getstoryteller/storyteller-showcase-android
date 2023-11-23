package com.getstoryteller.storytellersampleapp.features.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.getstoryteller.storytellersampleapp.ui.SampleAppTheme
import com.storyteller.Storyteller
import com.storyteller.domain.entities.UserInput
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setup()
        setContent {
            val navController = rememberNavController()
            SampleAppTheme {
                Surface(
                    modifier = Modifier.background(color = MaterialTheme.colors.surface)
                ) {
                    MainScreen(
                        activity = this,
                        navController = navController,
                        viewModel = viewModel,
                        fragmentManager = supportFragmentManager
                    )
                }
            }
        }
    }
}