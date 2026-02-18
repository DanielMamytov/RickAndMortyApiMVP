import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.feedup.R
import com.example.feedup.databinding.FragmentCreateEditBinding
import com.example.feedup.model.TaskItem
import com.example.feedup.presentation.feed.CharacterContract
import com.example.feedup.presentation.feed.CharactersPresenter

class CreateEditFragment :
    Fragment(R.layout.fragment_create_edit),
    CharacterContract.View {

    private var _binding: FragmentCreateEditBinding? = null
    private val binding get() = _binding!!

    private val presenter = CharactersPresenter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCreateEditBinding.bind(view)
        presenter.attach(this)

        binding.buttonSave.setOnClickListener {
            val title = binding.title.text.toString()
            val description = binding.editDescription.text.toString()

            presenter.onSaveClicked(title, description)

            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        presenter.detach()
        _binding = null
        super.onDestroyView()
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun showCharacters(posts: List<TaskItem>) {
        TODO("Not yet implemented")
    }

    override fun showEmpty() {
        TODO("Not yet implemented")
    }

    override fun showError(message: String) {
        // Toast / Snackbar
    }

    override fun navigateToDetails(postId: Int) {
        TODO("Not yet implemented")
    }

    override fun navigateToCreate() {
        TODO("Not yet implemented")
    }
}