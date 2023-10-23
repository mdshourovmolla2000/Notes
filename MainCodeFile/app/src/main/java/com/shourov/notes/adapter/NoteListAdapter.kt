package com.shourov.notes.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shourov.notes.R
import com.shourov.notes.database.tables.NoteTable
import com.shourov.notes.databinding.SingleNoteItemBinding
import com.shourov.notes.interfaces.NoteItemClickListener

class NoteListAdapter(private var itemList: ArrayList<NoteTable>, private val itemClickListener: NoteItemClickListener): RecyclerView.Adapter<NoteListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_note_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.onBind(itemList[position], position)

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = SingleNoteItemBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun onBind(currentItem: NoteTable, position: Int) {
            val bgColorList = itemView.context.resources.getStringArray(R.array.bgColorList)

            try{
                binding.itemCardView.setCardBackgroundColor(Color.parseColor(bgColorList[position.toString().last().toString().toInt()]))
            } catch (_: Exception){}

            binding.titleTextview.text = currentItem.title

            itemView.setOnClickListener {
                itemClickListener.onNoteItemClick(currentItem)
            }
        }

    }
}