package com.vikram.issuesitemstesting

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/*
* Provides CoroutineDispatcher to Test in case we are try to use coroutine in test and that doesn't have access to main CoroutineDispatcher
* */
@ExperimentalCoroutinesApi
class MainCoroutineRule(
    private val dispatcher: CoroutineDispatcher =TestCoroutineDispatcher()
):TestWatcher(),TestCoroutineScope by TestCoroutineScope(dispatcher) {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}