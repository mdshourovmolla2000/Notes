package com.shourov.notes.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shourov.notes.R
import com.shourov.notes.databinding.FragmentNotePreviewBinding

class NotePreviewFragment : Fragment() {

    private lateinit var binding: FragmentNotePreviewBinding

    private var id = 0
    private var title = ""
    private var description = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotePreviewBinding.inflate(inflater, container, false)

        id = arguments?.getInt("ID")!!.toInt()
        title = arguments?.getString("TITLE").toString()
        description = arguments?.getString("DESCRIPTION").toString()


        binding.titleTextview.text = title
        binding.descriptionTextview.text = description


        binding.editButton.setOnClickListener {
            val bundle = bundleOf(
                "ID" to id,
                "TITLE" to title,
                "DESCRIPTION" to description
            )
            findNavController().navigate(R.id.action_notePreviewFragment_to_editorFragment, bundle)
        }

        binding.backButton.setOnClickListener { findNavController().popBackStack() }

        return binding.root
    }
}