package com.example.feedup.presentation.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.feedup.R
import com.example.feedup.data.network.ApiClient
import com.example.feedup.databinding.FragmentDetailsBinding
import com.example.feedup.model.TaskItem
import com.example.feedup.presentation.feed.CharacterContract
import com.example.feedup.presentation.feed.CharactersPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsFragment : Fragment(R.layout.fragment_details), CharacterContract.View {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val presenter = CharactersPresenter()

    private var taskId: String = ""
    private var initialTitle: String = ""
    private var initialDescription: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailsBinding.bind(view)
        presenter.attach(this)

        taskId = arguments?.getString("postId").orEmpty()

        loadTask()

        binding.buttonSave.setOnClickListener {
            presenter.onDetailsSaveClicked(
                taskId = taskId,
                oldTitle = initialTitle,
                oldDescription = initialDescription,
                newTitle = binding.editTitle.text.toString(),
                newDescription = binding.editDescription.text.toString()
            )
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        presenter.detach()
        _binding = null
        super.onDestroyView()
    }

    private fun loadTask() {
        if (taskId.isBlank()) {
            showError("Task id is empty")
            return
        }

        ApiClient.characterApi.getTaskById(taskId).enqueue(object : Callback<TaskItem> {
            override fun onResponse(call: Call<TaskItem>, response: Response<TaskItem>) {
                if (!response.isSuccessful) {
                    showError("Problem: ${response.code()}")
                    return
                }

                val task = response.body() ?: return
                initialTitle = task.title
                initialDescription = task.description

                binding.editTitle.setText(task.title)
                binding.editDescription.setText(task.description)
            }

            override fun onFailure(call: Call<TaskItem>, t: Throwable) {
                showError(t.message ?: "Unknown error")
            }
        })
    }

    override fun showLoading() = Unit

    override fun showCharacters(posts: List<TaskItem>) = Unit

    override fun showEmpty() = Unit

    override fun showError(message: String) = Unit

    override fun navigateToDetails(postId: String) = Unit

    override fun navigateToCreate() = Unit
}
