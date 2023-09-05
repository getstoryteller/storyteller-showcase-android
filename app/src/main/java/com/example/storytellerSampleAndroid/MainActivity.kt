package com.example.storytellerSampleAndroid

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.storytellerSampleAndroid.ads.NativeAdsManager
import com.example.storytellerSampleAndroid.ads.StorytellerAdsDelegate
import com.example.storytellerSampleAndroid.databinding.ActivityMainBinding
import com.example.storytellerSampleAndroid.settings.SettingDialogFragment
import com.example.storytellerSampleAndroid.ui.VerticalVideoListFragment
import com.storyteller.Storyteller
import com.storyteller.Storyteller.Companion.activityReentered
import com.storyteller.ui.pager.StorytellerClipsFragment

class MainActivity : AppCompatActivity() {

  private var fragment: StorytellerClipsFragment? = null
  private lateinit var binding: ActivityMainBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    //make layout stable during system UI changes like hiding/showing status bar
    window.decorView.systemUiVisibility =
      window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setSupportActionBar(binding.toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(false)
    supportActionBar?.setDisplayShowTitleEnabled(false)

    binding.navControls.setOnItemSelectedListener {
      when (it.itemId) {
        R.id.home -> {
          addVerticalVideoFragment()
          true
        }

        R.id.embedd -> {
          supportFragmentManager.beginTransaction().apply {
            fragment = StorytellerClipsFragment.create("demo")
            fragment?.let {
              replace(R.id.fragment_host, it)
            }
            fragment?.collectionId = "must-see-moments"
            disallowAddToBackStack()
            commit()
          }
          true
        }

        else -> false
      }
    }
    addVerticalVideoFragment()
    val storytellerAdsDelegate = StorytellerAdsDelegate(NativeAdsManager(this))
    Storyteller.storytellerDelegate = storytellerAdsDelegate
    binding.settings.setOnClickListener {
      SettingDialogFragment().show(
        supportFragmentManager, SettingDialogFragment::class.java.simpleName)
    }
    openDeepLink(intent)
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

  private fun addVerticalVideoFragment() {
    supportFragmentManager.beginTransaction().apply {
      replace(R.id.fragment_host, VerticalVideoListFragment())
      fragment = null
      disallowAddToBackStack()
      commit()
    }
  }

}
