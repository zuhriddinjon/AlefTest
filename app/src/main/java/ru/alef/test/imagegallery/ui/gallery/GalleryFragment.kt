package ru.alef.test.imagegallery.ui.gallery

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_gallery.*
import ru.alef.test.imagegallery.R
import ru.alef.test.imagegallery.common.lazyFast
import ru.alef.test.imagegallery.data.NetworkStatus
import ru.alef.test.imagegallery.ui.gallery.adapter.PhotoAdapter
import ru.alef.test.imagegallery.ui.gallery.viewmodel.GalleryViewModel

class GalleryFragment : Fragment(R.layout.fragment_gallery),
    PhotoAdapter.OnPhotoClickListener,
    Observer<NetworkStatus>,
    SwipeRefreshLayout.OnRefreshListener {

    private var clickListener: PhotoListener? = null
    private val photoAdapter: PhotoAdapter by lazyFast { PhotoAdapter(this) }
    private val galleryViewModel: GalleryViewModel by activityViewModels()
    private val observerPhotos = Observer<List<String>> {
        photoAdapter.submitList(it)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PhotoListener)
            clickListener = context
    }

    override fun onDetach() {
        super.onDetach()
        clickListener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null)
            galleryViewModel.loadPhotos()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservers()
    }

    private fun setupObservers() {
        galleryViewModel.liveStatus.observe(viewLifecycleOwner, this)
        galleryViewModel.livePhotos.observe(viewLifecycleOwner, observerPhotos)
    }

    private fun setupViews() {
        rv_gallery.adapter = photoAdapter
        swipe_refresh.setOnRefreshListener(this)
    }

    override fun onPhotoClicked(position: Int, imageView: View) {
        val photoUrl = photoAdapter.getItem(position)
        clickListener?.onPhotoClicked(photoUrl, imageView)
    }

    override fun onChanged(t: NetworkStatus) {
        when (t) {
            is NetworkStatus.Loading -> swipe_refresh.isRefreshing = true
            is NetworkStatus.Success -> swipe_refresh.isRefreshing = false
            is NetworkStatus.Error -> {
                swipe_refresh.isRefreshing = false
                Toast.makeText(requireContext(), t.errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRefresh() {
        galleryViewModel.loadPhotos()
    }

    interface PhotoListener {
        fun onPhotoClicked(photoUrl: String, imageView: View)
    }
}