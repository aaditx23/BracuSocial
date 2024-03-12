package com.tutorial.bracusocial.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.bracusocial.ListItemChange
import com.tutorial.bracusocial.databinding.RecycleViewBinding

class CourseListAdapter(private val listener: ListItemChange, private var courseList: MutableList<String>): RecyclerView.Adapter<CourseListAdapter.MainViewHolder>() {

    inner class MainViewHolder(private val itemBinding: RecycleViewBinding):
        RecyclerView.ViewHolder(itemBinding.root){
        fun bindItem(name: String){
            itemBinding.itemSub1.text = name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(RecycleViewBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = courseList[position]
        holder.bindItem(data)

        holder.itemView.setOnClickListener{
            println(data)
            listener.onItemAdded(data)
        }
    }

    fun setFilteredList(newList: MutableList<String>){
        courseList = newList
        notifyDataSetChanged()

    }
    fun setData(newList: MutableList<String>){
        courseList = newList
        notifyDataSetChanged()
    }


}