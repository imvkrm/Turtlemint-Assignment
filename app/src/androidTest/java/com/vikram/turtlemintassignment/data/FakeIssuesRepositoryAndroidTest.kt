package com.vikram.turtlemintassignment.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.vikram.turtlemintassignment.data.local.CommentsItems
import com.vikram.turtlemintassignment.data.local.IssuesItems
import com.vikram.turtlemintassignment.other.StatusResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

class FakeIssuesRepositoryAndroidTest  :IssuesRepository{

    var issuesItemsServiceData: LinkedHashMap<Int, IssuesItems> = LinkedHashMap()

    private val observableIssuesItems = MutableLiveData<StatusResult<List<IssuesItems>>>()

    var commentsServiceData: LinkedHashMap<Int, CommentsItems> = LinkedHashMap()

    private val observableCommentsItems = MutableLiveData<StatusResult<List<CommentsItems>>>()

    override fun observeAllIssuesItems(): LiveData<StatusResult<List<IssuesItems>>> {
        runBlocking { refreshAllIssuesItems() }
        return observableIssuesItems
    }

    override fun observeIssuesItemByIssueNo(issueNo: Int): LiveData<StatusResult<IssuesItems>> {
        runBlocking { refreshAllIssuesItems() }
        return observableIssuesItems.map { issues ->
            when (issues) {
                is StatusResult.Loading -> StatusResult.Loading
                is StatusResult.Error -> StatusResult.Error(issues.exception)
                is StatusResult.Success -> {
                    val issue = issues.data.firstOrNull() { it.issueNo == issueNo }
                        ?: return@map StatusResult.Error(Exception("Not found"))
                    StatusResult.Success(issue)
                }
            }
        }
    }

    override suspend fun getAllIssuesItems(): StatusResult<List<IssuesItems>> {
        return StatusResult.Success(issuesItemsServiceData.values.toList())
    }

    private fun getComments(): StatusResult<List<CommentsItems>> {
        return StatusResult.Success(commentsServiceData.values.toList())
    }

    override suspend fun getIssuesItemByIssueNo(issueNo: Int): StatusResult<IssuesItems> {
        issuesItemsServiceData[issueNo]?.let {
            return StatusResult.Success(it)
        }
        return StatusResult.Error(Exception("Could not find task"))
    }

    override suspend fun refreshAllIssuesItems() {
        observableIssuesItems.value = getAllIssuesItems()
    }

    override suspend fun refreshCommentsItems(issueNo: Int) {
        getComments()
    }

    override suspend fun saveTask(issuesItems: IssuesItems) {
        issuesItemsServiceData[issuesItems.issueNo] = issuesItems
    }

    override suspend fun deleteAllTasks() {
        issuesItemsServiceData.clear()
        refreshAllIssuesItems()
    }

    override fun clearAllComments() {
        commentsServiceData.clear()
        observableIssuesItems.postValue(null)
    }

    override fun observeAllCommentsItems(): LiveData<StatusResult<List<CommentsItems>>?> {
        observableCommentsItems.value=  getComments()
        return observableCommentsItems
    }

    override fun getAllCommentsItems(): StatusResult<List<CommentsItems>> {
        return getComments()
    }

    fun addIssues(vararg issuesItems: IssuesItems) {
        for (issueItem in issuesItems) {
            issuesItemsServiceData[issueItem.issueNo] = issueItem
        }
        runBlocking { refreshAllIssuesItems() }
    }

    fun addComments(vararg commentsItems: CommentsItems) {
        for (commentsItem in commentsItems) {
            commentsServiceData[commentsItem.id] = commentsItem
        }
        getComments()
    }


}