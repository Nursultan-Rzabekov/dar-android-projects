package com.example.kotlinarhitecturecomponents.mvvm.logic

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class LanguagesViewModelFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LanguagesViewModel(application) as T
    }
}