package github.sachin2dehury.nitrresources.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import github.sachin2dehury.nitrresources.R
import github.sachin2dehury.nitrresources.core.Core.branch
import github.sachin2dehury.nitrresources.core.Core.stream
import github.sachin2dehury.nitrresources.ui.fragment.PageFragment
import kotlinx.android.synthetic.main.list_item.view.*

class BranchAdapter : RecyclerView.Adapter<BranchAdapter.BranchHolder>() {

    class BranchHolder(view: View) : RecyclerView.ViewHolder(view)

    private val branchList = when (stream) {
        "B. Tech" -> listOf(
            "AR",
            "BM",
            "BT",
            "CE",
            "CH",
            "CR",
            "CS",
            "EC",
            "EI",
            "EE",
            "FP",
            "ID",
            "ME",
            "MM",
            "MN"
        )
        "Int MSC" -> listOf(
            "CY",
            "ER",
            "HS",
            "LS",
            "MA",
            "PH",
        )
        else -> listOf("")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BranchHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return BranchHolder(view)
    }

    override fun onBindViewHolder(holder: BranchHolder, position: Int) {
        holder.itemView.apply {
            subText.text = branchList[position]
            imageText.text = branchList[position]
            setOnClickListener {
                branch = branchList[position]
                val intent = Intent(context, PageFragment::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return branchList.size
    }
}