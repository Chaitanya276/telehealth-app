package com.app.telehealth.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.app.telehealth.Constants
import com.app.telehealth.HomeActivity
import com.app.telehealth.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_sign_in.view.*


const val RC_SIGN_IN = 123
//lateinit var callbackManager: CallbackManager
//const val EMAIL = "EMAIL"

class SignInFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        view.textFieldLoginEmail.errorIconDrawable = null
        view.textFieldLoginPassword.errorIconDrawable = null
        view.btnLogin.setOnClickListener {
            if (Constants.isInternetAvailable(activity!!)) {
                if (Constants.formValidation(
                        emailET = view.etLoginEmail,
                        emailLayout = view.textFieldLoginEmail,
                        passwordET = view.etLoginPassword,
                        passwordLayout = view.textFieldLoginPassword
                    )
                ) {
                    val email = view.etLoginEmail.text.toString()
                    val password = view.etLoginPassword.text.toString()
                    view.signInLoader.isVisible = true
                    signInUser(email, password)
                }
            } else {
                Toast.makeText(activity, "Internet unavailable", Toast.LENGTH_SHORT).show()
            }

        }
        view.tvLoginForgotPassword.setOnClickListener {
            Constants.navigateFragment(ForgotPasswordFragment(), activity!!, R.id.signInFrame, true)

        }
        view.tvLoginSignUp.setOnClickListener {
            Constants.navigateFragment(SignUpFragment(), activity!!, R.id.signInFrame, true)

        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(activity!!, gso)
        view.googleSignIn.setSize(SignInButton.SIZE_STANDARD)
        view.googleSignIn.setOnClickListener {
            if (Constants.isInternetAvailable(activity!!)) {
                view.signInLoader.isVisible = true
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            } else {
                Toast.makeText(activity, "Internet unavailable", Toast.LENGTH_SHORT).show()
            }

        }
//        view.facebookSignIn.setOnClickListener {
//            view.facebookSignIn.setReadPermissions(listOf(EMAIL))
//
//            callbackManager = CallbackManager.Factory.create()
//
//            LoginManager.getInstance().registerCallback(callbackManager,
//                object : FacebookCallback<LoginResult> {
//                    override fun onSuccess(loginResult: LoginResult?) {
//                    }
//
//                    override fun onCancel() {
//                    }
//
//                    override fun onError(exception: FacebookException) {
//                    }
//                })
//        }


        return view
    }

    private fun signInUser(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                if (firebaseUser.isEmailVerified) {
                    val sharedPreferences =
                        activity?.getSharedPreferences(Constants.SHARED_TAG, Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreferences!!.edit()

                    editor.putBoolean("LoggedIn", true)
                    editor.apply()
                    editor.commit()
                    view?.signInLoader?.isVisible = false
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    view?.signInLoader?.isVisible = false
                    Toast.makeText(activity, "Please verify email first", Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener {
                when {
                    it.message.toString().contains("no user record") -> {
                        Toast.makeText(
                            activity,
                            "No user registered with this email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    it.message.toString().contains("password is invalid") -> {
                        Toast.makeText(activity, "Incorrect password", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(activity, "something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
                view?.signInLoader?.isVisible = false
            }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
//        }else{
//            callbackManager.onActivityResult(requestCode, resultCode, data);
//            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /*

var accessTokenTracker: AccessTokenTracker = object : AccessTokenTracker() {
    override fun onCurrentAccessTokenChanged(
        oldAccessToken: AccessToken,
        currentAccessToken: AccessToken
    ) {
        if (currentAccessToken == null) {
            profile_nm.setText("")
            profile_em.setText("")
            circleImageView.setImageResource(0)
            Toast.makeText(this@MainActivity, "User is logged out...", Toast.LENGTH_SHORT)
                .show()
        } else {
            loadUserProfile(currentAccessToken)
        }
    }
}

private fun loadUserProfile(newAccessToken: AccessToken) {
    val request = GraphRequest.newMeRequest(
        newAccessToken
    ) { `object`, response ->
        try {
            val first_name = `object`.getString("first_name")
            val last_name = `object`.getString("last_name")
            val email = `object`.getString("email")
            val id = `object`.getString("id")
            val image_url =
                "https://graph.facebook.con/$id/pictures?type=normal"
            profile_nm.setText("$first_name $last_name")
            profile_em.setText(email)
            val requestOptions = RequestOptions()
            requestOptions.dontAnimate()
            Glide.with(this@MainActivity).load(image_url).into(circleImageView)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    val parameters = Bundle()
    parameters.putString("fields", "first_name, last_name, email_id")
    request.parameters = parameters
    request.executeAsync()
}
*/
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
//            val name: String = account?.displayName.toString()
            view?.signInLoader?.isVisible = false
            val sharedPreferences =
                activity?.getSharedPreferences(Constants.SHARED_TAG, Context.MODE_PRIVATE)
            val intent = Intent(activity, HomeActivity::class.java)
            val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
            editor.putBoolean("LoggedIn", true)
            editor.apply()
            editor.commit()
            startActivity(intent)
            activity?.finish()
        } catch (e: ApiException) {
            view?.signInLoader?.isVisible = false
            if (e.statusCode != 12501) {
                Toast.makeText(activity, "something went wrong", Toast.LENGTH_SHORT).show()
            }
            Log.d("GOOGLE", "google error: ${e.message}")
            Log.w("TAG", "signInResult:failed code= ${e.statusCode}")
        }
    }


}
