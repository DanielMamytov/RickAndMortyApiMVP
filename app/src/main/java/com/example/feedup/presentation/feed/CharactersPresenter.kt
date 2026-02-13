package com.example.feedup.presentation.feed

import com.example.feedup.data.network.ApiClient
import com.example.feedup.model.CharactersModel
import com.example.feedup.model.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharactersPresenter: CharacterContract.Presenter {

    private var view: CharacterContract.View? = null

    private var currentCall: Call<CharactersModel>? = null

    override fun attach(view: CharacterContract.View) {
        this.view = view
    }

    override fun detach() {
        view = null
    }

    override fun loadCharacters() {
        view?.showLoading()

        currentCall = ApiClient.characterApi.getAllCharacters()
        currentCall?.enqueue(object : Callback<CharactersModel>{
            override fun onResponse(
                call: Call<CharactersModel?>,
                response: Response<CharactersModel?>
            ) {
                if (response.isSuccessful){
                    val characters = response.body()?.results.orEmpty()
                    view?.showCharacters(characters)
                }else{
                    view?.showError("Problem: ${response.code()}")
                }
            }

            override fun onFailure(
                call: Call<CharactersModel?>,
                error: Throwable
            ) {
                if (!call.isCanceled){
                    view?.showError(error.message ?: "неизвестная ошибка ")
                }
            }

        })

    }

    override fun onRetryClicked() {
        loadCharacters()
    }

    override fun onPostClicked(post: Result) {
        view?.navigateToDetails(post.id)
    }

    override fun onCreateClicked() {
        view?.navigateToCreate()
    }
}