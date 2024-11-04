package com.example.testing_day1.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.testing_day1.MainCoroutineRule
import com.example.testing_day1.data.Result
import com.example.testing_day1.data.Task
import com.example.testing_day1.data.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4:: class)
@MediumTest
class TasksLocalDataSourceTest {

    private lateinit var localDataSource: TasksLocalDataSource

    @get : Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get : Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get : Rule
    var databaseRule = DatabaseRule()


    @Before
    fun setup() {
        localDataSource = TasksLocalDataSource(
            databaseRule.database.taskDao()
        )
    }


    @Test
    fun saveTaskGetTask_task1_returnSameTask() = runTest{
        // Arrange
        val task2 = Task(title = "Task2", description = "description", isCompleted = false)
        localDataSource.saveTask(task2)

        // Act
        val result = localDataSource.getTask(task2.id)

        // Assert
        assertThat(result.succeeded , `is`(true))
        result as Result.Success
        assertThat(result.data, `is`(task2))
    }

    @Test
    fun completeTask_task1_retrievedTaskIsCompleted() = runTest{
        // Arrange
        val task2 = Task(title = "Task2", description = "description", isCompleted = false)

        localDataSource.saveTask(task2)
        localDataSource.completeTask(task2)

        // Act
        val result = localDataSource.getTask(task2.id)

        // Assert
        assertThat(result.succeeded , `is`(true))
        result as Result.Success
        assertThat(result.data.isCompleted, `is`(true))
    }

}