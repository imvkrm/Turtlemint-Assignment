package com.vikram.turtlemintassignment.ui.issues

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.vikram.turtlemintassignment.R
import com.vikram.turtlemintassignment.data.IssuesRepository
import com.vikram.turtlemintassignment.data.local.IssuesItems
import com.vikram.turtlemintassignment.other.Event
import com.vikram.turtlemintassignment.other.StatusResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class IssuesViewModel(
    private val issuesRepository: IssuesRepository
) : ViewModel() {

    val issuesItems = issuesRepository.observeAllIssuesItems()

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText


    private fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = Event(message)
    }

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading


    private val _items: LiveData<List<IssuesItems>> = fetchDataFromRepository()
    val items: LiveData<List<IssuesItems>> = _items

    fun fetchDataFromRepository(): LiveData<List<IssuesItems>> {

        _dataLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            issuesRepository.refreshAllIssuesItems()
        }

        return issuesRepository.observeAllIssuesItems().switchMap { filterIssues(it) }

    }

    private fun filterIssues(tasksResult: StatusResult<List<IssuesItems>>): LiveData<List<IssuesItems>> {

        val result = MutableLiveData<List<IssuesItems>>()

        if (tasksResult is StatusResult.Success) {

            val job = viewModelScope.launch(Dispatchers.IO) {
                result.value = tasksResult.data!!
            }
            if (job.isCompleted) {
                _dataLoading.value = false
            }
        } else {
            result.value = emptyList()
            _dataLoading.value = false
            if (items.value.isNullOrEmpty())
                showSnackbarMessage(R.string.loading_issues_error)

        }

        return result
    }


}