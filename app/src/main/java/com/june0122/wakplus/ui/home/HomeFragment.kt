package com.june0122.wakplus.ui.home

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
import com.june0122.wakplus.databinding.FragmentHomeBinding
import com.june0122.wakplus.ui.home.HomeViewModel.Companion.TWITCH_ID_INE
import com.june0122.wakplus.ui.home.HomeViewModel.Companion.YOUTUBE_ID_INE
import com.june0122.wakplus.ui.home.adapter.ContentListAdapter
import com.june0122.wakplus.ui.home.adapter.SnsListAdapter
import com.june0122.wakplus.ui.home.adapter.StreamerListAdapter
import com.june0122.wakplus.utils.decorations.SnsPlatformItemDecoration
import com.june0122.wakplus.utils.decorations.StreamerItemDecoration

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var streamerRecyclerView: RecyclerView
    private lateinit var snsRecyclerView: RecyclerView
    private lateinit var contentRecyclerView: RecyclerView
    private val homeViewModel: HomeViewModel by viewModels()
    private val contentListAdapter: ContentListAdapter = ContentListAdapter()
    private val streamerListAdapter: StreamerListAdapter = StreamerListAdapter()
    private val snsListAdapter: SnsListAdapter = SnsListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureRecyclerViews()

        homeViewModel.contentListAdapter = contentListAdapter

        if (contentListAdapter.itemCount == 0) {
            homeViewModel.getTwitchVideos(TWITCH_ID_INE)
            homeViewModel.getYoutubeVideos(YOUTUBE_ID_INE)
        }

        homeViewModel.contents.observe(requireActivity()) {
            contentListAdapter.updateUserListItems(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        contentRecyclerView.layoutManager = null
    }

    private fun configureRecyclerViews() {
        val largePx = resources.getDimensionPixelSize(R.dimen.margin_large)
        val smallPx = resources.getDimensionPixelSize(R.dimen.margin_small)

        streamerRecyclerView = binding.rvStreamer.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = streamerListAdapter
            addItemDecoration(StreamerItemDecoration(largePx))
        }

        snsRecyclerView = binding.rvSnsPlatform.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = snsListAdapter
            addItemDecoration(SnsPlatformItemDecoration(smallPx))
        }

        contentRecyclerView = binding.rvContent.apply {
            this.layoutManager = LinearLayoutManager(context)
            adapter = contentListAdapter
        }
    }
}

