package com.exlskills.android

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

/**
 * Created by Suleiman on 02/03/17.
 */

class DessertAdapter(private val context: Context) : RecyclerView.Adapter<DessertAdapter.DessertVh>() {
    private var desserts: List<Dessert>

    init {
        desserts = Dessert.prepareDesserts(
            ArrayList(context.resources.getStringArray(R.array.dessert_names).toMutableList()),
            ArrayList(context.resources.getStringArray(R.array.dessert_descriptions).toMutableList())
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DessertVh {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_dessert, parent, false)
        return DessertAdapter.DessertVh(view)
    }

    override fun onBindViewHolder(holder: DessertVh, position: Int) {
        val dessert = desserts[position]

        holder.mName.text = dessert.name
        holder.mDescription.text = dessert.description
        holder.mFirstLetter.text = dessert.firstLetter

    }

    override fun getItemCount(): Int {
        return if (desserts == null) 0 else desserts.size
    }

    class DessertVh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mName: TextView
        val mDescription: TextView
        val mFirstLetter: TextView
        init {

            mName = itemView.findViewById(R.id.txt_name) as TextView
            mDescription = itemView.findViewById(R.id.txt_desc) as TextView
            mFirstLetter = itemView.findViewById(R.id.txt_firstletter) as TextView
        }
    }
}