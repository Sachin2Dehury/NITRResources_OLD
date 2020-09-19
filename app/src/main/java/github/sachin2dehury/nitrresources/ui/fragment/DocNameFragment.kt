package github.sachin2dehury.nitrresources.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import github.sachin2dehury.nitrresources.R
import github.sachin2dehury.nitrresources.core.Core.docDetails
import github.sachin2dehury.nitrresources.core.Core.renameDoc
import github.sachin2dehury.nitrresources.core.Core.saveDoc
import github.sachin2dehury.nitrresources.core.DocDetails
import kotlinx.android.synthetic.main.fragment_doc_name.*

class DocNameFragment(private val docId: String = "", private val flag: Boolean = true) :
    Fragment(R.layout.fragment_doc_name) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveButton.setOnClickListener {
            checkInput()
        }
    }

    private fun checkInput() {
        val fileName = fileName.text.toString()
        val subCode = subCode.text.toString().toInt()
        val subName = subName.text.toString()
        if (fileName.isNotBlank() && subCode > 1000 && subCode < 7000 && subName.isNotBlank()) {
            docDetails = DocDetails(fileName, subCode, subName)
            if (flag) {
                saveDoc()
            } else {
                renameDoc(docId, fileName, subName, subCode)
            }
        } else {
            Toast.makeText(context, "Please Enter Valid File Details", Toast.LENGTH_LONG).show()
        }
    }
}