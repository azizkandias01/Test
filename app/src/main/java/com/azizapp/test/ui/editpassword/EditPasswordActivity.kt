package com.azizapp.test.ui.editpassword

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.azizapp.test.R
import com.azizapp.test.databinding.GantiPasswordLayoutBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPasswordActivity : AppCompatActivity() {

    lateinit var binding: GantiPasswordLayoutBinding
    private val editPasswordViewModel: EditPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ganti_password_layout)
        binding = DataBindingUtil.setContentView(this, R.layout.ganti_password_layout)

        binding.apply {
            lifecycleOwner = this@EditPasswordActivity
            viewModelEditPassword = editPasswordViewModel
        }
        editPasswordViewModel.action.observe(this, Observer { action ->
            when (action) {
                EditPasswordViewModel.ACTION_EDIT_SUCCESS -> editSuccess()
                EditPasswordViewModel.ACTION_EDIT_ERROR -> editError()
                EditPasswordViewModel.ACTION_EDIT_FAILED -> editFailed()
            }
        }
        )

    }

    private fun editFailed() {
        Snackbar.make(binding.root, "Edit Failed", Snackbar.LENGTH_SHORT).show()
    }

    private fun editError() {
        Snackbar.make(binding.root, "Edit Error", Snackbar.LENGTH_SHORT).show()
    }

    private fun editSuccess() {
        Snackbar.make(binding.root, "Edit Success", Snackbar.LENGTH_SHORT).show()
        finish()
    }
}


