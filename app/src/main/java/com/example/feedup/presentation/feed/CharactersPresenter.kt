package com.example.feedup.presentation.feed

import android.util.Log
import android.widget.Toast
import com.example.feedup.data.network.ApiClient
import com.example.feedup.model.TaskItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class CharactersPresenter : CharacterContract.Presenter {

    private var view: CharacterContract.View? = null

    private var currentCall: Call<List<TaskItem>>? = null

    private var createTask: Call<TaskItem>? = null

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
}