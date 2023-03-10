package com.example.funDoNotes.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
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

    private lateinit var googleSignInClient: GoogleSignInClient

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
//        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
//        (activity as MainActivity?)?.setDrawerLocked()

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val tvNewRegister: TextView = view.findViewById(R.id.tvNewResister)
        val forgotpass: TextView = view.findViewById(R.id.forgotpass)

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
            Toast.makeText(context, "Logging In", Toast.LENGTH_SHORT).show()
            signInGoogle()
        }

        forgotpass.setOnClickListener {
            val fragment = ResetPasswordFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
            transaction?.addToBackStack(null)
        }

        tvNewRegister.setOnClickListener {
            val fragment = RegisterFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
            transaction?.addToBackStack(null)
        }

        loginBtn.setOnClickListener {
            validateField()
        }

        return view
    }

    private fun signInGoogle() {
//        val signInIntent: Intent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, 1)
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                val fragment = HomePageFragment()
                val transaction = fragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
            }
        }

    private fun firebaseSignIn() {
        loginBtn.isEnabled = false
        loginBtn.alpha = 0.5f

        var user = User(
            email = "",
            password = loginPassword.text.toString(),
            username = loginUsername.text.toString()
        )
        loginViewModel.loginUser(user)
//        loginViewModel.loginWithApi(user.username, user.password)
        loginViewModel._userLoginStatus.observe(viewLifecycleOwner, Observer {

            if (it.status) {
                Toast.makeText(context, "Successfully with rest api", Toast.LENGTH_SHORT).show()
                val fragment = HomePageFragment()
                val transaction = fragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()

            } else {
                loginBtn.isEnabled = true
                loginBtn.alpha = 1.0f

            }
        })
    }

    private fun validateField() {
        val icon =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_warning_24)
        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)

        when {
            TextUtils.isEmpty(loginUsername.text.toString().trim()) -> {
                loginUsername.setError("Please Enter the username", icon)
            }
            TextUtils.isEmpty(loginPassword.text.toString().trim()) -> {
                loginPassword.setError("Please Enter the password", icon)
            }
            loginUsername.text.toString().isNotEmpty() &&
                    loginPassword.text.toString().isNotEmpty() -> {

                if (Patterns.EMAIL_ADDRESS.matcher(loginUsername.text.toString()).matches()
                ) {
                    firebaseSignIn()
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show()
                } else {
                    loginUsername.setError("Please Enter valid Email", icon)
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity?)?.setDrawerUnlocked()
    }


}