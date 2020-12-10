package com.robert.quickreturnbehavior

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SimpleStringRecyclerViewAdapter(private val mValues: List<String?>) : RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
        var mBoundString: String? = null
        val mTextView: TextView = mView.findViewById<View>(android.R.id.text1) as TextView

        override fun toString(): String {
            return super.toString() + " '" + mTextView.text
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mBoundString = mValues[position]
        holder.mTextView.text = mValues[position]
    }

    override fun getItemCount(): Int {
        return mValues.size
    }
}