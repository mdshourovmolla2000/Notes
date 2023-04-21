package com.shourov.notes.view

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shourov.notes.database.AppDao
import com.shourov.notes.database.AppDatabase
import com.shourov.notes.database.tables.NoteTable
import com.shourov.notes.databinding.DialogEditorExitBinding
import com.shourov.notes.databinding.DialogSaveBinding
import com.shourov.notes.databinding.FragmentEditorBinding
import com.shourov.notes.repository.EditorRepository
import com.shourov.notes.utils.KeyboardManager
import com.shourov.notes.utils.showWarningToast
import com.shourov.notes.view_model.EditorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorFragment : Fragment() {

    private lateinit var binding: FragmentEditorBinding

    private lateinit var dao: AppDao
    private lateinit var repository: EditorRepository
    private lateinit var viewModel: EditorViewModel

    private var id = 0
    private var title = ""
    private var description = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditorBinding.inflate(inflater, container, false)

        dao = AppDatabase.getDatabase(requireContext()).appDao()
        repository = EditorRepository(dao)
        viewModel =
            ViewModelProvider(this, EditorViewModelFactory(repository))[EditorViewModel::class.java]

        id = arguments?.getInt("ID")!!.toInt()
        title = arguments?.getString("TITLE").toString()
        description = arguments?.getString("DESCRIPTION").toString()

        if (title.isNotEmpty()) {
            binding.titleEdittext.setText(title)
            binding.descriptionEdittext.setText(description)
        }

        binding.backButton.setOnClickListener { editorExitDialog() }

        binding.saveButton.setOnClickListener { saveNote(it) }

        return binding.root
    }

    private fun saveNote(view: View) {
        if (binding.titleEdittext.text.toString().trim().isEmpty()) {
            requireContext().showWarningToast("Enter title")
            KeyboardManager.showKeyboard(binding.titleEdittext)
            return
        }
        if (binding.descriptionEdittext.text.toString().trim().isEmpty()) {
            requireContext().showWarningToast("Enter description")
            KeyboardManager.showKeyboard(binding.descriptionEdittext)
            return
        }
        KeyboardManager.hideKeyBoard(requireContext(), view)
        saveNoteDialog()
    }

    private fun saveNoteDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val dialogBinding = DialogSaveBinding.inflate(layoutInflater)

        builder.setView(dialogBinding.root)
        builder.setCancelable(true)

        val alertDialog: AlertDialog = builder.create()

        //make transparent to default dialog
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(0))

        dialogBinding.discardButton.setOnClickListener { alertDialog.dismiss() }

        dialogBinding.saveButton.setOnClickListener {
            if (title.isEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.insertNote(
                        NoteTable(
                            0,
                            binding.titleEdittext.text.toString().trim(),
                            binding.descriptionEdittext.text.toString().trim()
                        )
                    )

                    withContext(Dispatchers.Main) {
                        alertDialog.dismiss()
                        findNavController().popBackStack()
                    }
                }
            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.updateNote(
                        NoteTable(
                            id,
                            binding.titleEdittext.text.toString().trim(),
                            binding.descriptionEdittext.text.toString().trim()
                        )
                    )

                    withContext(Dispatchers.Main) {
                        alertDialog.dismiss()
                        findNavController().popBackStack()
                    }
                }
            }

        }

        alertDialog.show()
    }

    private fun editorExitDialog() {
        if (binding.titleEdittext.text.toString().trim()
                .isNotEmpty() or binding.descriptionEdittext.text.toString().trim().isNotEmpty()
        ) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            val dialogBinding = DialogEditorExitBinding.inflate(layoutInflater)

            builder.setView(dialogBinding.root)
            builder.setCancelable(true)

            val alertDialog: AlertDialog = builder.create()

            //make transparent to default dialog
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(0))

            dialogBinding.discardButton.setOnClickListener {
                findNavController().popBackStack()
                alertDialog.dismiss()
            }

            dialogBinding.keepButton.setOnClickListener { alertDialog.dismiss() }

            alertDialog.show()
        } else {
            findNavController().popBackStack()
        }
    }
}


class EditorViewModelFactory(private val repository: EditorRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = EditorViewModel(repository) as T
}