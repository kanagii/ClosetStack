package com.example.closetstack

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter

class TagAutoCompleteAdapter(
    context: Context,
    private val allTags: List<String>,
    private val etTags: AutoCompleteTextView
) : ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, mutableListOf()) {

    private val tagFilter: Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            if (constraint == null) {
                results.values = emptyList<String>()
                results.count = 0
                return results
            }

            // Only filter based on the word being currently typed (after last comma)
            val lastWord: String = constraint.toString()
                .substringAfterLast(",")
                .trim()

            val filtered: List<String> = if (lastWord.isEmpty()) {
                allTags
            } else {
                allTags.filter { tag ->
                    tag.startsWith(lastWord, ignoreCase = true)
                }
            }

            results.values = filtered
            results.count = filtered.size
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            clear()
            val list: List<String> = (results?.values as? List<String>) ?: emptyList()
            if (list.isNotEmpty()) {
                addAll(list)
            }
            if ((results?.count ?: 0) > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            val selectedTag: String = resultValue?.toString() ?: return ""
            val currentText: String = etTags.text.toString()
            return if (currentText.contains(",")) {
                val prefix: String = currentText.substringBeforeLast(",").trimEnd()
                "$prefix, $selectedTag"
            } else {
                selectedTag
            }
        }
    }

    override fun getFilter(): Filter = tagFilter
}