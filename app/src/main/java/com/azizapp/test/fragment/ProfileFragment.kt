package com.azizapp.test.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.azizapp.test.R
import com.azizapp.test.databinding.FragmentProfileBinding
import com.azizapp.test.ui.editpassword.EditPasswordActivity
import com.azizapp.test.ui.login.LoginActivity
import com.azizapp.test.ui.profile.ActivityEditProfile
import com.azizapp.test.ui.profile.EditProfileViewModel
import com.azizapp.test.utill.Session
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private val editProfileViewModel: EditProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)


        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModeEditProfile = editProfileViewModel
        }

        val ubahProfil : RelativeLayout = binding.root.findViewById(R.id.ubahProfil)
        val gantiPassword : RelativeLayout = binding.root.findViewById(R.id.ubahPassword)
        val keluar:RelativeLayout = binding.root.findViewById(R.id.keluar)

        ubahProfil.setOnClickListener {
            val intent = Intent(activity, ActivityEditProfile::class.java)
            startActivity(intent)
        }

        gantiPassword.setOnClickListener {
            val intent = Intent(activity, EditPasswordActivity::class.java)
            startActivity(intent)
        }
        keluar.setOnClickListener{
            Session.bearer = ""
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        editProfileViewModel.onLoad()
        // Inflate the layout for this fragment
        return binding.root
    }
}