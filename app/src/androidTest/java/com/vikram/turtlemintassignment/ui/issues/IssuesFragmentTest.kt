package com.vikram.turtlemintassignment.ui.issues

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.vikram.turtlemintassignment.R
import com.vikram.turtlemintassignment.ServiceLocator
import com.vikram.turtlemintassignment.TurtlemintApplication
import com.vikram.turtlemintassignment.data.FakeIssuesRepositoryAndroidTest
import com.vikram.turtlemintassignment.data.IssuesRepository
import com.vikram.turtlemintassignment.data.local.IssuesItems
import com.vikram.turtlemintassignment.other.StatusResult
import com.vikram.turtlemintassignment.other.succeeded
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class IssuesFragmentTest {

    private lateinit var repository: IssuesRepository

    @Before
    fun initRepository() {
        repository = FakeIssuesRepositoryAndroidTest()
        ServiceLocator.tasksRepository = repository
    }

    @After
    fun cleanupDb() = runTest {
        ServiceLocator.resetRepository()
    }


    @Test
    fun checkIfIssuesItemsAreVisible() = runTest() {


        repository.saveTask(
            IssuesItems(
                101,
                "name1",
                "title1",
                "des1",
                "avatar1",
                "date1",
                1,
                "comment_url1"
            )
        )

        repository.saveTask(
            IssuesItems(
                102,
                "name2",
                "title2",
                "des2",
                "avatar2",
                "date2",
                2,
                "comment_url2"
            )
        )

        repository.saveTask(
            IssuesItems(
                103,
                "name3",
                "title3",
                "des3",
                "avatar3",
                "date3",
                3,
                "comment_url3"
            )
        )


        launchFragmentInContainer<IssuesFragment>(Bundle(), R.style.Theme_TurtlemintAssignment)

        Espresso.onView(withId(R.id.containerIssues))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        if (repository.getAllIssuesItems() is StatusResult.Success) {
            Log.e(
                "#########",
                "" + (repository.getAllIssuesItems() as StatusResult.Success<List<IssuesItems>>).data[0].title
            )

        }
    }
}

//
//    @Test
//    fun clickIssueItem_navigateToInfoFragment() = runBlocking {
//
//        val navController =
//            Mockito.mock(NavController::class.java)
//        repository.updateIssuesItemsFromRemote()
//        var testViewModel: IssuesViewModel? = null
//        launchFragmentInHiltContainer<IssuesFragment>(fragmentFactory = testFragmentFactory) {
//            testViewModel = viewModel
//            testViewModel?.fetchDataFromRepository()
//            Navigation.setViewNavController(requireView(), navController)
//        }
//
//        Espresso.onView(withId(R.id.containerIssues)).perform(ViewActions.click())
//
//        Mockito.verify(navController)
//            .navigate(IssuesFragmentDirections.actionIssuesFragmentToInfoFragment(1))
//
//
//    }
