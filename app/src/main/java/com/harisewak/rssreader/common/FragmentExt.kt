package com.harisewak.rssreader.common

import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.harisewak.rssreader.R

fun Fragment.setTitle(title: String) {
    if (requireActivity() is AppCompatActivity) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = title
    }
}

fun Fragment.showProgress() {
    view?.findViewById<ProgressBar>(R.id.progress_bar)?.visibility = View.VISIBLE
}

fun Fragment.hideProgress() {
    view?.findViewById<ProgressBar>(R.id.progress_bar)?.visibility = View.GONE
}