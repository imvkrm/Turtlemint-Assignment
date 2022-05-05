package com.vikram.turtlemintassignment.data

import com.google.common.truth.Truth.assertThat
import com.vikram.turtlemintassignment.data.local.CommentsItems
import com.vikram.turtlemintassignment.data.local.IssuesItems
import com.vikram.turtlemintassignment.other.StatusResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class DefaultIssuesRepositoryTest {


    private val issue1 =
        IssuesItems(101, "name1", "title1", "des1", "avatar1", "date1", 1, "comment_url1")
    private val issue2 =
        IssuesItems(102, "name2", "title2", "des2", "avatar2", "date2", 2, "comment_url2")
    private val issue3 =
        IssuesItems(103, "name3", "title3", "des3", "avatar3", "date3", 3, "comment_url3")

    private val comment1 = CommentsItems(1, "name1", "des1", "avatar1", "date1")
    private val comment2 = CommentsItems(2, "name2", "des2", "avatar2", "date2")
    private val comment3 = CommentsItems(3, "name3", "des3", "avatar3", "date3")


    private val remoteIssues = listOf(issue1, issue2).sortedBy { it.id }
    private val localIssues = listOf(issue3).sortedBy { it.id }

    private val remoteComments = listOf(comment1, comment2, comment3).sortedBy { it.id }

    private lateinit var issuesRemoteDataSource: FakeDataSource
    private lateinit var issuesLocalDataSource: FakeDataSource

    private lateinit var issuesRepository: DefaultIssuesRepository

    @Before
    fun createRepository() {
        issuesRemoteDataSource = FakeDataSource(remoteIssues.toMutableList(),remoteComments.toMutableList())
        issuesLocalDataSource = FakeDataSource(localIssues.toMutableList())
        issuesRepository = DefaultIssuesRepository(
            issuesRemoteDataSource, issuesLocalDataSource, Dispatchers.Unconfined
        )
    }

    @Test
    fun getIssues_requestsAllIssuesFromLocalDataSource() = runTest {

        val issues = issuesRepository.getAllIssuesItems() as StatusResult.Success
        val issuesData = issues.data

        assertThat(issuesData).isEqualTo(localIssues)
    }

    @Test
    fun getComments_requestsAllCommnetsFromDataSource() = runTest {

        val comments = issuesRepository.getAllCommentsItems() as StatusResult.Success
        val commentsData = comments.data

        assertThat(commentsData).isEqualTo(remoteComments)
    }
}