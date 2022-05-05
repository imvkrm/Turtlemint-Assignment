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
package com.vikram.turtlemintassignment.data.local


import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.vikram.turtlemintassignment.data.IssuesDataSource
import com.vikram.turtlemintassignment.other.StatusResult
import com.vikram.turtlemintassignment.other.StatusResult.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Concrete implementation of a data source as a db.
 */
class IssuesLocalDataSource internal constructor(
    private val issuesDao: IssuesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IssuesDataSource {

    override fun observeAllIssuesItems(): LiveData<StatusResult<List<IssuesItems>>> {
        return issuesDao.observeAllIssuesItems().map {
            Success(it)
        }
    }

    override fun observeIssuesItemByIssueNo(issueNo: Int): LiveData<StatusResult<IssuesItems>> {
        return issuesDao.observeIssuesItemByIssueNo(issueNo).map {
            Success(it)
        }
    }

    override suspend fun getAllIssuesItems(): StatusResult<List<IssuesItems>> {
        return Success(issuesDao.getAllIssuesItems())
    }

    override suspend fun getIssuesItemByIssueNo(issueNo: Int): StatusResult<IssuesItems> {
        return Success(issuesDao.getIssuesItemByIssueNo(issueNo))
    }

    override suspend fun refreshAllIssuesItems() {
        //NO-OP
    }

    override suspend fun refreshCommentsItems(issueNo: Int) {
        //NO-OP
    }


    override suspend fun saveTask(issuesItems: IssuesItems) = withContext(ioDispatcher) {
        issuesDao.insertIssuesItems(issuesItems)
    }

    override suspend fun deleteAllTasks() = withContext(ioDispatcher) {
        issuesDao.deleteAllIssuesItems()
    }

    override fun clearAllComments() {
        //NO-OP
    }

    override fun observeAllCommentsItems(): LiveData<StatusResult<List<CommentsItems>>?>? {
        //NO-OP
        return null
    }

    override fun getAllCommentsItems(): StatusResult<List<CommentsItems>>? {
        //NO-OP
        return null
    }
}
