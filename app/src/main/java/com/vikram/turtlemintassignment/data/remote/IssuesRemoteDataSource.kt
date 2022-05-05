/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vikram.turtlemintassignment.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.vikram.turtlemintassignment.data.IssuesDataSource
import com.vikram.turtlemintassignment.data.local.CommentsItems
import com.vikram.turtlemintassignment.data.local.IssuesItems
import com.vikram.turtlemintassignment.data.remote.response.CommentApiResponse
import com.vikram.turtlemintassignment.data.remote.response.IssuesApiResponseItem
import com.vikram.turtlemintassignment.other.StatusResult
import com.vikram.turtlemintassignment.other.StatusResult.Error
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of the data source that adds a latency simulating network.
 */
class IssuesRemoteDataSource internal constructor(
    private val issuesApi: IssuesApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IssuesDataSource {


    private val observableTasks = MutableLiveData<StatusResult<List<IssuesItems>>>()

    private val observableComments = MutableLiveData<StatusResult<List<CommentsItems>>?>()

    private suspend fun getIssues(): StatusResult<List<IssuesItems>> = withContext(ioDispatcher) {
        return@withContext try {
            val issuesItems = issuesApi.getAllIssuesFromApi()
            databaseModel(issuesItems)
        } catch (e: Exception) {
            Error(e)
        }
    }


    private fun databaseModel(issuesApiResponseItemList: List<IssuesApiResponseItem>): StatusResult<List<IssuesItems>> {
        return StatusResult.Success(issuesApiResponseItemList.map {
            IssuesItems(
                id = it.id!!,
                name = it.user.login ?: "",
                title = it.title ?: "",
                description = it.body ?: "",
                avatar = it.user.avatar_url ?: "",
                date = it.updated_at ?: "",
                issueNo = it.number!!,
                commentUrl = it.comments_url ?: ""

            )
        })
    }

    override fun observeAllIssuesItems(): LiveData<StatusResult<List<IssuesItems>>> {
        return observableTasks
    }

    override fun observeIssuesItemByIssueNo(issueNo: Int): LiveData<StatusResult<IssuesItems>> {
        return observableTasks.map { issues ->
            when (issues) {
                is StatusResult.Loading -> StatusResult.Loading
                is Error -> Error(issues.exception)
                is StatusResult.Success -> {
                    val issues = issues.data.firstOrNull() { it.issueNo == issueNo }
                        ?: return@map Error(Exception("Not found"))
                    StatusResult.Success(issues)
                }
            }
        }
    }

    override suspend fun getAllIssuesItems(): StatusResult<List<IssuesItems>> {
      return getIssues()
    }

    override suspend fun getIssuesItemByIssueNo(issueNo: Int): StatusResult<IssuesItems> {
       return  getIssuesItemByIssueNo(issueNo)
    }

    override suspend fun refreshAllIssuesItems() {
        observableTasks.value = getIssues()
    }

    override suspend fun refreshCommentsItems(issueNo: Int) {
        observableComments.value=null
        observableComments.value = getComments(issueNo)

    }


    override suspend fun saveTask(issuesItems: IssuesItems) {
        //No-OP
    }

    override suspend fun deleteAllTasks() {
        //No-OP
    }

    override fun clearAllComments() {
        observableComments.postValue(null)
    }


    private suspend fun getComments(issueNo: Int): StatusResult<List<CommentsItems>> =
        withContext(ioDispatcher) {
            return@withContext try {
                databaseCommentModel(issuesApi.getAllCommentFromApi(issueNo))
            } catch (e: Exception) {
                Error(e)
            }
        }


    override fun observeAllCommentsItems(): MutableLiveData<StatusResult<List<CommentsItems>>?> {
        return observableComments
    }

    override fun getAllCommentsItems(): StatusResult<List<CommentsItems>>? {
        return observableComments.value
    }

    private fun databaseCommentModel(commentApiResponseItemList: List<CommentApiResponse>): StatusResult<List<CommentsItems>> {

        return StatusResult.Success(commentApiResponseItemList.map {
            CommentsItems(
                id = it.id,
                name = it.user.login ?: "",
                description = it.body ?: "",
                avatar = it.user.avatar_url ?: "",
                date = it.updated_at ?: ""
            )
        })
    }
}
