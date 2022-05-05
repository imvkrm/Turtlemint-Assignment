package com.vikram.turtlemintassignment.ui.issues

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.vikram.turtlemintassignment.data.FakeTestRepository
import com.vikram.turtlemintassignment.data.local.IssuesItems
import com.vikram.turtlemintassignment.getOrAwaitValueTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class IssuesViewModelTest {


    private lateinit var issuesViewModel: IssuesViewModel

    private lateinit var tasksRepository: FakeTestRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {

        tasksRepository = FakeTestRepository()
        val issue1 =
            IssuesItems(101, "name1", "title1", "des1", "avatar1", "date1", 1, "comment_url1")
        val issue2 =
            IssuesItems(102, "name2", "title2", "des2", "avatar2", "date2", 2, "comment_url2")
        val issue3 =
            IssuesItems(103, "name3", "title3", "des3", "avatar3", "date3", 3, "comment_url3")

        tasksRepository.addIssues(issue1, issue2, issue3)

        issuesViewModel = IssuesViewModel(tasksRepository)
    }

    @Test
    fun addNewTask_setsNewTaskEvent() {
        val issues =
            IssuesItems(101, "name1", "title1", "des1", "avatar1", "date1", 1, "comment_url1")

        val value = issuesViewModel.items.getOrAwaitValueTest()


        assertThat(value).contains(issues)

    }


}