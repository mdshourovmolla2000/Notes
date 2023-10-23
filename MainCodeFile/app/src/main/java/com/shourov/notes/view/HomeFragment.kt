package com.shourov.notes.view

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.shourov.notes.R
import com.shourov.notes.adapter.NoteListAdapter
import com.shourov.notes.application.BaseApplication.Companion.database
import com.shourov.notes.callback.SwipeToDeleteCallback
import com.shourov.notes.database.AppDao
import com.shourov.notes.database.tables.NoteTable
import com.shourov.notes.databinding.DialogInfoBinding
import com.shourov.notes.databinding.FragmentHomeBinding
import com.shourov.notes.interfaces.NoteItemClickListener
import com.shourov.notes.repository.HomeRepository
import com.shourov.notes.view_model.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), NoteItemClickListener {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var dao: AppDao
    private lateinit var repository: HomeRepository
    private lateinit var viewModel: HomeViewModel

    private var noteList = ArrayList<NoteTable>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        dao = database.appDao()
        repository = HomeRepository(dao)
        viewModel = ViewModelProvider(this, HomeViewModelFactory(repository))[HomeViewModel::class.java]

        binding.searchButton.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_searchFragment) }
        binding.addNoteIcon.setOnClickListener {
            val bundle = bundleOf(
                "ID" to "",
                "TITLE" to "",
                "DESCRIPTION" to ""
            )
            findNavController().navigate(R.id.action_homeFragment_to_editorFragment, bundle)
        }

        binding.infoButton.setOnClickListener { info() }

        binding.notesRecyclerview.adapter = NoteListAdapter(noteList, this)

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.deleteNote(noteList[viewHolder.absoluteAdapterPosition])
                }
            }
        }

        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(binding.notesRecyclerview)

        observerList()

        return binding.root
    }

    private fun observerList() {
        viewModel.getNotes().observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.notesRecyclerview.visibility = View.GONE
                binding.noNotesAvailableLayout.visibility = View.VISIBLE
            } else {
                noteList.clear()
                noteList.addAll(ArrayList(it).asReversed())

                binding.notesRecyclerview.adapter?.notifyDataSetChanged()

                binding.noNotesAvailableLayout.visibility = View.GONE
                binding.notesRecyclerview.visibility = View.VISIBLE
            }
        }
    }

    private fun info() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val dialogBinding = DialogInfoBinding.inflate(layoutInflater)

        builder.setView(dialogBinding.root)
        builder.setCancelable(true)

        val alertDialog: AlertDialog = builder.create()

        //make transparent to default dialog
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(0))

        alertDialog.show()
    }

    override fun onNoteItemClick(currentItem: NoteTable) {
        val bundle = bundleOf(
            "ID" to currentItem.id,
            "TITLE" to currentItem.title,
            "DESCRIPTION" to currentItem.description
        )
        findNavController().navigate(R.id.action_homeFragment_to_notePreviewFragment, bundle)
    }
}



class HomeViewModelFactory(private val repository: HomeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel(repository) as T
}