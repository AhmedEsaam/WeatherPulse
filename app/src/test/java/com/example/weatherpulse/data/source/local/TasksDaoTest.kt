package com.example.testing_day1.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.testing_day1.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4:: class)
@SmallTest
class TasksDaoTest {

    private lateinit var tasksDao: TasksDao

    @get : Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get : Rule
    var databaseRule = DatabaseRule()


    @Before
    fun setup() {
        tasksDao = databaseRule.database.taskDao()
    }

    @Test
    fun insertTaskGetTaskById_task1_returnSameTask() = runTest {
        // Arrange  : Create [task1] and insert it into database
        val task1 = Task(title = "Attend the Android Testing Lecture", "description", isCompleted = true)
        tasksDao.insertTask(task1)

        // Act
        val returnedTask = tasksDao.getTaskById(task1.id)

        // Assert
        assertThat(returnedTask as Task, notNullValue())
        assertThat(returnedTask, `is`(task1))
    }

    @Test
    fun updateTaskGetTaskById_task1_returnUpdatedTask() = runTest {
        // Arrange
        val task1 = Task(title = "Attend the Android Testing Lecture", "description", isCompleted = true)
        tasksDao.insertTask(task1)
        task1.title = "Deliver Android Kotlin Project"
//        task1 = Task(title = "Deliver Android Kotlin Project", "description", isCompleted = false, id = task1.id)
        tasksDao.updateTask(task1)


        // Act
        val returnedTask = tasksDao.getTaskById(task1.id)

        // Assert
        assertThat(returnedTask as Task, notNullValue())
        assertThat(returnedTask.title, `is`("Deliver Android Kotlin Project"))
        assertThat(returnedTask, `is`(task1))
    }
}