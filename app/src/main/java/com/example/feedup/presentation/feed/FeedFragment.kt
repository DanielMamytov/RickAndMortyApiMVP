package com.example.feedup.presentation.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feedup.R
import com.example.feedup.databinding.FragmentFeedBinding

class FeedFragment : Fragment(R.layout.fragment_feed) {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharactersViewModel by viewModels()
    private lateinit var adapter: CharactersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentFeedBinding.bind(view)

        adapter = CharactersAdapter(
            onItemClick = { viewModel.onPostClicked(it) },
            onItemLongClick = { viewModel.onPostLongClicked(it) }
        )
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        binding.retryButton.setOnClickListener { viewModel.onRetryClicked() }
        binding.fabCreate.setOnClickListener { viewModel.onCreateClicked() }

        observeViewModel()

        if (savedInstanceState == null) {
            viewModel.loadCharacters()
        }
    }

    private fun observeViewModel() {
        viewModel.characters.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
            if (posts.isNotEmpty()) {
                showCharacters()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoading()
        }

        viewModel.isEmpty.observe(viewLifecycleOwner) { isEmpty ->
            if (isEmpty) showEmpty()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                showError()
                viewModel.onErrorConsumed()
            }
        }

        viewModel.navigateToDetails.observe(viewLifecycleOwner) { postId ->
            if (!postId.isNullOrBlank()) {
                val bundle = Bundle().apply { putString("postId", postId) }
                findNavController().navigate(R.id.action_feedFragment_to_detailsFragment, bundle)
                viewModel.onDetailsConsumed()
            }
        }

        viewModel.navigateToCreate.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                findNavController().navigate(R.id.action_feedFragment_to_createEditFragment)
                viewModel.onCreateConsumed()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun showLoading() {
        binding.progress.visibility = View.VISIBLE
        binding.recycler.visibility = View.GONE
        binding.emptyText.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
    }

    private fun showCharacters() {
        binding.progress.visibility = View.GONE
        binding.recycler.visibility = View.VISIBLE
        binding.emptyText.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
    }

    private fun showEmpty() {
        binding.progress.visibility = View.GONE
        binding.recycler.visibility = View.GONE
        binding.emptyText.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.GONE
    }

    private fun showError() {
        binding.progress.visibility = View.GONE
        binding.recycler.visibility = View.GONE
        binding.emptyText.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
    }
}
