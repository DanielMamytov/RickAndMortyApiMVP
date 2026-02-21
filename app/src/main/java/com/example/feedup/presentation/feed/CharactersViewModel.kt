package com.example.feedup.presentation.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.feedup.data.network.ApiClient
import com.example.feedup.model.TaskItem
import com.example.feedup.model.TaskPatchRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharactersViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _characters = MutableLiveData<List<TaskItem>>(emptyList())
    val characters: LiveData<List<TaskItem>> = _characters

    private val _isEmpty = MutableLiveData(false)
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _navigateToDetails = MutableLiveData<String?>(null)
    val navigateToDetails: LiveData<String?> = _navigateToDetails

    private val _navigateToCreate = MutableLiveData(false)
    val navigateToCreate: LiveData<Boolean> = _navigateToCreate

    private val _selectedTask = MutableLiveData<TaskItem?>(null)
    val selectedTask: LiveData<TaskItem?> = _selectedTask

    private val _actionCompleted = MutableLiveData(false)
    val actionCompleted: LiveData<Boolean> = _actionCompleted

    private var currentCall: Call<List<TaskItem>>? = null
    private var createTaskCall: Call<TaskItem>? = null
    private var updateTaskCall: Call<TaskItem>? = null
    private var patchTaskCall: Call<TaskItem>? = null
    private var deleteTaskCall: Call<Unit>? = null
    private var taskByIdCall: Call<TaskItem>? = null

    fun loadCharacters() {
        _isLoading.value = true
        _errorMessage.value = null

        currentCall = ApiClient.characterApi.getAllCharacters()
        currentCall?.enqueue(object : Callback<List<TaskItem>> {
            override fun onResponse(call: Call<List<TaskItem>>, response: Response<List<TaskItem>>) {
                _isLoading.value = false
                if (!response.isSuccessful) {
                    _errorMessage.value = "Problem: ${response.code()}"
                    return
                }

                val items = response.body().orEmpty()
                _characters.value = items
                _isEmpty.value = items.isEmpty()
            }

            override fun onFailure(call: Call<List<TaskItem>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message ?: "Unknown error"
            }
        })
    }

    fun onRetryClicked() = loadCharacters()

    fun onPostClicked(post: TaskItem) {
        _navigateToDetails.value = post.id
    }

    fun onPostLongClicked(post: TaskItem) {
        deleteTask(post.id)
    }

    fun onCreateClicked() {
        _navigateToCreate.value = true
    }

    fun onSaveClicked(title: String, description: String) {
        if (title.isBlank() || description.isBlank()) {
            _errorMessage.value = "Fields must not be empty"
            return
        }

        createTask(TaskItem(id = "", title = title, description = description))
    }

    private fun createTask(task: TaskItem) {
        createTaskCall = ApiClient.characterApi.createTask(task)
        createTaskCall?.enqueue(object : Callback<TaskItem> {
            override fun onResponse(call: Call<TaskItem>, response: Response<TaskItem>) {
                if (!response.isSuccessful) {
                    _errorMessage.value = "Problem: ${response.code()}"
                    return
                }
                _actionCompleted.value = true
                loadCharacters()
            }

            override fun onFailure(call: Call<TaskItem>, t: Throwable) {
                _errorMessage.value = t.message ?: "Unknown error"
            }
        })
    }

    fun loadTask(taskId: String) {
        if (taskId.isBlank()) {
            _errorMessage.value = "Task id is empty"
            return
        }

        taskByIdCall = ApiClient.characterApi.getTaskById(taskId)
        taskByIdCall?.enqueue(object : Callback<TaskItem> {
            override fun onResponse(call: Call<TaskItem>, response: Response<TaskItem>) {
                if (!response.isSuccessful) {
                    _errorMessage.value = "Problem: ${response.code()}"
                    return
                }
                _selectedTask.value = response.body()
            }

            override fun onFailure(call: Call<TaskItem>, t: Throwable) {
                _errorMessage.value = t.message ?: "Unknown error"
            }
        })
    }

    fun onDetailsSaveClicked(
        taskId: String,
        oldTitle: String,
        oldDescription: String,
        newTitle: String,
        newDescription: String
    ) {
        val titleChanged = oldTitle != newTitle
        val descriptionChanged = oldDescription != newDescription

        if (!titleChanged && !descriptionChanged) {
            _errorMessage.value = "Nothing changed"
            return
        }

        if (titleChanged && descriptionChanged) {
            updateTask(TaskItem(id = taskId, title = newTitle, description = newDescription))
            return
        }

        patchTask(
            taskId = taskId,
            title = newTitle.takeIf { titleChanged },
            description = newDescription.takeIf { descriptionChanged }
        )
    }

    private fun updateTask(task: TaskItem) {
        updateTaskCall = ApiClient.characterApi.updateTask(task.id, task)
        updateTaskCall?.enqueue(object : Callback<TaskItem> {
            override fun onResponse(call: Call<TaskItem>, response: Response<TaskItem>) {
                if (!response.isSuccessful) {
                    _errorMessage.value = "Problem: ${response.code()}"
                    return
                }
                _actionCompleted.value = true
                loadCharacters()
            }

            override fun onFailure(call: Call<TaskItem>, t: Throwable) {
                _errorMessage.value = t.message ?: "Unknown error"
            }
        })
    }

    private fun patchTask(taskId: String, title: String? = null, description: String? = null) {
        if (title == null && description == null) {
            _errorMessage.value = "Nothing to patch"
            return
        }

        patchTaskCall = ApiClient.characterApi.patchTask(
            taskId,
            TaskPatchRequest(title = title, description = description)
        )

        patchTaskCall?.enqueue(object : Callback<TaskItem> {
            override fun onResponse(call: Call<TaskItem>, response: Response<TaskItem>) {
                if (!response.isSuccessful) {
                    _errorMessage.value = "Problem: ${response.code()}"
                    return
                }
                _actionCompleted.value = true
                loadCharacters()
            }

            override fun onFailure(call: Call<TaskItem>, t: Throwable) {
                _errorMessage.value = t.message ?: "Unknown error"
            }
        })
    }

    private fun deleteTask(taskId: String) {
        deleteTaskCall = ApiClient.characterApi.deleteTask(taskId)
        deleteTaskCall?.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    _errorMessage.value = "Problem: ${response.code()}"
                    return
                }
                loadCharacters()
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _errorMessage.value = t.message ?: "Unknown error"
            }
        })
    }

    fun onDetailsConsumed() {
        _navigateToDetails.value = null
    }

    fun onCreateConsumed() {
        _navigateToCreate.value = false
    }

    fun onErrorConsumed() {
        _errorMessage.value = null
    }

    fun onActionCompletedConsumed() {
        _actionCompleted.value = false
    }

    override fun onCleared() {
        currentCall?.cancel()
        createTaskCall?.cancel()
        updateTaskCall?.cancel()
        patchTaskCall?.cancel()
        deleteTaskCall?.cancel()
        taskByIdCall?.cancel()
        super.onCleared()
    }
}
