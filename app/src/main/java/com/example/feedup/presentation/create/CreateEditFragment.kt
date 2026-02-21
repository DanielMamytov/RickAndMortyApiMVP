package com.example.feedup.presentation.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.feedup.R
import com.example.feedup.databinding.FragmentCreateEditBinding
import com.example.feedup.presentation.feed.CharactersViewModel

class CreateEditFragment : Fragment(R.layout.fragment_create_edit) {

    private var _binding: FragmentCreateEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharactersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCreateEditBinding.bind(view)

        viewModel.actionCompleted.observe(viewLifecycleOwner) { completed ->
            if (completed) {
                findNavController().navigateUp()
                viewModel.onActionCompletedConsumed()
            }
        }

        binding.buttonSave.setOnClickListener {
            val title = binding.title.text.toString()
            val description = binding.editDescription.text.toString()
            viewModel.onSaveClicked(title, description)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
