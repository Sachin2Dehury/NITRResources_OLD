package github.sachin2dehury.nitrresources.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import github.sachin2dehury.nitrresources.R
import github.sachin2dehury.nitrresources.core.Core.stream
import github.sachin2dehury.nitrresources.core.Core.changeFragment
import github.sachin2dehury.nitrresources.ui.fragment.YearFragment
import kotlinx.android.synthetic.main.list_item.view.*

class StreamAdapter : RecyclerView.Adapter<StreamAdapter.StreamHolder>() {

    class StreamHolder(view: View) : RecyclerView.ViewHolder(view)

    private val streamList = listOf("B. Tech", "M. Tech", "Dual Degree", "Int MSC", "MBA")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return StreamHolder(view)
    }

    override fun onBindViewHolder(holder: StreamHolder, position: Int) {
        holder.itemView.apply {
            subText.text = streamList[position]
            imageText.text = streamList[position]
            setOnClickListener {
                stream = streamList[position]
                changeFragment(YearFragment())
            }
        }
    }

    override fun getItemCount(): Int {
        return streamList.size
    }
}