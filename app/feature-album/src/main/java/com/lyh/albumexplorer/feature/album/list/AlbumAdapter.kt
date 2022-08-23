package com.lyh.albumexplorer.feature.album.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lyh.albumexplorer.feature.album.R
import com.lyh.albumexplorer.feature.album.databinding.ItemAlbumBinding
import com.lyh.albumexplorer.feature.album.detail.AlbumDetailFragment
import com.lyh.albumexplorer.feature.album.model.AlbumUi

class AlbumAdapter(
    private val albums: List<AlbumUi>,
    private val albumDetailFragmentContainer: View?
) : RecyclerView.Adapter<AlbumAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemBinding =
            ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val albumUi = albums[position]
        holder.bind(albumUi)

        with(holder.itemView) {
            tag = albumUi
            setOnClickListener { itemView ->
                val bundle = Bundle()
                bundle.putString(
                    AlbumDetailFragment.ARG_ITEM_ID,
                    albumUi.id.toString()
                )

                if (albumDetailFragmentContainer != null) {
                    albumDetailFragmentContainer.findNavController()
                        .navigate(R.id.fragment_album_detail, bundle)
                } else {
                    itemView.findNavController().navigate(R.id.show_album_detail, bundle)
                }
            }
        }
    }

    override fun getItemCount(): Int = albums.size

    class ItemViewHolder(private val itemBinding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(albumUi: AlbumUi) {
            with(itemBinding) {
                textViewTitle.text = albumUi.title
                shapeableImageView.load(albumUi.thumbnailUrl)
            }
        }
    }
}
