package com.exlskills.android

import java.util.ArrayList

/**
 * Created by Suleiman on 02/03/17.
 */

class Dessert(val name: String, val description: String?) {
    val firstLetter: String

    init {
        this.firstLetter = name.get(0).toString()
    }

    companion object {
        fun prepareDesserts(names: List<String>, descriptions: List<String>): List<Dessert> {
            val desserts = ArrayList<Dessert>(names.size)

            for (i in names.indices) {
                val dessert = Dessert(names[i], descriptions[i])
                desserts.add(dessert)
            }

            return desserts
        }
    }
}