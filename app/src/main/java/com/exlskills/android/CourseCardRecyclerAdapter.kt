package com.exlskills.android

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.exlskills.android.remote.CourseLiteMeta
import com.squareup.picasso.Picasso

class CourseCardRecyclerAdapter(private val courses: List<CourseLiteMeta>, val onClickCallback: (course: CourseLiteMeta) -> Unit) :
    RecyclerView.Adapter<CourseCardRecyclerAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var courseId: String = ""
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)
            itemDetail = itemView.findViewById(R.id.item_detail)
        }

        fun setOnClickListener(l: View.OnClickListener) {
            itemView.setOnClickListener(l)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CourseViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_layout, viewGroup, false)
        return CourseViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: CourseViewHolder, i: Int) {
        viewHolder.itemTitle.text = courses[i].title
        viewHolder.itemDetail.text = courses[i].headline
        Picasso.get().load(courses[i].logo_url).into(viewHolder.itemImage)
        viewHolder.courseId = courses[i].id
        viewHolder.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                return onClickCallback(courses[i])
            }
        })
    }

    override fun getItemCount(): Int {
        return courses.size
    }

}