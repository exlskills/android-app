package com.exlskills.android

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.exlskills.android.remote.DigitalDiploma
import com.squareup.picasso.Picasso

class ProjectCardRecyclerAdapter(private val projects: List<DigitalDiploma>) :
    RecyclerView.Adapter<ProjectCardRecyclerAdapter.ProjectViewHolder>() {

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
        viewHolder.itemTitle.text = projects[i].title
        viewHolder.itemDetail.text = projects[i].headline
        Picasso.get().load(projects[i].logo_url).into(viewHolder.itemImage)
    }

    override fun getItemCount(): Int {
        return projects.size
    }

}