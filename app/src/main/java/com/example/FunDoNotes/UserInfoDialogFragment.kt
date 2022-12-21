package com.example.FunDoNotes

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.loginandregistrationwithfragment.R

class UserInfoDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.order_UserInfo))
            .create()

    companion object {
        const val TAG = "UserInfoDialog"
    }
}