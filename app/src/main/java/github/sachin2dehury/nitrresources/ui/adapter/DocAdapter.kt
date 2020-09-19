package github.sachin2dehury.nitrresources.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import github.sachin2dehury.nitrresources.R
import github.sachin2dehury.nitrresources.core.Core
import github.sachin2dehury.nitrresources.core.Core.downloadDoc
import github.sachin2dehury.nitrresources.core.Core.liveData
import github.sachin2dehury.nitrresources.ui.fragment.DocNameFragment
import kotlinx.android.synthetic.main.doc_item.view.*

class DocAdapter :
    RecyclerView.Adapter<DocAdapter.DocHolder>() {
    class DocHolder(view: View) : RecyclerView.ViewHolder(view)

    private val keys = liveData.value!!.keys.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.doc_item, parent, false)
        return DocHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DocHolder, position: Int) {
        val docId = keys[position]
        val doc = liveData.value!![docId]!!
        holder.itemView.apply {
            docName.text = doc.name
            subText.text = "${doc.subCode}:${doc.subName}"
            setOnClickListener {
                downloadDoc(doc.url, context)
            }
            setOnLongClickListener {
                val menu = PopupMenu(context, holder.itemView).apply {
                    menuInflater.inflate(R.menu.item_menu, menu)
                    show()
                }
                menu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.rename -> {
                            Core.changeFragment(DocNameFragment(docId, false))
                            true
                        }
                        R.id.delete -> {
//                            Core.deleteDoc(docId)
                            true
                        }
                        R.id.download -> {
                            downloadDoc(doc.url, context)
                            true
                        }
                        else -> false
                    }
                }
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return keys.size
    }
}