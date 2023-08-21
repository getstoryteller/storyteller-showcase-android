package com.example.storytellerSampleAndroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storytellerSampleAndroid.SampleApp.Companion.initializeStoryteller
import com.example.storytellerSampleAndroid.databinding.ActivityMainBinding
import com.example.storytellerSampleAndroid.ui.VerticalVideoListFragment
import com.example.storytellerSampleAndroid.ui.VerticalVideoListViewModel
import com.storyteller.Storyteller
import com.storyteller.Storyteller.Companion.activityReentered
import com.storyteller.ui.pager.StorytellerClipsFragment
import java.util.UUID

class MainActivity : AppCompatActivity(R.layout.activity_main) {

  private var fragment: StorytellerClipsFragment? = null
  private lateinit var binding: ActivityMainBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    //make layout stable during system UI changes like hiding/showing status bar
    window.decorView.systemUiVisibility =
      window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

    binding = ActivityMainBinding.inflate(layoutInflater)

    binding.navControls.setOnItemSelectedListener {
      Log.d("FINA", "onCreate: ---")
      when (it.itemId) {
        R.id.home -> {
          Log.d("FINA", "onCreate: ---")
          addVerticalVideoFragment()
          true
        }
        R.id.embedd -> {
          Log.d("FINA", "onCreate: ---")
          supportFragmentManager.beginTransaction().apply {
            fragment = StorytellerClipsFragment.create("demo")
            fragment?.let {
              replace(R.id.fragment_host, it)
            }
            disallowAddToBackStack()
            commit()
          }
          true
        }

        else -> false
      }
    }
    addVerticalVideoFragment()
    //openDeepLink(intent)
  }

  override fun onActivityReenter(resultCode: Int, data: Intent?) {
    super.onActivityReenter(resultCode, data)
    // This method allows smooth close transition syncing. It should be used inside `onActivityReenter` only.
    activityReentered()
  }


  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    openDeepLink(intent)
  }

  private fun openDeepLink(intent: Intent?) {
    val deepLink = intent?.dataString
    if (deepLink != null && Storyteller.isStorytellerDeepLink(deepLink)) {
      Storyteller.openDeepLink(this, deepLink)
    }
  }

  private fun changeUser() {
    /*
    If you use login in your app and wish to allow users to logout and log back in as a new user
    (or proceed as an anonymous user) then when a user logs out you should call initialize
    again specifying a new externalId. Note that this will reset the local store of which pages the user has viewed.
    For more info, see - https://www.getstoryteller.com/documentation/android/users
    */
    val freshUserId = UUID.randomUUID().toString()
    initializeStoryteller(
      userId = freshUserId,
      onSuccess =
      {
        // binding.storytellerRowView.reloadData()
        Toast.makeText(
          this@MainActivity, "New User with Id: $freshUserId", Toast.LENGTH_SHORT
        ).show()
        Log.i("Storyteller Sample", "initialize success ${Storyteller.currentUser}")
      },
      onFailure = {
        Log.e("Storyteller Sample", "initialize failed $it}")
      })

  }
  private fun addVerticalVideoFragment() {
    supportFragmentManager.beginTransaction().apply {
      replace(R.id.fragment_host, VerticalVideoListFragment())
      fragment = null
      disallowAddToBackStack()
      commit()
    }
  }
}
