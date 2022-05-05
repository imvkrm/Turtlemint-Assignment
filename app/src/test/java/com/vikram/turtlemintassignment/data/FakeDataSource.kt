package com.vikram.turtlemintassignment.data

import androidx.lifecycle.LiveData
import com.vikram.turtlemintassignment.data.local.CommentsItems
import com.vikram.turtlemintassignment.data.local.IssuesItems
import com.vikram.turtlemintassignment.other.StatusResult

class FakeDataSource(var issuesItems: MutableList<IssuesItems>? = mutableListOf(),var commentsItems: MutableList<CommentsItems>? = mutableListOf()) : IssuesDataSource {
    override fun observeAllIssuesItems(): LiveData<StatusResult<List<IssuesItems>>> {
        TODO("Not yet implemented")
    }


    override fun observeIssuesItemByIssueNo(issueNo: Int): LiveData<StatusResult<IssuesItems>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllIssuesItems(): StatusResult<List<IssuesItems>> {
        issuesItems?.let { return StatusResult.Success(ArrayList(it)) }
        return StatusResult.Error(
            Exception("Issues not found")
        )
    }

    override suspend fun getIssuesItemByIssueNo(issueNo: Int): StatusResult<IssuesItems> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshAllIssuesItems() {
        TODO("Not yet implemented")
    }

    override suspend fun refreshCommentsItems(issueNo: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun saveTask(issuesItem: IssuesItems) {
        issuesItems?.add(issuesItem)
    }

    override suspend fun deleteAllTasks() {
        issuesItems?.clear()
    }

    override fun clearAllComments() {
        commentsItems?.clear()
    }

    override fun observeAllCommentsItems(): LiveData<StatusResult<List<CommentsItems>>?>? {
        TODO("Not yet implemented")
    }

    override fun getAllCommentsItems(): StatusResult<List<CommentsItems>> {

        commentsItems?.let { return StatusResult.Success(ArrayList(it)) }
        return StatusResult.Error(
            Exception("Comments not found")
        )
    }
}