package com.vikram.turtlemintassignment.ui.issues

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.vikram.turtlemintassignment.data.IssuesRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class IssuesViewModelFactory (
    private val issuesRepository: IssuesRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (IssuesViewModel(issuesRepository) as T)
}