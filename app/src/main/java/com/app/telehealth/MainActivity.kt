package com.app.telehealth

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.app.telehealth.fragments.Introduction.IntroFragment1
import com.app.telehealth.fragments.Introduction.IntroFragment2
import com.app.telehealth.fragments.Introduction.IntroFragment3
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

private const val mInterval = 3000.toLong()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = IntroFragment1()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val sharedPreferences = this.getSharedPreferences(Constants.SHARED_TAG, Context.MODE_PRIVATE)
        if(sharedPreferences.getBoolean("LoggedIn", false)){
            Constants.navigateTo(this, HomeActivity(), true)
        }
        nextIntroduction.setOnClickListener {
            val fragmentPosition = supportFragmentManager.findFragmentById(R.id.mainActivityFrameLayout)
            val currentFragment = fragmentPosition.toString().substring(13,14).toInt()
            when(currentFragment){
                1 -> Constants.navigateFragment(IntroFragment2(), this, R.id.mainActivityFrameLayout)
                2 -> Constants.navigateFragment(IntroFragment3(), this, R.id.mainActivityFrameLayout)

            }
        }
        previousIntroduction.setOnClickListener {
            val fragmentPosition = supportFragmentManager.findFragmentById(R.id.mainActivityFrameLayout)
            val currentFragment = fragmentPosition.toString().substring(13,14).toInt()
            when(currentFragment){
                3 -> Constants.navigateFragment(IntroFragment2(), this, R.id.mainActivityFrameLayout)
                2 -> Constants.navigateFragment(IntroFragment1(), this, R.id.mainActivityFrameLayout)
            }
        }
        transaction.replace(R.id.mainActivityFrameLayout, fragment)
            .addToBackStack(null)
            .commit()
//        JavaHelper.printHashKey(this)
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            navigateToHome(true);
        }
        if (FirebaseAuth.getInstance().currentUser != null) {
            navigateToHome(true)
        }

        signUp.setOnClickListener {
            navigateToHome(false, "signup")
        }
        signIn.setOnClickListener {
            navigateToHome(false)
        }


    }

    fun navigateToHome(signedIn: Boolean, location: String? = "signin") {
        if (signedIn) {
            Constants.navigateTo(this, HomeActivity(), true)
//            val intent = Intent(this, HomeActivity::class.java)
//            intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//            finish()
        } else {
            Constants.navigateTo(this, SignInActivity(), true, putExtra =  location)

//            val intent = Intent(this, SignInActivity::class.java)
//            intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
//            intent.putExtra("buttonName", location)
//            startActivity(intent)
//            this.finish()
        }

    }


}
