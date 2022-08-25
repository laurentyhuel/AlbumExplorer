package com.lyh.albumexplorer.feature.album.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.lyh.albumexplorer.feature.album.R
import com.lyh.albumexplorer.feature.album.databinding.FragmentAlbumListBinding
import com.lyh.albumexplorer.feature.album.model.AlbumUi
import com.lyh.albumexplorer.feature.core.Resource
import com.lyh.albumexplorer.feature.core.ResourceError
import com.lyh.albumexplorer.feature.core.ResourceLoading
import com.lyh.albumexplorer.feature.core.ResourceSuccess
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumListFragment : Fragment() {

    private val albumListViewModel: AlbumListViewModel by viewModel()
    private var _binding: FragmentAlbumListBinding? = null
    private var albumDetailFragmentContainer: View? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAlbumListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        albumDetailFragmentContainer = view.findViewById(R.id.album_detail_nav_container)


        viewLifecycleOwner.lifecycleScope.launch {
            albumListViewModel.albums
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect(this@AlbumListFragment::bind)
        }

        binding.buttonRetry.setOnClickListener {
            albumListViewModel.triggerAlbums()
        }
    }

    private fun bind(albumsResource: Resource<List<AlbumUi>>) {

        with(binding) {
            recyclerView.isVisible = albumsResource is ResourceSuccess
            textViewMessage.isVisible = albumsResource !is ResourceSuccess
            imageViewFailed.isVisible = albumsResource is ResourceError
            buttonRetry.isVisible = albumsResource is ResourceError
            progressBar.isVisible = albumsResource is ResourceLoading
        }


        when (albumsResource) {
            is ResourceSuccess -> binding.recyclerView.adapter =
                AlbumAdapter(albumsResource.data, albumDetailFragmentContainer).apply {
                    stateRestorationPolicy =
                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
            is ResourceLoading -> binding.textViewMessage.text =
                getString(com.lyh.albumexplorer.feature.core.R.string.loading_data)
            is ResourceError -> binding.textViewMessage.text = context?.let {
                albumsResource.errorMessage.getMessage(
                    it
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}