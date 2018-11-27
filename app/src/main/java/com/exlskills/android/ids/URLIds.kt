package com.exlskills.android.ids

import com.exlskills.android.ids.GraphIds.Companion.fromGlobalId
import com.exlskills.android.ids.GraphIds.Companion.toGlobalId
import org.intellij.lang.annotations.RegExp

class URLIds {
    companion object {
        private val trimRegex = Regex("^\\s+|\\s+$")
        private val rmInvCharsRegex = Regex("[^a-z0-9 -]")
        private val rmWhitespaceRegex = Regex("\\s+")
        private val rmDashesRegex = Regex("-+")
        private val fromUrlChars = "àáäâèéëêìíïîòóöôùúüûñç·/_,:;"
        private val toUrlChars = "aaaaeeeeiiiioooouuuunc------"

        private fun stringToSlug(inStr: String): String {
            // Note, in the regexes there is no need for the /g option as replace matches all occurrences anyway
            var str = inStr
            str = trimRegex.replace(str, "") // trim
            str = str.toLowerCase()

            // remove accents, swap ñ for n, etc
            for (i in fromUrlChars.indices) {
                str = Regex(fromUrlChars[i].toString()).replace(str, toUrlChars[i].toString())
            }

            str = rmInvCharsRegex.replace(str, "") // remove invalid chars
            str = rmWhitespaceRegex.replace(str, "-") // collapse whitespace and replace by -
            str = rmDashesRegex.replace(str, "-") // collapse dashes

            return str
        }

        fun toUrlId(text: String, id: String): String {
            return "${stringToSlug(text)}-${fromGlobalId(id).id}"
        }

        fun fromUrlId(objType: String, urlId: String): String {
            return toGlobalId(objType, urlId.substring(urlId.lastIndexOf('-') + 1))
        }
    }
}