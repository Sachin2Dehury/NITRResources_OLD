package github.sachin2dehury.nitrresources.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import github.sachin2dehury.nitrresources.R
import github.sachin2dehury.nitrresources.core.Core.stream
import github.sachin2dehury.nitrresources.core.Core.year
import github.sachin2dehury.nitrresources.core.Core.changeFragment
import github.sachin2dehury.nitrresources.ui.fragment.BranchFragment
import kotlinx.android.synthetic.main.list_item.view.*

class YearAdapter : RecyclerView.Adapter<YearAdapter.YearHolder>() {

    class YearHolder(view: View) : RecyclerView.ViewHolder(view)

    private val yearList = listOf("First", "Second", "Third", "Fourth", "Fifth")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return YearHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: YearHolder, position: Int) {
        holder.itemView.apply {
            subText.text = "${yearList[position]} Year"
            imageText.text = "${position+1}"
            setOnClickListener {
                year = yearList[position]
                changeFragment(BranchFragment())
            }
        }
    }

    override fun getItemCount(): Int {
        return when (stream) {
            "B. Tech" -> 4
            "M. Tech" -> 2
            "Dual Degree" -> 5
            "Int MSC" -> 5
            "MBA" -> 2
            else -> 4
        }
    }
}