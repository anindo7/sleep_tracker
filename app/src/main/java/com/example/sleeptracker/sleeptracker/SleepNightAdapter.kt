package com.example.sleeptracker.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sleeptracker.R
import com.example.sleeptracker.convertDurationToFormatted
import com.example.sleeptracker.convertNumericQualityToString
import com.example.sleeptracker.database.SleepNight
import com.example.sleeptracker.databinding.ListItemViewBinding

class SleepNightAdapter: ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(
    SleepNightDiffCallback()
) {
//    var data= listOf<SleepNight>()
//    set(value){
//        field=value
//        notifyDataSetChanged()
//    }
//
//    override fun getItemCount(): Int {
//        return data.size
//    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)// data[position]
        holder.bind(item) // refactored code
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        ) // refactored code
    }

    class ViewHolder private constructor(val binding: ListItemViewBinding): RecyclerView.ViewHolder(binding.root){

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemViewBinding.inflate(inflater,parent,false)
                return ViewHolder(
                    binding
                )
            }
        }

         fun bind(item: SleepNight) {
             binding.sleep = item
             binding.executePendingBindings()
        }
    }
}

class SleepNightDiffCallback: DiffUtil.ItemCallback<SleepNight>(){

    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId==newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem==newItem
    }
}