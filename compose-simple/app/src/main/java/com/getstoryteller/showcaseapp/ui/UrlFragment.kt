package com.getstoryteller.showcaseapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.getstoryteller.showcaseapp.R

class UrlFragment : Fragment() {

  companion object {
    private const val ARG_URL = "url"

    fun newInstance(url: String) = UrlFragment().apply {
      arguments = Bundle().apply {
        putString(ARG_URL, url)
      }
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_story_clips, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val url = arguments?.getString(ARG_URL)
    view.findViewById<TextView>(R.id.textView).text = url ?: "No URL"
  }
}
