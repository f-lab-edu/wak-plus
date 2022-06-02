package com.june0122.wakplus.ui.settings.dark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.june0122.wakplus.R
import com.june0122.wakplus.databinding.FragmentDarkModeBinding
import com.june0122.wakplus.ui.main.MainActivity

class DarkModeFragment : Fragment() {
    private lateinit var binding: FragmentDarkModeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dark_mode, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).setupActionBar(binding.toolbar)
    }
}