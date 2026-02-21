package com.example.feedup.presentation.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.feedup.R
import com.example.feedup.databinding.FragmentDetailsBinding
import com.example.feedup.presentation.feed.CharactersViewModel

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharactersViewModel by viewModels()

    private var taskId: String = ""
    private var initialTitle: String = ""
    private var initialDescription: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailsBinding.bind(view)

        taskId = arguments?.getString("postId").orEmpty()
        observeViewModel()
        viewModel.loadTask(taskId)

        binding.buttonSave.setOnClickListener {
            viewModel.onDetailsSaveClicked(
                taskId = taskId,
                oldTitle = initialTitle,
                oldDescription = initialDescription,
                newTitle = binding.editTitle.text.toString(),
                newDescription = binding.editDescription.text.toString()
            )
        }
    }

    private fun observeViewModel() {
        viewModel.selectedTask.observe(viewLifecycleOwner) { task ->
            if (task != null) {
                initialTitle = task.title
                initialDescription = task.description
                binding.editTitle.setText(task.title)
                binding.editDescription.setText(task.description)
            }
        }

        viewModel.actionCompleted.observe(viewLifecycleOwner) { completed ->
            if (completed) {
                findNavController().navigateUp()
                viewModel.onActionCompletedConsumed()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
