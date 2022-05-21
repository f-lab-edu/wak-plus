package com.june0122.wakplus.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.june0122.wakplus.ui.home.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventSharedViewModel @Inject constructor(
    homeViewModel: HomeViewModel
) : ViewModel() {
    val isLoading: LiveData<Boolean> = homeViewModel.isLoading
}