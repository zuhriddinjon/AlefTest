package ru.alef.test.imagegallery.ui.gallery.photo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_photo.*
import ru.alef.test.imagegallery.R

const val KEY_PHOTO_EDIT = "key_photo_edit"

class PhotoFragment : Fragment(R.layout.fragment_photo), View.OnClickListener {

    private lateinit var photo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        photo = requireArguments().getString(KEY_PHOTO_EDIT, "")
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        Glide
            .with(requireContext())
            .load(photo)
//            .centerCrop()
            .centerInside()
            .error(R.drawable.logo)
            .into(iv_image)

        iv_close_photo.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        requireActivity().supportFragmentManager.popBackStack()
    }
}