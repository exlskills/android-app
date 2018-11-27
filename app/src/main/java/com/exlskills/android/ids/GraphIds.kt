package com.exlskills.android.ids

import android.util.Base64

class GraphIds {
    companion object {
        fun toGlobalId(objType: String, id: String): String {
            return Base64.encodeToString("$objType:$id".toByteArray(), Base64.DEFAULT)
        }

        fun fromGlobalId(gId: String): GraphId {
            val parts = String(Base64.decode(gId, Base64.DEFAULT)).split(':')
            return GraphId(parts[0], parts[1])
        }
    }
}