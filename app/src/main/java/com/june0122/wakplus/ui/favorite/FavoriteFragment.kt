package com.june0122.wakplus.ui.favorite

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
import com.june0122.wakplus.ui.home.adapter.SnsListAdapter
import com.june0122.wakplus.utils.decorations.SnsPlatformItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var snsRecyclerView: RecyclerView
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    private val snsListAdapter: SnsListAdapter = SnsListAdapter { position ->
        favoriteViewModel.onSnsClick(position)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureRecyclerViews()

        favoriteViewModel.snsListAdapter = snsListAdapter

        favoriteViewModel.snsPlatforms.observe(viewLifecycleOwner) { snsPlatforms ->
            snsListAdapter.submitList(snsPlatforms)
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
    }
}