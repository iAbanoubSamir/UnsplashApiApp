package com.profabanoub.unsplashapi.ui.fragment.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.profabanoub.unsplashapi.R
import com.profabanoub.unsplashapi.data.remote.model.PhotoModel
import com.profabanoub.unsplashapi.databinding.FragmentGalleryBinding
import com.profabanoub.unsplashapi.ui.paging.UnsplashPhotoAdapter
import com.profabanoub.unsplashapi.ui.paging.UnsplashPhotoLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery),
    UnsplashPhotoAdapter.OnItemClickListener {

    private val viewModel by viewModels<GalleryViewModel>()
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)

        val unsplashAdapter = UnsplashPhotoAdapter(this)

        binding.apply {
            galleryRecycleView.setHasFixedSize(true)
            galleryRecycleView.itemAnimator = null
            galleryRecycleView.adapter = unsplashAdapter.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoLoadStateAdapter { unsplashAdapter.retry() },
                footer = UnsplashPhotoLoadStateAdapter { unsplashAdapter.retry() }
            )

            galleryButtonRetry.setOnClickListener {
                unsplashAdapter.retry()
            }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            unsplashAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        unsplashAdapter.addLoadStateListener { loadState ->
            binding.apply {
                galleryProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                galleryRecycleView.isVisible = loadState.source.refresh is LoadState.NotLoading
                galleryButtonRetry.isVisible = loadState.source.refresh is LoadState.Error
                galleryTextViewError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    unsplashAdapter.itemCount < 1
                ) {
                    galleryRecycleView.isVisible = false
                    galleryTextViewEmpty.isVisible = true
                } else {
                    galleryTextViewEmpty.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.galleryRecycleView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(photo: PhotoModel) {
        val action = GalleryFragmentDirections
            .actionGalleryFragment2ToDetailsFragment(photo)
        findNavController().navigate(action)
    }
}