package com.onurbarman.scorpcaseapp.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.onurbarman.scorpcaseapp.databinding.ItemPersonBinding
import com.onurbarman.scorpcaseapp.model.person.Person

class PersonAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PersonViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PersonViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Person>?) {
        if (list == null){
            differ.submitList(null)
        }
        else {
            val personList = differ.currentList.toMutableList()
            personList.addAll(list)
            differ.submitList(personList.toSet().toList())
        }
    }

    class PersonViewHolder constructor(private val binding: ItemPersonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Person) {
            binding.textPerson.text = "${item.fullName} (${item.id})"
        }

        companion object {
            fun from(parent: ViewGroup): PersonViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPersonBinding.inflate(layoutInflater, parent, false)
                return PersonViewHolder(binding)
            }
        }
    }
}