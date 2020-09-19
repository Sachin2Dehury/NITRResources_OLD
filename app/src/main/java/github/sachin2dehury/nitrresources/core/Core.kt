package github.sachin2dehury.nitrresources.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import github.sachin2dehury.nitrresources.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object Core {
    lateinit var branch: String
    lateinit var year: String
    lateinit var stream: String
    lateinit var user: String
    lateinit var userPassword: String
    lateinit var page: String

    lateinit var fragmentManager: FragmentManager

    private lateinit var currentUser: FirebaseUser

    private val firebaseStorage = FirebaseStorage.getInstance().reference
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFireStore = FirebaseFirestore.getInstance()

    const val UPLOAD_DOC = 1
    const val REQUEST_CODE_SIGN_IN = 2
    const val bookLink = "http://libgen.rs/"
    const val questionLink = "https://eapplication.nitrkl.ac.in/nitris/Login.aspx"
    private const val PDF = "application/pdf"
    private const val PPT = "application/ppt"
    private const val IMG = "image/*"
    private const val MB = 1024 * 1024

    private val mutableLiveData = MutableLiveData<MutableMap<String, DocDetails>>()
    val liveData: LiveData<MutableMap<String, DocDetails>> = mutableLiveData

    private val mutableSearchLiveData = MutableLiveData<MutableMap<String, DocDetails>>()
    private val liveSearchData: LiveData<MutableMap<String, DocDetails>> = mutableSearchLiveData

    lateinit var fileUri: Uri
    lateinit var docDetails: DocDetails

    init {
        mutableLiveData.value = mutableMapOf()
        mutableSearchLiveData.value = mutableMapOf()
    }

    fun changeFragment(fragment: Fragment) {
        fragmentManager.beginTransaction().apply {
            replace(R.id.navFragment, fragment)
            addToBackStack(fragment.javaClass.simpleName)
            commit()
        }
    }

    fun getDocList() = CoroutineScope(Dispatchers.IO).launch {
        try {
            mutableLiveData.value!!.clear()
            val path = "NITR/$stream/$year/$branch/$page"
            val documents = firebaseFireStore.collection(path).get().await()!!.documents
            withContext(Dispatchers.Main) {
                for (document in documents) {
                    val doc = document.toObject(DocDetails::class.java)!!
                    mutableLiveData.value!![document.id] = doc
                }
            }
        } catch (e: Exception) {
            Log.w("Catch", "getDocList")
            Log.w("Catch", e.toString())
        }
//        Log.w("Hello", "Get list ${liveData.value}")
    }

    private fun uploadDoc(uri: Uri, doc: DocDetails) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val path = "NITR/$stream/$year/$branch/$page"
                val docId = firebaseFireStore.collection(path).add(doc).await()!!.id
                val storeReference = firebaseStorage.child("$path/$docId.pdf")
                val docRef = firebaseFireStore.collection(path).document(docId)
                storeReference.putFile(uri).await()
                doc.url = storeReference.downloadUrl.await().toString()
                storeReference.metadata.await().apply {
                    doc.size = sizeBytes.toDouble() / MB
                    doc.time = updatedTimeMillis
                    doc.type = contentType!!
                }
                withContext(Dispatchers.Main) {
                    mutableLiveData.value!![docId] = doc
                }
                firebaseFireStore.runBatch { batch ->
                    batch.update(docRef, "url", doc.url)
                    batch.update(docRef, "size", doc.size)
                    batch.update(docRef, "time", doc.time)
                    batch.update(docRef, "type", doc.type)
                }.await()
            } catch (e: Exception) {
                Log.w("Catch", "uploadDoc")
                Log.w("Catch", e.toString())
            }
        }

    fun saveDoc() = CoroutineScope(Dispatchers.IO).launch {
        try {
            fragmentManager.popBackStack()
            uploadDoc(fileUri, docDetails)
        } catch (e: Exception) {
            Log.w("Catch", "saveDoc")
            Log.w("Catch", e.toString())
        }
    }

    fun searchDoc(name: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            mutableSearchLiveData.value!!.clear()
            val path = "NITR/$stream/$year/$branch/$page"
            val search =
                firebaseFireStore.collection(path).whereEqualTo("name", name).get()
                    .await()!!.documents
            withContext(Dispatchers.Main) {
                for (document in search) {
                    val doc = document.toObject(DocDetails::class.java)!!
                    mutableSearchLiveData.value!![document.id] = doc
                }
            }
        } catch (e: Exception) {
            Log.w("Catch", "searchDOc")
            Log.w("Catch", e.toString())
        }
    }

    fun myDocs() = CoroutineScope(Dispatchers.IO).launch {
        try {
            mutableSearchLiveData.value!!.clear()
            val path = "NITR/$stream/$year/$branch/$page"
            val search =
                firebaseFireStore.collection(path)
                    .whereEqualTo("contributor", currentUser.toString()).get()
                    .await()!!.documents
            withContext(Dispatchers.Main) {
                for (document in search) {
                    val doc = document.toObject(DocDetails::class.java)!!
                    mutableSearchLiveData.value!![document.id] = doc
                }
            }
        } catch (e: Exception) {
            Log.w("Catch", "searchDOc")
            Log.w("Catch", e.toString())
        }
    }

    fun updateDocList() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val path = "NITR/$stream/$year/$branch/$page"
            lateinit var querySnapshot: QuerySnapshot
            firebaseFireStore.collection(path).addSnapshotListener { value, _ ->
                querySnapshot = value!!
            }
            withContext(Dispatchers.Main) {
                for (change in querySnapshot.documentChanges) {
                    val doc = (change.document.toObject(DocDetails::class.java))
                    when (change.type) {
                        DocumentChange.Type.ADDED -> mutableLiveData.value!![change.document.id] =
                            doc
                        DocumentChange.Type.MODIFIED -> mutableLiveData.value!![change.document.id] =
                            doc
                        DocumentChange.Type.REMOVED -> mutableLiveData.value!!.remove(change.document.id)
                    }
                }
            }
        } catch (e: Exception) {
            Log.w("Catch", "updateDocList")
            Log.w("Catch", e.toString())
        }
    }

    fun renameDoc(docId: String, name: String, subName: String, subCode: Int) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val path = "NITR/$stream/$year/$branch/$page"
                val docRef = firebaseFireStore.collection(path).document(docId)
                firebaseFireStore.runBatch { batch ->
                    batch.update(docRef, "name", name)
                    batch.update(docRef, "subCode", subCode)
                    batch.update(docRef, "subName", subName)
                }.await()
                val doc = mutableLiveData.value!![docId]!!.copy(
                    name = name,
                    subCode = subCode,
                    subName = subName
                )
                withContext(Dispatchers.Main) {
                    mutableLiveData.value!![docId] = doc
                }
            } catch (e: Exception) {
                Log.w("Catch", "renameDoc")
                Log.w("Catch", e.toString())
            }
        }

    fun batchUpload() = CoroutineScope(Dispatchers.IO).launch {
//        TODO
    }

    fun deleteDoc(docId: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val path = "NITR/$stream/$year/$branch/$page"
            firebaseFireStore.collection(path).document(docId).delete().await()
            firebaseFireStore.collection("Trash").add(mutableLiveData.value!![docId]!!).await().id
            withContext(Dispatchers.Main) {
                mutableLiveData.value!!.remove(docId)
            }
        } catch (e: Exception) {
            Log.w("Catch", "deleteDoc")
            Log.w("Catch", e.toString())
        }
    }

    fun downloadDoc(link: String, context: Context) {
        val url = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, url)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    fun openDoc(fragment: Fragment) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = PDF
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        fragment.startActivityForResult(intent, UPLOAD_DOC)
    }

    fun signIn() = CoroutineScope(Dispatchers.IO).launch {
        try {
            firebaseAuth.signInWithEmailAndPassword(user, userPassword).await()!!
            currentUser = firebaseAuth.currentUser!!
            firebaseFireStore.collection("User").add(User(user, userPassword)).await()
        } catch (e: Exception) {
            Log.w("Catch", "signIn")
            Log.w("Catch", e.toString())
        }
    }

    fun register() = CoroutineScope(Dispatchers.IO).launch {
        try {
            firebaseAuth.createUserWithEmailAndPassword(user, userPassword).await()!!
            currentUser = firebaseAuth.currentUser!!
            firebaseFireStore.collection("User").add(User(user, userPassword)).await()
        } catch (e: Exception) {
            Log.w("Catch", "register")
            Log.w("Catch", e.toString())
        }
    }

    fun signOut() = CoroutineScope(Dispatchers.IO).launch {
        try {
            firebaseAuth.signOut()
//            currentUser = ""
        } catch (e: Exception) {
            Log.w("Catch", "signOut")
            Log.w("Catch", e.toString())
        }
    }

    fun signInWithGoogle(activity: Activity) {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val signInClient = GoogleSignIn.getClient(activity, options)
        signInClient.signInIntent.also { intent ->
            activity.startActivityForResult(intent, REQUEST_CODE_SIGN_IN)
        }
    }

    fun googleSignIn(account: GoogleSignInAccount) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credentials).await()
            currentUser = firebaseAuth.currentUser!!
//            firebaseFireStore.collection("User").add(User(user, userPassword)).await()
        } catch (e: Exception) {
            Log.w("Catch", "googleSignIn")
            Log.w("Catch", e.toString())
        }
    }
}