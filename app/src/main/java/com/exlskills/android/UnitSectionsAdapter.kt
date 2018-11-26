package com.exlskills.android

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.exlskills.android.remote.UnitSection
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by Suleiman on 02/03/17.
 */

class UnitSectionsAdapter(private val context: Context, private val sections: List<UnitSection>, val onClickCallback: (section: UnitSection) -> Unit) : RecyclerView.Adapter<UnitSectionsAdapter.UnitSectionVh>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitSectionVh {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_unit_section, parent, false)
        return UnitSectionsAdapter.UnitSectionVh(view)
    }

    override fun onBindViewHolder(holder: UnitSectionVh, position: Int) {
        val section = sections[position]

        holder.mName.text = section.title
        holder.mDescription.text = section.headline
        holder.mFirstLetter.text = (position + 1).toString()
        when (section.ema.roundToInt()) {
            100 -> holder.mFirstLetter.setTextColor(context.resources.getColor(R.color.colorPrimary))
            in 80..100 -> holder.mFirstLetter.setTextColor(context.resources.getColor(R.color.colorAccent))
        }
        holder.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                return onClickCallback(section)
            }
        })
    }

    override fun getItemCount(): Int {
        return sections.size
    }

    class UnitSectionVh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mName: TextView
        val mDescription: TextView
        val mFirstLetter: TextView

        init {
            mName = itemView.findViewById(R.id.txt_name) as TextView
            mDescription = itemView.findViewById(R.id.txt_desc) as TextView
            mFirstLetter = itemView.findViewById(R.id.txt_firstletter) as TextView
        }

        fun setOnClickListener(l: View.OnClickListener) {
            itemView.setOnClickListener(l)
        }
    }
}
