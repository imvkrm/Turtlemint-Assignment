package com.vikram.turtlemintassignment.data

import androidx.lifecycle.LiveData
import com.vikram.turtlemintassignment.data.local.CommentsItems
import com.vikram.turtlemintassignment.data.local.IssuesItems
import com.vikram.turtlemintassignment.other.StatusResult

interface IssuesRepository {

    fun observeAllIssuesItems(): LiveData<StatusResult<List<IssuesItems>>>

    fun observeIssuesItemByIssueNo(issueNo:Int): LiveData<StatusResult<IssuesItems>>

    suspend  fun getAllIssuesItems(): StatusResult<List<IssuesItems>>

    suspend fun getIssuesItemByIssueNo(issueNo: Int): StatusResult<IssuesItems>

    suspend fun refreshAllIssuesItems()

    suspend fun refreshCommentsItems(issueNo: Int)

    suspend fun saveTask(issuesItems: IssuesItems)

    suspend fun deleteAllTasks()

    fun clearAllComments()
     fun observeAllCommentsItems():LiveData<StatusResult<List<CommentsItems>>?>?

    fun getAllCommentsItems():StatusResult<List<CommentsItems>>?
}