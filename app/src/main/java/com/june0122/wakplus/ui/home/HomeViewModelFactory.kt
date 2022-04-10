package com.june0122.wakplus.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.june0122.wakplus.data.repository.ContentRepository

class HomeViewModelFactory(private val repository: ContentRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(repository) as T
        } else {
            throw IllegalStateException("ViewModel Class Not Found")
        }
    }
}