package com.example.FunDoNotes.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.FunDoNotes.model.User
import com.example.FunDoNotes.model.UserAuthService
import com.example.FunDoNotes.viewmodel.RegisterViewModel
import com.example.FunDoNotes.viewmodel.RegisterViewModelFactory
import com.example.loginandregistrationwithfragment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment(R.layout.fragment_register) {



    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var etEmail: EditText
    private lateinit var firstname: EditText
    private lateinit var lastname: EditText
    private lateinit var setpassword: EditText
    private lateinit var cnfpassword: EditText
    private lateinit var registerBtn: Button

    private lateinit var firebaseAuth: FirebaseAuth

    private var database = Firebase.firestore

    private lateinit var sEmail: String
    private lateinit var sFirstName: String
    private lateinit var sLastName: String
    private lateinit var sPassword: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerViewModel =
            ViewModelProvider(this, RegisterViewModelFactory(UserAuthService())).get(
                RegisterViewModel::class.java
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        val tvLogin: TextView = view.findViewById(R.id.tvLogin)

        firebaseAuth = FirebaseAuth.getInstance()

        etEmail = view.findViewById(R.id.etEmail)
        setpassword = view.findViewById(R.id.setpassword)
        cnfpassword = view.findViewById(R.id.confirmPassword)
        registerBtn = view.findViewById(R.id.registerBtn)
        firstname = view.findViewById(R.id.firstname)
        lastname = view.findViewById(R.id.lasttname)

        tvLogin.setOnClickListener {
            val fragment = LoginFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
        }

        registerBtn.setOnClickListener {
            saveData()
            validateEmptyField()
        }

        return view
    }

    private fun saveData() {

        sEmail = etEmail.text.toString().trim()
        sFirstName = firstname.text.toString().trim()
        sLastName = lastname.text.toString().trim()
        sPassword = setpassword.text.toString().trim()

        val userMap = hashMapOf(
            "email" to sEmail,
            "fristName" to sFirstName,
            "lastName" to sLastName,
            "password" to sPassword
        )

        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection("user").document(userId).set(userMap)
            .addOnSuccessListener {
                Toast.makeText(context,"Successfully added", Toast.LENGTH_SHORT).show()
                etEmail.text.clear()
                firstname.text.clear()
                lastname.text.clear()
                setpassword.text.clear()
            }
            .addOnFailureListener {
                Toast.makeText(context,"Failed to add", Toast.LENGTH_SHORT).show()
            }

    }


    private fun firbaseSugnUp() {
        registerBtn.isEnabled = false
        registerBtn.alpha = 0.5f

        var user = User(
            email = etEmail.text.toString(),
            password = setpassword.text.toString(),
            username = ""
        )
        registerViewModel.registerUSer(user)
        registerViewModel._userRegisterStatus.observe(viewLifecycleOwner, Observer {

            if (it.status) {
                val fragment = HomePageFragment()
                val transaction = fragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
            } else {
                registerBtn.isEnabled = true
                registerBtn.alpha = 1.0f
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("SuspiciousIndentation")
    private fun validateEmptyField() {
        val icon =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_warning_24)
            icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)

        when {
            TextUtils.isEmpty(etEmail.text.toString().trim()) -> {
                etEmail.setError("Please Enter the Email", icon)
            }
            TextUtils.isEmpty(firstname.text.toString().trim()) -> {
                firstname.setError("Please Enter firstname", icon)
            }
            TextUtils.isEmpty(lastname.text.toString().trim()) -> {
                lastname.setError("Please Enter lastname", icon)
            }
            TextUtils.isEmpty(setpassword.text.toString().trim()) -> {
                setpassword.setError("Please Enter the password", icon)
            }
            TextUtils.isEmpty(cnfpassword.text.toString().trim()) -> {
                cnfpassword.setError("Please confirm the password", icon)
            }

            etEmail.text.toString().isNotEmpty() &&
                    firstname.text.toString().isNotEmpty() &&
                    lastname.text.toString().isNotEmpty() &&
                    setpassword.text.toString().isNotEmpty() &&
                    cnfpassword.text.toString().isNotEmpty() -> {

                if (etEmail.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) {
                    if (setpassword.text.toString().length >= 5) {
                        if (setpassword.text.toString() == cnfpassword.text.toString()) {
                            firbaseSugnUp()
                            Toast.makeText(
                                requireContext(),
                                "Register Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            cnfpassword.setError("Password did't match", icon)
                        }
                    } else {
                        setpassword.setError("please Enter at least 5 character", icon)
                    }
                } else {
                    etEmail.setError("Please Enter valid Email", icon)
                }

            }

        }

    }
}

