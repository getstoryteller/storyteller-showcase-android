package com.example.storytellerSampleAndroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.storyteller.ui.pager.StorytellerClipsFragment

class EmbeddedClipActivity : AppCompatActivity(R.layout.activity_embeded_clip) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val clipFragment = StorytellerClipsFragment.create(
            collectionId = "clipssample",
            topLevelBackEnabled = true,
        ).apply {
            listener = object : StorytellerClipsFragment.Listener {
                override fun onTopLevelBackPressed(): Boolean {
                    finish()
                    return true
                }
            }
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container_view, clipFragment)
        transaction.commit()
    }
}
