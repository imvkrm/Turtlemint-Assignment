package com.vikram.turtlemintassignment.ui.info

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.vikram.turtlemintassignment.R
import com.vikram.turtlemintassignment.data.IssuesRepository
import com.vikram.turtlemintassignment.data.local.CommentsItems
import com.vikram.turtlemintassignment.data.local.IssuesItems
import com.vikram.turtlemintassignment.other.Event
import com.vikram.turtlemintassignment.other.StatusResult
import kotlinx.coroutines.launch


class InfoViewModel(
    private val issuesRepository: IssuesRepository
) : ViewModel() {


    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText


    private fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = Event(message)
    }

    private val _issueNo = MutableLiveData<Int>()

    fun start(issueNo: Int) {

        if (_dataLoading.value == true || issueNo == _issueNo.value) {
            return
        }
        _issueNo.value = issueNo

    }



    private val _issuesItem = _issueNo.switchMap { issueNo ->
        issuesRepository.observeIssuesItemByIssueNo(issueNo).map { computeResult(it) }
    }
    val issuesItem: LiveData<IssuesItems?> = _issuesItem


    private fun computeResult(issuesResult: StatusResult<IssuesItems>): IssuesItems? {
        return if (issuesResult is StatusResult.Success) {
            issuesResult.data
        } else {
             showSnackbarMessage(R.string.loading_issues_error)
            null
        }
    }






    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private var _commentItems = _issueNo.switchMap { issueNo ->
        fetchDataFromRepository(issueNo)
    }

    private fun fetchDataFromRepository(issueNo: Int): LiveData<List<CommentsItems>> {
        _dataLoading.value = true
      val job=  viewModelScope.launch {
            issuesRepository.refreshCommentsItems(issueNo)

        }

      if(job.isCompleted) {
          _dataLoading.value = false
      }

        return issuesRepository.observeAllCommentsItems()!!.switchMap { filterComments(it) }

    }

    private fun filterComments(issuesResult: StatusResult<List<CommentsItems>>?): LiveData<List<CommentsItems>> {

        val result = MutableLiveData<List<CommentsItems>>()

        if (issuesResult is StatusResult.Success) {

            viewModelScope.launch {
                result.value = issuesResult.data!!
                _dataLoading.value = false
            }

        } else {
            result.value = emptyList()
            _dataLoading.value = false
        }


        return result
    }

    val commentItems: LiveData<List<CommentsItems>>
        get() = _commentItems

}