package com.example.feedup.presentation.feed

import android.util.Log
import com.example.feedup.data.network.ApiClient
import com.example.feedup.model.TaskItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharactersPresenter : CharacterContract.Presenter {

    private var view: CharacterContract.View? = null

    private var currentCall: Call<List<TaskItem>>? = null

    private var createTask: Call<TaskItem>? = null
    private var putTask: Call<TaskItem>? = null
    private var patchTask: Call<TaskItem>? = null
    private var deleteTaskCall: Call<Void>? = null

    override fun attach(view: CharacterContract.View) {
        this.view = view
    }

    override fun detach() {
        view = null
    }

    override fun loadCharacters() {
        view?.showLoading()

        currentCall = ApiClient.characterApi.getAllCharacters()
        currentCall?.enqueue(object : Callback<List<TaskItem>> {

            override fun onResponse(
                call: Call<List<TaskItem>>,
                response: Response<List<TaskItem>>
            ) {
                if (response.isSuccessful) {
                    val characters = response.body().orEmpty()
                    view?.showCharacters(characters)
                } else {
                    view?.showError("Problem: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<TaskItem>>, t: Throwable) {
                view?.showError(t.message ?: "Unknown error")
            }
        })
    }

    override fun onRetryClicked() {
        loadCharacters()
    }

    override fun onPostClicked(post: TaskItem) {
        view?.navigateToDetails(post.id.toInt())
    }
    override fun onSaveClicked(title: String, description: String) {
        if (title.isBlank() || description.isBlank()) {
            view?.showError("Fields must not be empty")
            return
        }

        val task = TaskItem(
            id = "",
            title = title,
            description = description
        )

        createTask(task)
    }

    override fun createTask(task: TaskItem) {
        createTask = ApiClient.characterApi.createTask(task)

        createTask?.enqueue(object : Callback<TaskItem> {
            override fun onResponse(
                call: Call<TaskItem>,
                response: Response<TaskItem>
            ) {
                if (response.isSuccessful) {
                    Log.d("API", "POST success: ${response.body()}")
                } else {
                    view?.showError("POST error: ${response.code()}")
                }
            }

            override fun onFailure(
                call: Call<TaskItem>,
                t: Throwable
            ) {
                view?.showError(t.message ?: "Unknown error")
            }
        })
    }

    override fun updateTaskPut(id: String, task: TaskItem) {
        putTask = ApiClient.characterApi.updateTaskPut(id, task)
        putTask?.enqueue(object : Callback<TaskItem> {
            override fun onResponse(call: Call<TaskItem>, response: Response<TaskItem>) {
                if (response.isSuccessful) {
                    Log.d("API", "PUT success: ${response.body()}")
                } else {
                    view?.showError("PUT error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TaskItem>, t: Throwable) {
                view?.showError(t.message ?: "Unknown error")
            }
        })
    }

    override fun updateTaskPatch(id: String, fields: Map<String, String>) {
        patchTask = ApiClient.characterApi.updateTaskPatch(id, fields)
        patchTask?.enqueue(object : Callback<TaskItem> {
            override fun onResponse(call: Call<TaskItem>, response: Response<TaskItem>) {
                if (response.isSuccessful) {
                    Log.d("API", "PATCH success: ${response.body()}")
                } else {
                    view?.showError("PATCH error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TaskItem>, t: Throwable) {
                view?.showError(t.message ?: "Unknown error")
            }
        })
    }

    override fun deleteTask(id: String) {
        deleteTaskCall = ApiClient.characterApi.deleteTask(id)
        deleteTaskCall?.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("API", "DELETE success for id=$id")
                } else {
                    view?.showError("DELETE error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                view?.showError(t.message ?: "Unknown error")
            }
        })
    }


    override fun onCreateClicked() {
        view?.navigateToCreate()
    }
}