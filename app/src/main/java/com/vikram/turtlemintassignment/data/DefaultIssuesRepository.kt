package com.vikram.turtlemintassignment.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.vikram.turtlemintassignment.data.local.CommentsItems
import com.vikram.turtlemintassignment.data.local.IssuesItems
import com.vikram.turtlemintassignment.other.StatusResult
import kotlinx.coroutines.*
import javax.inject.Inject

class DefaultIssuesRepository @Inject constructor(
    private val issuesRemoteDataSource: IssuesDataSource,
    private val issuesLocalDataSource: IssuesDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
):IssuesRepository {
    override fun observeAllIssuesItems(): LiveData<StatusResult<List<IssuesItems>>> {
        return issuesLocalDataSource.observeAllIssuesItems()
    }

    override fun observeIssuesItemByIssueNo(issueNo: Int): LiveData<StatusResult<IssuesItems>> {
        return issuesLocalDataSource.observeIssuesItemByIssueNo(issueNo)
    }

    override suspend fun getAllIssuesItems(): StatusResult<List<IssuesItems>> {
        return issuesLocalDataSource.getAllIssuesItems()
    }

    override suspend fun getIssuesItemByIssueNo(issueNo: Int): StatusResult<IssuesItems> {
        return issuesLocalDataSource.getIssuesItemByIssueNo(issueNo)
    }

    override suspend fun refreshAllIssuesItems() {
        updateIssuesFromRemoteDataSource()
    }

    override suspend fun refreshCommentsItems(issueNo: Int) {
        updateIssueFromRemoteDataSource(issueNo)
    }

    override suspend fun saveTask(issuesItems: IssuesItems) {
        coroutineScope {
            launch { issuesRemoteDataSource.saveTask(issuesItems) }
            launch { issuesLocalDataSource.saveTask(issuesItems) }
        }
    }

    override suspend fun deleteAllTasks() {
        withContext(ioDispatcher) {
            coroutineScope {
              //  launch { issuesRemoteDataSource.deleteAllTasks() }
                launch { issuesLocalDataSource.deleteAllTasks() }
            }
        }
    }

    override fun clearAllComments() {
        issuesRemoteDataSource.clearAllComments()
    }

    override  fun observeAllCommentsItems(): LiveData<StatusResult<List<CommentsItems>>?> {
        return issuesRemoteDataSource.observeAllCommentsItems()!!
    }

    override fun getAllCommentsItems(): StatusResult<List<CommentsItems>>? {
      return  issuesRemoteDataSource.getAllCommentsItems()
    }

    private suspend fun updateIssueFromRemoteDataSource(issueNo: Int) {
        issuesRemoteDataSource.refreshCommentsItems(issueNo)

    }

    private suspend fun updateIssuesFromRemoteDataSource() {
        issuesRemoteDataSource.refreshAllIssuesItems()
        val remoteIssues = issuesRemoteDataSource.observeAllIssuesItems().value


        if (remoteIssues is StatusResult.Success) {
            // Real apps might want to do a proper sync.
            issuesLocalDataSource.deleteAllTasks()
            remoteIssues.data.forEach { issues ->
                issuesLocalDataSource.saveTask(issues)
            }
        } else if (remoteIssues is StatusResult.Error) {
            Log.e("##########","error")
          //  throw remoteIssues.exception

        }
    }

}