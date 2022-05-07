package com.june0122.wakplus.ui.favorite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.R
import com.june0122.wakplus.databinding.FragmentFavoriteBinding
import com.june0122.wakplus.ui.home.adapter.ContentListAdapter
import com.june0122.wakplus.ui.home.adapter.SnsListAdapter
import com.june0122.wakplus.utils.decorations.SnsPlatformItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var snsRecyclerView: RecyclerView
    private lateinit var contentRecyclerView: RecyclerView
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    private val snsListAdapter: SnsListAdapter = SnsListAdapter { position ->
        favoriteViewModel.onSnsClick(position)
    }
    private val contentListAdapter = ContentListAdapter(
        { content -> favoriteViewModel.onFavoriteClick(content) },
        { url, _ -> launchSnsWithUrl(url) }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureRecyclerViews()

        favoriteViewModel.snsListAdapter = snsListAdapter
        favoriteViewModel.contentListAdapter = contentListAdapter

        favoriteViewModel.snsPlatforms.observe(viewLifecycleOwner) { snsPlatforms ->
            snsListAdapter.submitList(snsPlatforms)
        }

        favoriteViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            contentListAdapter.submitList(favorites)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snsRecyclerView.layoutManager = null
    }

    private fun configureRecyclerViews() {
        val snsItemPx = resources.getDimensionPixelSize(R.dimen.margin_small)

        snsRecyclerView = binding.rvSnsPlatform.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = snsListAdapter
            addItemDecoration(SnsPlatformItemDecoration(snsItemPx))
        }

        contentRecyclerView = binding.rvContent.apply {
            this.layoutManager = LinearLayoutManager(context)
            adapter = contentListAdapter
        }
    }

    private fun launchSnsWithUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context?.startActivity(intent)
    }
}