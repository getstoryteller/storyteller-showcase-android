package com.example.storytellerSampleAndroid.ads

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.storytellerSampleAndroid.R
import com.example.storytellerSampleAndroid.databinding.ActivityAdsBinding
import com.storyteller.Storyteller
import com.storyteller.Storyteller.Companion.activityReentered
import com.storyteller.ui.list.StorytellerClipsView

class AdsActivity : AppCompatActivity(R.layout.activity_ads) {

  private lateinit var binding: ActivityAdsBinding


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.decorView.systemUiVisibility =
      window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

    binding = ActivityAdsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    // set global delegate which can handle ads
    Storyteller.storytellerDelegate = StorytellerAdsDelegate(NativeAdsManager(this))

    binding.storytellerRowView.reloadData()
    binding.storytellerClipsView.apply {
      configuration = StorytellerClipsView.ListConfiguration(collection = "clipssample")
      reloadData()
    }
    //setup refresh layout
    binding.refreshLayout.setOnRefreshListener {
      /*
      Tell the SDK to load the latest data from the API
       */
      binding.storytellerRowView.reloadData()
      binding.storytellerClipsView.reloadData()
    }
  }

  override fun onActivityReenter(resultCode: Int, data: Intent?) {
    super.onActivityReenter(resultCode, data)
    // This method allows smooth close transition syncing. It should be used inside `onActivityReenter` only.
    activityReentered()
  }

}
