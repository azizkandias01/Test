package com.azizapp.test.ui.laporan

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.azizapp.test.R
import com.azizapp.test.databinding.FragmentLaporanBinding
import com.azizapp.test.utill.Session
import com.azizapp.test.utill.snackbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*
import kotlinx.android.synthetic.main.fragment_laporan.*
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class LaporanFragment @Inject constructor(private val typeUser: String) : Fragment() {

    lateinit var binding: FragmentLaporanBinding
    private val laporanViewModel: LaporanViewModel by viewModels()

    private var imageUri: Uri? = null
    var sImage: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        pilihLaporan()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_laporan, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModelLaporan = laporanViewModel
        }

        binding.editTextNamaJalan.setOnClickListener {
            val intent = Intent(activity, LaporanActivity::class.java)
            startActivityForResult(intent, 100)
        }

        binding.editGambar.setOnClickListener {
            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                startActivityForResult(it, 1)
            }
        }
        binding.buttonLapor.setOnClickListener {
            when (typeUser) {
                "login" -> uploadImage()
                "anonim" -> uploadImageAnonym()
            }
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            val lat = data.getDoubleExtra("LAT", 0.0)
            val long = data.getDoubleExtra("LONG", 0.0)
            editTextNamaJalan.setText(data.getStringExtra("ADDRESS"))
            editTextLokasi.setText("[$lat,$long]")
        } else if (requestCode == 1 && data != null) {
            imageUri = data.data
            editGambar.setImageURI(imageUri)
            val bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

            val bytes = byteArrayOutputStream.toByteArray()
            sImage = Base64.encodeToString(bytes, Base64.DEFAULT)

        }
    }


    fun pilihLaporan(): String {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        val dialog = BottomSheetDialog(requireActivity())
        var jenis = ""
        dialog.setContentView(view)
        dialog.show()

        view.titik_tersumbat.setOnClickListener {
            jenis = "Titik Tersumbat"
            Toast.makeText(
                activity,
                "Anda melaporkan $jenis", Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
            tv_laporkan.text = "Laporkan $jenis"
        }

        view.titik_banjir.setOnClickListener {
            jenis = "Titik Banjir"
            Toast.makeText(
                activity,
                "Anda melaporkan $jenis", Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
            tv_laporkan.text = "Laporkan $jenis"
        }
        return jenis
    }

    private fun uploadImage() {
        val bearer = "Bearer " + Session.bearer

        if (imageUri == null) {
            this.view?.snackbar("Select an Image First")
            return
        }

        val geometry = "{\"type\": \"Point\", \"coordinates\": ${editTextLokasi.text}}"
        val namaJalan = editTextNamaJalan.text.toString()
        val image = sImage
        val tipePengaduan = tv_laporkan.text.toString().substring(15)
        val deskripsi = editTextDeskripsi.text.toString()
        val statusPengaduan = "Belum diverifikasi"

        laporanViewModel.uploadLaporan(bearer,namaJalan,image,deskripsi,tipePengaduan,geometry,statusPengaduan)
        laporanViewModel.loadingEnable.observe(viewLifecycleOwner,{
            if (it) {
                binding.pbLoginLoading.visibility = View.VISIBLE
                binding.buttonLapor.visibility = View.GONE
            }else{
                binding.pbLoginLoading.visibility = View.GONE
                binding.buttonLapor.visibility = View.VISIBLE
            }
        })
        laporanViewModel.action.observe(viewLifecycleOwner,{
            when(it){
                LaporanViewModel.ACTION_SUCCESS -> {
                    val intent = Intent(activity, SuccessPage::class.java)
                    intent.putExtra("type", "login")
                    startActivity(intent)
                }
                LaporanViewModel.ACTION_ERROR -> actionError()
                LaporanViewModel.ACTION_FAILED -> actionFailed()
            }
        })

    }

    private fun uploadImageAnonym() {
        if (imageUri == null) {
            this.view?.snackbar("Select an Image First")
            return
        }

        val geometry = "{\"type\": \"Point\", \"coordinates\": ${editTextLokasi.text}}"
        val namaJalan = editTextNamaJalan.text.toString()
        val image = sImage
        val tipePengaduan = tv_laporkan.text.toString().substring(15)
        val deskripsi = editTextDeskripsi.text.toString()
        val statusPengaduan = "Belum diverifikasi"

        laporanViewModel.uploadLaporanAnonymous(namaJalan,image,deskripsi,tipePengaduan,geometry,statusPengaduan)
        laporanViewModel.loadingEnable.observe(viewLifecycleOwner,{
            if (it) {
                binding.pbLoginLoading.visibility = View.GONE
                binding.buttonLapor.visibility = View.VISIBLE
            }else{
                binding.pbLoginLoading.visibility = View.VISIBLE
                binding.buttonLapor.visibility = View.GONE
            }
        })
        laporanViewModel.action.observe(viewLifecycleOwner,{
            when(it){
                LaporanViewModel.ACTION_SUCCESS -> {
                    val intent = Intent(activity, SuccessPage::class.java)
                    intent.putExtra("type", "anonim")
                    startActivity(intent)
                }
                LaporanViewModel.ACTION_ERROR -> actionError()
                LaporanViewModel.ACTION_FAILED -> actionFailed()
            }
        })
    }

    private fun actionFailed() {
        this.view?.snackbar("Upload Failed")
    }

    private fun actionError() {
        this.view?.snackbar("Upload Error")
    }
}


