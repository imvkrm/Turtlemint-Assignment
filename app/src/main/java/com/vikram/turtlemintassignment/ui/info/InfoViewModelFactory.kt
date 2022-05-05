package com.vikram.turtlemintassignment.ui.info

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.vikram.turtlemintassignment.data.IssuesRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class InfoViewModelFactory (
    private val issuesRepository: IssuesRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (InfoViewModel(issuesRepository) as T)
}