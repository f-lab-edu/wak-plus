package com.june0122.wakplus.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.june0122.wakplus.R
import com.june0122.wakplus.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutDarkMode.setOnClickListener { layout ->
            layout.findNavController().navigate(R.id.action_settings_to_dark_mode)
        }

        binding.layoutScreenThemes.setOnClickListener { layout ->
            layout.findNavController().navigate(R.id.action_settings_to_themes)
        }
    }
}