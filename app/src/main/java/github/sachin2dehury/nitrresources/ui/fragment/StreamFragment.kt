package github.sachin2dehury.nitrresources.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import github.sachin2dehury.nitrresources.R
import github.sachin2dehury.nitrresources.ui.adapter.StreamAdapter
import kotlinx.android.synthetic.main.fragment_list.*

class StreamFragment : Fragment(R.layout.fragment_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.apply {
            adapter = StreamAdapter()
            layoutManager = LinearLayoutManager(context)
        }
    }
}