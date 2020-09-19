package github.sachin2dehury.nitrresources.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import github.sachin2dehury.nitrresources.R
import github.sachin2dehury.nitrresources.core.Core
import github.sachin2dehury.nitrresources.core.Core.REQUEST_CODE_SIGN_IN
import github.sachin2dehury.nitrresources.core.Core.changeFragment
import github.sachin2dehury.nitrresources.core.Core.googleSignIn
import github.sachin2dehury.nitrresources.core.Core.register
import github.sachin2dehury.nitrresources.core.Core.signIn
import github.sachin2dehury.nitrresources.core.Core.signInWithGoogle
import github.sachin2dehury.nitrresources.core.Core.user
import github.sachin2dehury.nitrresources.core.Core.userPassword
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(R.layout.fragment_login) {

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userButton.text = "Sign In"
        userButton.setOnClickListener {
            user = username.text.toString()
            userPassword = password.text.toString()
            if (userButton.text.toString() == "Register") {
                register()
            } else {
                signIn()
            }
            Core.fragmentManager.popBackStack()
            changeFragment(StreamFragment())
        }
        googleButton.setOnClickListener {
            signInWithGoogle(requireActivity())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
                googleSignIn(it)
            }
            Core.fragmentManager.popBackStack()
            changeFragment(StreamFragment())
        }
    }
}