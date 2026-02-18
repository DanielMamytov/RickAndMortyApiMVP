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
    private var updateTask: Call<TaskItem>? = null
    private var patchTask: Call<TaskItem>? = null
    private var deleteTask: Call<Unit>? = null

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
                p0: Call<TaskItem?>,
                p1: Response<TaskItem?>
            ) {
                Log.e("DATA", "onResponse: ${p1.isSuccessful}")
            }

            override fun onFailure(
                p0: Call<TaskItem?>,
                p1: Throwable
            ) {
                view?.showError(p1.message ?: "Unknown error")

            }

        })
    }



    override fun onCreateClicked() {
        view?.navigateToCreate()
    }

    fun updateTask(task: TaskItem) {
        updateTask = ApiClient.characterApi.updateTask(task.id, task)

        updateTask?.enqueue(object : Callback<TaskItem> {
            override fun onResponse(call: Call<TaskItem>, response: Response<TaskItem>) {
                if (!response.isSuccessful) {
                    view?.showError("Problem: ${response.code()}")
                    return
                }
                Log.d("DATA", "PUT success: ${response.body()}")
            }

            override fun onFailure(call: Call<TaskItem>, t: Throwable) {
                view?.showError(t.message ?: "Unknown error")
            }
        })
    }

    fun patchTask(taskId: String, title: String? = null, description: String? = null) {
        val patchBody = buildMap<String, String> {
            title?.let { put("title", it) }
            description?.let { put("description", it) }
        }

        if (patchBody.isEmpty()) {
            view?.showError("Nothing to patch")
            return
        }

        patchTask = ApiClient.characterApi.patchTask(taskId, patchBody)

        patchTask?.enqueue(object : Callback<TaskItem> {
            override fun onResponse(call: Call<TaskItem>, response: Response<TaskItem>) {
                if (!response.isSuccessful) {
                    view?.showError("Problem: ${response.code()}")
                    return
                }
                Log.d("DATA", "PATCH success: ${response.body()}")
            }

            override fun onFailure(call: Call<TaskItem>, t: Throwable) {
                view?.showError(t.message ?: "Unknown error")
            }
        })
    }

    fun deleteTask(taskId: String) {
        deleteTask = ApiClient.characterApi.deleteTask(taskId)

        deleteTask?.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    view?.showError("Problem: ${response.code()}")
                    return
                }
                Log.d("DATA", "DELETE success for id=$taskId")
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                view?.showError(t.message ?: "Unknown error")
            }
        })
    }
}
