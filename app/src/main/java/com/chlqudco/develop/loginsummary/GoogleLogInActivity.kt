package com.chlqudco.develop.loginsummary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.chlqudco.develop.loginsummary.databinding.ActivityGoogleLogInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class GoogleLogInActivity : AppCompatActivity() {

    private val binding: ActivityGoogleLogInBinding by lazy { ActivityGoogleLogInBinding.inflate(layoutInflater) }

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private lateinit var client: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        //로그인 되어 있으면 로그아웃 후 초기화
        if (firebaseAuth.currentUser != null){ signOut() }
        initLogIn()

        // 요청 정보 옵션
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        client = GoogleSignIn.getClient(this, options)

        //로그인 버튼 리스너
        binding.loginButton.setOnClickListener {
            startActivityForResult(client.signInIntent, 1)
        }

        //로그아웃 버튼 리스너
        binding.logoutButton.setOnClickListener {
            signOut()
            initLogIn()
        }
    }

    //로그아웃 비지니스 로직
    private fun signOut() {
        firebaseAuth.signOut()
        val opt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val client = GoogleSignIn.getClient(this, opt)
        client.signOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            var account: GoogleSignInAccount? = null
            try {
                //계정 잘 넘어 왔으면 Firebase에 로그인 시도
                account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!.idToken)
            } catch (e: ApiException) {
                Toast.makeText(this, "Failed Google Login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Firebase에 로그인
    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    successLogIn(FirebaseAuth.getInstance().currentUser)
                }
            }
    }

    //로그인 성공
    private fun successLogIn(user: FirebaseUser?){
        binding.profileImageView.setImageURI(user?.photoUrl)
        binding.userNameTextView.text = user?.displayName
        binding.loginTextView.text = "로그인 됨"
        binding.logoutButton.isEnabled = true
        binding.loginButton.isEnabled = false
        binding.profileGroup.visibility = View.VISIBLE
    }

    //로그아웃 초기화
    private fun initLogIn(){
        binding.profileImageView.setImageURI(null)
        binding.userNameTextView.text = ""
        binding.loginTextView.text = "로그인 안됨"
        binding.logoutButton.isEnabled = false
        binding.loginButton.isEnabled = true
    }

}