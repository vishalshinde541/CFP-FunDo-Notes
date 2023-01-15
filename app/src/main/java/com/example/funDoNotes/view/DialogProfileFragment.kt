package com.example.funDoNotes.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.funDoNotes.model.User
import com.example.loginandregistrationwithfragment.R
import com.example.loginandregistrationwithfragment.databinding.FragmentDialogProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.Date

class DialogProfileFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogProfileBinding
    private lateinit var image: CircleImageView
    private lateinit var email: TextView
    private lateinit var fName: TextView
    private lateinit var lName: TextView
    private lateinit var clickToSaveImg: TextView
    private lateinit var profileImgBtn: CircleImageView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userId: String
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImg: Uri
    private lateinit var dialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDialogProfileBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dialog_profile, container, false)
        var closeBtn = view.findViewById<ImageButton>(R.id.dialog_dismissBtn)

        image = view!!.findViewById(R.id.dialog_circularProfilePic)
        email = view.findViewById(R.id.dialog_email) as TextView
        fName = view.findViewById(R.id.dialog_fname) as TextView
        lName = view.findViewById(R.id.dialog_lname) as TextView
        profileImgBtn = view.findViewById(R.id.dialog_circularProfilePic)
        clickToSaveImg = view.findViewById(R.id.save_profileImg)
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        dialog = AlertDialog.Builder(requireContext())
            .setMessage("Updating profile Image...")
            .setCancelable(false)

        readFirestoreData()

        clickToSaveImg.setOnClickListener {

            if (selectedImg!! == null) {
                Toast.makeText(requireContext(), "Please select your profile image", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage()
            }
        }

        profileImgBtn.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        closeBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dismiss()
            }
        })
        return view
    }

    private fun readFirestoreData() {
        userId = firebaseAuth.currentUser?.uid!!
        val docRef = db.collection("user").document(userId)
        docRef.get()
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    fName.text = it.result.getString("firstName")
                    lName.text = it.result.getString("lastName")
                    email.text = it.result.getString("email")
                    val result = it.result.getString("imageUrl")
                    if (result != null){
                        Glide.with(this).load(it.result.getString("imageUrl")).into(image)
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
    }

    private fun uploadImage() {
        val reference = storage.reference.child("profile").child(Date().time.toString())
        reference.putFile(selectedImg).addOnCompleteListener {
            if (it.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())
                }
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
        userId = firebaseAuth.currentUser?.uid!!
        val docRef = db.collection("user").document(userId)
        docRef.update("imageUrl", imgUrl).addOnCompleteListener {
            Toast.makeText(requireContext(), "user profile image updated", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {

                selectedImg = data.data!!
            }
            profileImgBtn.setImageURI(selectedImg)
        }
    }

    companion object {

    }
}