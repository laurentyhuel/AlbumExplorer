package com.lyh.albumexplorer.feature.album.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.lyh.albumexplorer.feature.album.R
import com.lyh.albumexplorer.feature.album.databinding.FragmentAlbumDetailBinding
import com.lyh.albumexplorer.feature.album.model.AlbumUi
import com.lyh.albumexplorer.feature.core.Resource
import com.lyh.albumexplorer.feature.core.ResourceError
import com.lyh.albumexplorer.feature.core.ResourceLoading
import com.lyh.albumexplorer.feature.core.ResourceSuccess
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumDetailFragment : Fragment() {

    private val albumViewModel: AlbumViewModel by viewModel()

    private var _binding: FragmentAlbumDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var albumId: Long = NO_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val stringId = it.getString(ARG_ITEM_ID)
            if (stringId?.isNotEmpty() == true) {
                albumId = stringId.toLong()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (albumId == NO_ID) {
            binding.layout.isVisible = false
        } else {
            binding.layout.isVisible = true
            viewLifecycleOwner.lifecycleScope.launch {
                albumViewModel.album
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .collect(this@AlbumDetailFragment::bind)
            }
            albumViewModel.setAlbumId(albumId)
        }
    }

    private fun bind(albumsResource: Resource<AlbumUi>) {

        with(binding) {
            groupAlbum.isVisible = albumsResource is ResourceSuccess
            progressBar.isVisible = albumsResource is ResourceLoading
            groupError.isVisible = albumsResource is ResourceError
        }

        when (albumsResource) {
            is ResourceSuccess -> with(binding) {
                val albumUi = albumsResource.data
                textViewTitle.text = albumUi.title
                textViewNumber.text = getString(R.string.album_number, albumUi.id)
                imageViewAlbum.load(albumsResource.data.url)
            }
            is ResourceError -> with(binding) {
                textViewMessage.text = context?.let {
                    albumsResource.errorMessage.getMessage(
                        it
                    )
                }
            }
            is ResourceLoading -> {} // Nothing to do
        }
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
        const val NO_ID = 0L
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
