package com.example.feedup.presentation.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feedup.R
import com.example.feedup.databinding.FragmentFeedBinding
import com.example.feedup.model.Result

class FeedFragment : Fragment(R.layout.fragment_feed), CharacterContract.View {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private val presenter: CharacterContract.Presenter = CharactersPresenter()
    private lateinit var adapter: CharactersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentFeedBinding.bind(view)

        adapter = CharactersAdapter()
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        binding.retryButton.setOnClickListener { presenter.onRetryClicked() }
        binding.fabCreate.setOnClickListener { presenter.onCreateClicked() }

        presenter.attach(this)
        presenter.loadCharacters()
    }

    override fun showLoading() {
        binding.progress.visibility = View.VISIBLE
        binding.recycler.visibility = View.GONE
        binding.emptyText.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
    }

    override fun showCharacters(posts: List<Result>) {
        adapter.submitList(posts)
        binding.progress.visibility = View.GONE
        binding.recycler.visibility = View.VISIBLE
        binding.emptyText.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
    }

    override fun showEmpty() {
        binding.progress.visibility = View.GONE
        binding.recycler.visibility = View.GONE
        binding.emptyText.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.GONE
    }

    override fun showError(message: String) {
        binding.progress.visibility = View.GONE
        binding.recycler.visibility = View.GONE
        binding.emptyText.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE    }

    override fun navigateToDetails(postId: Int) {
        val bundle = Bundle().apply { putInt("postId", postId) }
        findNavController().navigate(R.id.action_feedFragment_to_detailsFragment, bundle)
    }

    override fun navigateToCreate() {
        findNavController().navigate(R.id.action_feedFragment_to_createEditFragment)
    }


}