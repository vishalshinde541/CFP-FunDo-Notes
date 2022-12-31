package com.example.funDoNotes.view

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.funDoNotes.model.User
import com.example.funDoNotes.model.UserAuthService
import com.example.funDoNotes.viewmodel.LoginViewModelFactory
import com.example.funDoNotes.viewmodel.LoginVieweModel
import com.example.loginandregistrationwithfragment.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var loginViewModel: LoginVieweModel
    private lateinit var loginUsername: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginBtn: Button
    private lateinit var gooleBtn: ImageView
    private lateinit var fAuth: FirebaseAuth

    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(UserAuthService())).get(
            LoginVieweModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val tvNewRegister : TextView = view.findViewById(R.id.tvNewResister)
        val forgotpass : TextView = view.findViewById(R.id.forgotpass)

        loginUsername = view.findViewById(R.id.loginUsername)
        loginPassword = view.findViewById(R.id.loginPassword)
        loginBtn = view.findViewById(R.id.loginBtn)
        gooleBtn = view.findViewById(R.id.googleBtn)
        fAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        gooleBtn.setOnClickListener {
            signInGoogle()
        }

        forgotpass.setOnClickListener {
            val fragment = ResetPasswordFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
        }

        tvNewRegister.setOnClickListener {
            val fragment = RegisterFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
        }

        loginBtn.setOnClickListener {
            validateField()
        }

        return view
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val fragment = HomePageFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
        }
    }

    private fun firebaseSignIn(){
        loginBtn.isEnabled = false
        loginBtn.alpha = 0.5f

        var user = User(
            email = "",
            password = loginPassword.text.toString(),
            username = loginUsername.text.toString()
        )
        loginViewModel.loginUser(user)
        loginViewModel._userLoginStatus.observe(viewLifecycleOwner, Observer {

            if (it.status){
                val fragment = HomePageFragment()
                val transaction = fragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragmentsContainer,fragment)?.commit()
            }else{
                loginBtn.isEnabled = true
                loginBtn.alpha = 1.0f
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun validateField(){
        val icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_warning_24)
        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)

        when{
            TextUtils.isEmpty(loginUsername.text.toString().trim())->{
                loginUsername.setError("Please Enter the username", icon)
            }
            TextUtils.isEmpty(loginPassword.text.toString().trim())->{
                loginPassword.setError("Please Enter the password", icon)
            }
            loginUsername.text.toString().isNotEmpty() &&
                    loginPassword.text.toString().isNotEmpty() -> {

                if (loginUsername.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) {
                    firebaseSignIn()
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show()
                }else{
                    loginUsername.setError("Please Enter valid Email", icon)
                }
            }

        }
    }


}