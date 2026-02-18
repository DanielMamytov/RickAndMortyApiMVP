package com.example.feedup.presentation.feed

import com.example.feedup.model.TaskItem

interface CharacterContract {

    interface View{
        fun showLoading()
        fun showCharacters(posts: List<TaskItem>)
        fun showEmpty()
        fun showError(message: String)
        fun navigateToDetails(postId: Int)
        fun navigateToCreate()
    }

    interface Presenter {
        fun attach(view:View)
        fun detach()
        fun loadCharacters()
        fun onRetryClicked()
        fun onPostClicked(post: TaskItem)

        fun createTask(task: TaskItem)
        fun updateTaskPut(id: String, task: TaskItem)
        fun updateTaskPatch(id: String, fields: Map<String, String>)
        fun deleteTask(id: String)
        fun onSaveClicked(title: String, description: String)

        fun onCreateClicked()
    }
}