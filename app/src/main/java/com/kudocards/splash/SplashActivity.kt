package com.kudocards.splash

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kudocards.R
import com.google.firebase.auth.FirebaseAuth
import com.firebase.ui.auth.AuthUI
import java.util.*
import android.content.Intent
import com.kudocards.cards.CardsActivity

/**
 * Created by marcus on 26/12/17.
 */
class SplashActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkIsLogged()
    }

    private fun checkIsLogged() {
        // Choose authentication providers
        val providers = Arrays.asList(
                AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())

        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            // Create and launch sign-in intent
            startListCardsActivity()
        } else {
            // not signed in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(false)
                            .build(), RC_SIGN_IN)
        }
    }

    private fun startListCardsActivity() {
        val intent = Intent(this, CardsActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                if (FirebaseAuth.getInstance().currentUser != null) {
                    startListCardsActivity()
                }
            } else {
                // Sign in failed, check response for error code
                finish()
            }

        }
    }

}