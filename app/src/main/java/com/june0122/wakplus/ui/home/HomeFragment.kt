package com.june0122.wakplus.ui.home

import android.content.Context
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
import com.june0122.wakplus.databinding.FragmentHomeBinding
import com.june0122.wakplus.ui.home.adapter.ContentListAdapter
import com.june0122.wakplus.ui.home.adapter.SnsListAdapter
import com.june0122.wakplus.ui.home.adapter.StreamerListAdapter
import com.june0122.wakplus.utils.CenterSmoothScroller
import com.june0122.wakplus.utils.EmptyDataObserver
import com.june0122.wakplus.utils.decorations.SnsPlatformItemDecoration
import com.june0122.wakplus.utils.decorations.StreamerItemDecoration
import com.june0122.wakplus.utils.listeners.DataLoadListener
import com.june0122.wakplus.utils.listeners.StreamerClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var streamerRecyclerView: RecyclerView
    private lateinit var snsRecyclerView: RecyclerView
    private lateinit var contentRecyclerView: RecyclerView
    private lateinit var dataLoadListener: DataLoadListener
    private val homeViewModel: HomeViewModel by viewModels()
    private val contentListAdapter = ContentListAdapter(
        { content -> homeViewModel.onFavoriteClick(content) },
        { url, _ -> launchSnsWithUrl(url) }
    )
    private val streamerListAdapter = StreamerListAdapter(
        object : StreamerClickListener {
            override fun onStreamerClick(position: Int) {
                configureSmoothScroller(position)
                homeViewModel.onStreamerClick(position)
            }

            override fun onStreamerLongClick(position: Int) {
            }
        }
    )
    private val snsListAdapter: SnsListAdapter = SnsListAdapter { position ->
        homeViewModel.onSnsClick(position)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is DataLoadListener) {
            dataLoadListener = context
        }

        if (this::dataLoadListener.isInitialized.not()) {
            throw RuntimeException("lateinit property $dataLoadListener has not been initialized")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureRecyclerViews()

        homeViewModel.contentListAdapter = contentListAdapter
        homeViewModel.streamerListAdapter = streamerListAdapter
        homeViewModel.snsListAdapter = snsListAdapter
        homeViewModel.initContentList()

        homeViewModel.contents.observe(viewLifecycleOwner) { contents ->
            contentListAdapter.submitList(contents)
        }

        homeViewModel.snsPlatforms.observe(viewLifecycleOwner) { snsPlatforms ->
            snsListAdapter.submitList(snsPlatforms)
        }

        homeViewModel.streamers.observe(viewLifecycleOwner) { streamers ->
            streamerListAdapter.submitList(streamers)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { status ->
            dataLoadListener.onStatusChanged(status)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dataLoadListener.onStatusChanged(false)
    }

    private fun configureRecyclerViews() {
        val streamerItemPx = resources.getDimensionPixelSize(R.dimen.margin_normal)
        val snsItemPx = resources.getDimensionPixelSize(R.dimen.margin_small)

        streamerRecyclerView = binding.rvStreamer.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = streamerListAdapter
            addItemDecoration(StreamerItemDecoration(streamerItemPx))
        }

        snsRecyclerView = binding.rvSnsPlatform.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = snsListAdapter
            addItemDecoration(SnsPlatformItemDecoration(snsItemPx))
        }

        contentRecyclerView = binding.rvContent.apply {
            val emptyObserver = EmptyDataObserver(binding.rvContent, binding.layoutEmptyContent)
            this.layoutManager = LinearLayoutManager(context)
            adapter = contentListAdapter.apply {
                registerAdapterDataObserver(emptyObserver)
            }
        }
    }

    private fun configureSmoothScroller(position: Int) {
        val smoothScroller = CenterSmoothScroller(requireContext())
        smoothScroller.targetPosition = position
        streamerRecyclerView.layoutManager?.startSmoothScroll(smoothScroller)
    }

    private fun launchSnsWithUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context?.startActivity(intent)
    }
}

