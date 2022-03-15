package com.onurbarman.scorpcaseapp.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onurbarman.scorpcaseapp.databinding.ItemPersonBinding
import com.onurbarman.scorpcaseapp.model.person.Person

class PeoplesAdapter : ListAdapter<Person, PeoplesAdapter.ViewHolder>(PeoplesDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount() = currentList.size

    class ViewHolder private constructor(private val binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SetTextI18n")
        fun bind(item: Person) {
            binding.textPerson.text = "${item.fullName} (${item.id})"
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPersonBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    fun submitData(newList: List<Person>?){
        if (currentList == newList)
            return
        submitList(newList)
    }
}


class PeoplesDiffCallback : DiffUtil.ItemCallback<Person>() {

    override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem == newItem
    }


}