package com.june0122.wakplus.ui.settings.dark

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.june0122.wakplus.R
import com.june0122.wakplus.databinding.FragmentDarkModeBinding
import com.june0122.wakplus.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DarkModeFragment : Fragment() {
    private lateinit var binding: FragmentDarkModeBinding
    private val darkModeViewModel: DarkModeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dark_mode, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).setupActionBar(binding.toolbar)

        // TODO: RecyclerView를 통해 다크 모드 설정 화면 구현
        binding.layoutLightMode.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            darkModeViewModel.onModeClick(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.layoutDarkMode.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            darkModeViewModel.onModeClick(AppCompatDelegate.MODE_NIGHT_YES)
        }

        binding.layoutSystemMode.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
                darkModeViewModel.onModeClick(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                darkModeViewModel.onModeClick(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            }
        }

        darkModeViewModel.mode.observe(viewLifecycleOwner) { mode ->
            when (mode) {
                AppCompatDelegate.MODE_NIGHT_NO -> {
                    binding.imgCheckLight.visibility = View.VISIBLE
                    binding.imgCheckDark.visibility = View.INVISIBLE
                    binding.imgCheckSystem.visibility = View.INVISIBLE
                }
                AppCompatDelegate.MODE_NIGHT_YES -> {
                    binding.imgCheckLight.visibility = View.INVISIBLE
                    binding.imgCheckDark.visibility = View.VISIBLE
                    binding.imgCheckSystem.visibility = View.INVISIBLE
                }
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM or
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> {
                    binding.imgCheckLight.visibility = View.INVISIBLE
                    binding.imgCheckDark.visibility = View.INVISIBLE
                    binding.imgCheckSystem.visibility = View.VISIBLE
                }
            }
        }
    }
}