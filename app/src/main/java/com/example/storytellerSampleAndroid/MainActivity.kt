package com.example.storytellerSampleAndroid

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.storytellerSampleAndroid.SampleApp.Companion.initializeStoryteller
import com.example.storytellerSampleAndroid.ads.AdsActivity
import com.example.storytellerSampleAndroid.compose.JetpackComposeActivity
import com.example.storytellerSampleAndroid.databinding.ActivityMainBinding
import com.example.storytellerSampleAndroid.multiple.MultipleListsActivity
import com.storyteller.Storyteller
import com.storyteller.Storyteller.Companion.activityReentered
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import com.storyteller.domain.entities.UserActivity
import com.storyteller.domain.entities.UserActivityData
import com.storyteller.domain.entities.ads.AdResponse
import com.storyteller.ui.list.StorytellerDelegate
import com.storyteller.ui.list.StorytellerListViewDelegate
import java.util.UUID

class MainActivity : AppCompatActivity(R.layout.activity_main), StorytellerDelegate,
    StorytellerListViewDelegate {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //make layout stable during system UI changes like hiding/showing status bar
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //enable sdk logging for debug
        //Storyteller.enableLogging = true

        /*
        The global delegate is used for data related events coming from the StoryPlayer
        */
        Storyteller.storytellerDelegate = this
        /*
        The delegate is used for the SDK to manage events from a StorytellerRowView instance
        Assign it to the appropriate activity to receive callbacks
        For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-delegate#HowToUse
         */
        binding.storytellerRowView.delegate = this

        binding.storytellerRowView.reloadData()

        //setup refresh layout
        binding.refreshLayout.setOnRefreshListener {
            /*
            Tell the SDK to load the latest data from the API
             */
            binding.storytellerRowView.reloadData()
        }

        //setup change user button
        binding.changeUserButton.setOnClickListener { changeUser() }
        //open activity with advanced sample
        binding.multipleListsButton.setOnClickListener {
            startActivity(Intent(this, MultipleListsActivity::class.java))
        }

        binding.goToJetpackComposeScreen.setOnClickListener {
                val intent = Intent(this@MainActivity, JetpackComposeActivity::class.java)
                startActivity(intent)
        }

        binding.goToAdsScreen.setOnClickListener {
            val intent = Intent(this@MainActivity, AdsActivity::class.java)
            startActivity(intent)
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
                binding.storytellerRowView.reloadData()
                Toast.makeText(
                    this@MainActivity, "New User with Id: $freshUserId", Toast.LENGTH_SHORT
                ).show()
                Log.i("Storyteller Sample", "initialize success ${Storyteller.currentUser}")
            },
            onFailure = {
                Log.e("Storyteller Sample", "initialize failed $it}")
            })

    }

    /*
    Called when the data loading network request is complete
    For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-list-view-delegate#ErrorHandling
     */
    override fun onDataLoadComplete(
        success: Boolean,
        error: com.storyteller.domain.entities.Error?,
        dataCount: Int
    ) {
        Log.i(
            "Storyteller Sample",
            "onDataLoadComplete callback: success $success, error $error, dataCount $dataCount"
        )
        binding.refreshLayout.isRefreshing = false
    }

    /*
    Called when the network request to load data for all stories has started
     */
    override fun onDataLoadStarted() {
        Log.i("Storyteller Sample", "onDataLoadStarted callback")
    }

    /*
    Called when story player has been dismissed
     */
    override fun onPlayerDismissed() {
        Log.i("Storyteller Sample", "onPlayerDismissed callback")
    }

    /*
     Called when an analytics event is triggered
     For more info, see - https://www.getstoryteller.com/documentation/android/analytics
     */
    override fun onUserActivityOccurred(type: UserActivity.EventType, data: UserActivityData) {
        Log.i("Storyteller Sample", "onUserActivityOccurred: type $type data $data")
    }

    /*
    Called when a user swipes up on a page which should direct the user
    to a specific place within the integrating app.
    For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-delegate#SwipingUpToTheIntegratingApp
     */
    override fun userNavigatedToApp(url: String) {
        Log.i("Storyteller Sample", "userNavigatedToApp: swipeUpUrl $url")
        // Pass swipeUpUrl from SDK callback to OtherActivity where it can be accessed as an extra string value when it is started
        startActivity(Intent(this, OtherActivity::class.java).apply {
            putExtra("EXTRA_SWIPE_URL", url)
        })
    }

    /*
    Called when a user swipes up on a page which opens a web link.
    Allows to configure WebViewClient if required.
    For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-delegate#HowToUse
    */
    override fun configureWebView(view: WebView, url: String?, favicon: Bitmap?) {
        Log.i("Storyteller Sample", "configureWebView $url")
    }

    /*
    Called when the tenant is configured to request ads from the containing app
    and the SDK requires ad data from the containing app
    For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-delegate#ClientAds
     */
    override fun getAdsForList(
        adRequestInfo: StorytellerAdRequestInfo,
        onComplete: (AdResponse) -> Unit,
        onError: () -> Unit
    ) {
        Log.i("Storyteller Sample", "getAdsForList $adRequestInfo")
    }
}
