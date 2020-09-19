package github.sachin2dehury.nitrresources.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import github.sachin2dehury.nitrresources.R

class PageAdapter(private val pages: List<String>) :
    RecyclerView.Adapter<PageAdapter.PageHolder>() {
    inner class PageHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return PageHolder(view)
    }

    override fun onBindViewHolder(holder: PageHolder, position: Int) {
        holder.itemView.apply {
//            val fragment = DocListFragment(pages[position])
//            Core.fragmentManager.beginTransaction().apply {
//                replace(R.id.viewPager, fragment)
//                addToBackStack(fragment.javaClass.simpleName)
//                commit()
//            }
        }
    }

    override fun getItemCount(): Int {
        return pages.size;
    }
}