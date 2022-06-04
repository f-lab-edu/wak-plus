package com.june0122.wakplus.ui.settings.theme

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
import com.june0122.wakplus.databinding.FragmentThemesBinding
import com.june0122.wakplus.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThemeFragment : Fragment() {
    private lateinit var binding: FragmentThemesBinding
    private lateinit var themeRecyclerView: RecyclerView
    private val themeViewModel: ThemeViewModel by viewModels()
    private val themeListAdapter: ThemeListAdapter = ThemeListAdapter { theme ->
        themeViewModel.onThemeClick(theme)
        requireActivity().recreate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_themes, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setupActionBar(binding.toolbar)

        configureRecyclerView()
    }

    private fun configureRecyclerView() {
        themeRecyclerView = binding.rvTheme.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = themeListAdapter
        }
    }
}