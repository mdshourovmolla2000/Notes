package com.shourov.notes.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.shourov.notes.R
import com.shourov.notes.adapter.NoteListAdapter
import com.shourov.notes.callback.SwipeToDeleteCallback
import com.shourov.notes.database.AppDao
import com.shourov.notes.database.AppDatabase
import com.shourov.notes.database.tables.NoteTable
import com.shourov.notes.databinding.FragmentSearchBinding
import com.shourov.notes.interfaces.NoteItemClickListener
import com.shourov.notes.repository.SearchRepository
import com.shourov.notes.view_model.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment(), NoteItemClickListener {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var dao: AppDao
    private lateinit var repository: SearchRepository
    private lateinit var viewModel: SearchViewModel

    private var noteList = ArrayList<NoteTable>()
    private var searchText = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        dao = AppDatabase.getDatabase(requireContext()).appDao()
        repository = SearchRepository(dao)
        viewModel = ViewModelProvider(this, SearchViewModelFactory(repository))[SearchViewModel::class.java]

        observerList()

        binding.closeButton.setOnClickListener { binding.searchEdittext.text.clear() }

        binding.notesRecyclerview.apply { adapter = NoteListAdapter(noteList, this@SearchFragment) }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.deleteNote(noteList[viewHolder.absoluteAdapterPosition])
                    noteList.removeAt(viewHolder.absoluteAdapterPosition)
                    withContext(Dispatchers.Main){
                        binding.notesRecyclerview.adapter?.notifyItemRemoved(viewHolder.absoluteAdapterPosition)
                    }
                }
            }
        }

        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(binding.notesRecyclerview)

        binding.searchEdittext.doOnTextChanged { text, _, _, _ ->
            searchText = text.toString()
            if (searchText.isNotEmpty()){
                viewModel.searchNote(searchText)
            } else {
                noteList.clear()
                binding.notesRecyclerview.adapter?.notifyDataSetChanged()
                binding.notFoundLayout.visibility = View.GONE
            }
        }

        return binding.root
    }

    private fun observerList() {
        viewModel.searchResultLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.notesRecyclerview.visibility = View.GONE
                binding.notFoundLayout.visibility = View.VISIBLE
            } else {
                binding.notFoundLayout.visibility = View.GONE
                binding.notesRecyclerview.visibility = View.VISIBLE

                noteList.clear()
                noteList.addAll(ArrayList(it).asReversed())

                binding.notesRecyclerview.adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onNoteItemClick(currentItem: NoteTable) {
        val bundle = bundleOf(
            "ID" to currentItem.id,
            "TITLE" to currentItem.title,
            "DESCRIPTION" to currentItem.description
        )
        findNavController().navigate(R.id.action_searchFragment_to_notePreviewFragment, bundle)
    }
}



class SearchViewModelFactory(private val repository: SearchRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = SearchViewModel(repository) as T
}