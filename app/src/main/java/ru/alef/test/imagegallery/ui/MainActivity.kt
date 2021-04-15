package ru.alef.test.imagegallery.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import ru.alef.test.imagegallery.R
import ru.alef.test.imagegallery.ui.gallery.GalleryFragment
import ru.alef.test.imagegallery.ui.gallery.adapter.PhotoAdapter
import ru.alef.test.imagegallery.ui.gallery.photo.KEY_PHOTO_EDIT
import ru.alef.test.imagegallery.ui.gallery.photo.PhotoFragment

class MainActivity : AppCompatActivity(),
    GalleryFragment.PhotoListener,
    Runnable {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var isDoubleClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            showGallery()
    }

    private fun showGallery() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_main, GalleryFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            if (isDoubleClicked)
                finish()
            isDoubleClicked = true
            handler.postDelayed(this, 200)
        } else
            super.onBackPressed()
    }

    override fun run() {
        isDoubleClicked = false
    }

    override fun onPhotoClicked(photoUrl: String, imageView: View) {
        val args = bundleOf(Pair(KEY_PHOTO_EDIT, photoUrl))
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .add(R.id.container_main, PhotoFragment::class.java, args)
            .addSharedElement(imageView, "image")
            .addToBackStack(null)
            .commit()
    }
}