package com.example.feedup.presentation.feed

import com.example.feedup.model.Result

interface CharacterContract {

    interface View{
        fun showLoading()
        fun showCharacters(posts: List<Result>)
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
        fun onPostClicked(post: Result)
        fun onCreateClicked()
    }
}