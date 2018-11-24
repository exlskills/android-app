package com.exlskills.android

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.exlskills.android.com.exlskills.android.remote.DigitalDiploma

class ProjectCardRecyclerAdapter(private val courses: List<DigitalDiploma>) :
    RecyclerView.Adapter<ProjectCardRecyclerAdapter.ProjectViewHolder>() {

    private val images = intArrayOf(R.drawable.gophergif,
        R.drawable.gophergif, R.drawable.gophergif,
        R.drawable.gophergif, R.drawable.gophergif,
        R.drawable.gophergif, R.drawable.gophergif,
        R.drawable.gophergif)

    inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)
            itemDetail = itemView.findViewById(R.id.item_detail)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ProjectViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_layout, viewGroup, false)
        return ProjectViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ProjectViewHolder, i: Int) {
        viewHolder.itemTitle.text = courses[i].title
        viewHolder.itemDetail.text = courses[i].headline
        viewHolder.itemImage.setImageResource(images[0])
    }

    override fun getItemCount(): Int {
        return courses.size
    }

}