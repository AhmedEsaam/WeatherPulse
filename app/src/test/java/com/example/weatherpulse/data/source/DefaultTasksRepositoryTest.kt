package com.example.testing_day1.data.source

import com.example.testing_day1.MainCoroutineRule
import com.example.testing_day1.data.Result
import com.example.testing_day1.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.jetbrains.annotations.ApiStatus.Experimental
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description


class DefaultTasksRepositoryTest {

//@ExperimentalCoroutinesApi
//val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
//
//@ExperimentalCoroutinesApi
//@Before
//fun setupDispatcher() {
//    Dispatchers.setMain(testDispatcher)
//}
//
//@ExperimentalCoroutinesApi
//@After
//fun tearDownDispatcher() {
//    Dispatchers.resetMain()
//    testDispatcher.cleanupTestCoroutines()
//}

    @ExperimentalCoroutinesApi
    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun getTasks_forceUpdateFalseLocalNotEmpty_LocalTasks() = runTest {
        // Arrange      : Parameters: Task Lists
        val task1 = Task(title = "Go to China")
        val task2 = Task(title = "Go to Japan")
        val task3 = Task(title = "Build the Great Pyramid of Giza")
        val task4 = Task(title = "Deliver Android Kotlin Project")
        val remoteTasks = mutableListOf(task1, task2)
        val localTasks = mutableListOf(task3, task4)
        //              : Object:     Create DefaultRepository object
        val tasksRepository = DefaultTasksRepository(
            tasksRemoteDataSource = FakeDataSource(remoteTasks),
            tasksLocalDataSource = FakeDataSource(localTasks)
        )

        // Act          : Call getTasks(boolean) Method
        val result = tasksRepository.getTasks(forceUpdate = false) as Result.Success

        // Assert       : Assert
        assertThat(result.data, `is`(localTasks))

    }
}