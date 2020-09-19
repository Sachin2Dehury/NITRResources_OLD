package github.sachin2dehury.nitrresources.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import github.sachin2dehury.nitrresources.R
import github.sachin2dehury.nitrresources.core.Core
import github.sachin2dehury.nitrresources.core.Core.UPLOAD_DOC
import github.sachin2dehury.nitrresources.core.Core.changeFragment
import github.sachin2dehury.nitrresources.core.Core.fileUri
import github.sachin2dehury.nitrresources.core.Core.getDocList
import github.sachin2dehury.nitrresources.core.Core.liveData
import github.sachin2dehury.nitrresources.core.Core.openDoc
import github.sachin2dehury.nitrresources.core.DocDetails
import github.sachin2dehury.nitrresources.ui.adapter.DocAdapter
import kotlinx.android.synthetic.main.fragment_list.*

class DocListFragment(private val page: String) :
    Fragment(R.layout.fragment_list) {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPLOAD_DOC && resultCode == AppCompatActivity.RESULT_OK) {
            fileUri = data!!.data!!
            changeFragment(DocNameFragment())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Core.page = page

        progressBar.visibility = View.VISIBLE
        val listObserver = Observer<MutableMap<String, DocDetails>> {
            listView.apply {
                adapter = DocAdapter()
                layoutManager = LinearLayoutManager(context)
            }
        }
        liveData.observeForever(listObserver)
        getDocList()
        Log.w("Hello", "Call from Observer ${liveData.value}")
        listView.adapter!!.notifyDataSetChanged()
        progressBar.visibility = View.GONE

        uploadButton.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                openDoc(this@DocListFragment)
            }
        }
    }
}