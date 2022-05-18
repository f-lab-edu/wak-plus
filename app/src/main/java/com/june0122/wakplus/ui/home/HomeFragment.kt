package com.june0122.wakplus.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.R
import com.june0122.wakplus.databinding.FragmentHomeBinding
import com.june0122.wakplus.ui.home.adapter.ContentListAdapter
import com.june0122.wakplus.ui.home.adapter.SnsListAdapter
import com.june0122.wakplus.ui.home.adapter.StreamerListAdapter
import com.june0122.wakplus.utils.CenterSmoothScroller
import com.june0122.wakplus.utils.decorations.SnsPlatformItemDecoration
import com.june0122.wakplus.utils.decorations.StreamerItemDecoration
import com.june0122.wakplus.utils.listeners.StreamerClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var streamerRecyclerView: RecyclerView
    private lateinit var snsRecyclerView: RecyclerView
    private lateinit var contentRecyclerView: RecyclerView
    private val homeViewModel: HomeViewModel by viewModels()
    private val horizontalLayoutManager =
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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

        if (contentListAdapter.itemCount == 0) {
            homeViewModel.collectAllStreamersContents()
        }

//        homeViewModel.contents.observe(viewLifecycleOwner) { contents ->
//            contentListAdapter.submitData(contents)
//        }

        lifecycleScope.launch {
            homeViewModel.contentFlow.collectLatest { pagingData ->
                contentListAdapter.submitData(pagingData)
            }
        }

        homeViewModel.snsPlatforms.observe(viewLifecycleOwner) { snsPlatforms ->
            snsListAdapter.submitList(snsPlatforms)
        }

        homeViewModel.streamers.observe(viewLifecycleOwner) { streamers ->
            streamerListAdapter.submitList(streamers)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        streamerRecyclerView.layoutManager = null
        snsRecyclerView.layoutManager = null
        contentRecyclerView.layoutManager = null
    }

    private fun configureRecyclerViews() {
        val streamerItemPx = resources.getDimensionPixelSize(R.dimen.margin_normal)
        val snsItemPx = resources.getDimensionPixelSize(R.dimen.margin_small)

        streamerRecyclerView = binding.rvStreamer.apply {
            this.layoutManager = horizontalLayoutManager
            adapter = streamerListAdapter
            addItemDecoration(StreamerItemDecoration(streamerItemPx))
        }

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

    private fun configureSmoothScroller(position: Int) {
        val smoothScroller = CenterSmoothScroller(requireContext())
        smoothScroller.targetPosition = position
        horizontalLayoutManager.startSmoothScroll(smoothScroller)
    }

    private fun launchSnsWithUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context?.startActivity(intent)
    }
}

