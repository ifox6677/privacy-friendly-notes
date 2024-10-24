/*
 This file is part of the application Privacy Friendly Notes.
 Privacy Friendly Notes is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.
 Privacy Friendly Notes is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with Privacy Friendly Notes. If not, see <http://www.gnu.org/licenses/>.
 */
package org.secuso.privacyfriendlynotes.ui.util

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.regex.Pattern

data class ChecklistItem(var state: Boolean, var name: String)

/**
 * Provides common utilities to interact with a checklist.
 * @author Patrick Schneider
 */
class ChecklistUtil {

    companion object {
        fun parse(checklist: String): List<ChecklistItem> {
            try {
                val content = JSONArray(checklist)
                return (0 until content.length()).map {
                    val obj = content.getJSONObject(it)
                    return@map ChecklistItem(obj.getBoolean("checked"), obj.getString("name"))
                }
            } catch (ex: JSONException) {
                return ArrayList()
            }
        }

        fun json(checklist: List<ChecklistItem>): JSONArray {
            val jsonArray = JSONArray()
            try {
                for ((state, name) in checklist) {
                    val jsonObject = JSONObject()
                    jsonObject.put("name", name)
                    jsonObject.put("checked", state)
                    jsonArray.put(jsonObject)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return jsonArray
        }

        fun textToItem(text: String): ChecklistItem {
            Pattern.compile("-\\s*\\[(.*)]\\s*(.*)").matcher(text).apply {
                if (matches()) {
                    val checked = group(1)
                    val name = group(2)
                    return ChecklistItem(checked !== null && checked.isNotEmpty() && checked.isNotBlank(), name!!)
                }
            }
            return ChecklistItem(false, text)
        }
    }
}