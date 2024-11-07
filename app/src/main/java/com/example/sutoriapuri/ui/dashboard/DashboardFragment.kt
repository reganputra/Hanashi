package com.example.sutoriapuri.ui.dashboard

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.sutoriapuri.R
import com.example.sutoriapuri.data.Result
import com.example.sutoriapuri.data.ViewModelFactory
import com.example.sutoriapuri.databinding.FragmentDashboardBinding
import com.example.sutoriapuri.util.Util
import com.example.sutoriapuri.util.Util.reduceFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private var currentImage: Uri? = null
    private val uploadViewModel: DashboardViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(false)
        setupButton()
        setupUpload()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                showImage()
            } else {
                currentImage = null
                Toast.makeText(activity, "No Picture Selected", Toast.LENGTH_SHORT).show()
            }
        }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            currentImage = uri
            showImage()
        } else {
            Toast.makeText(activity, "No Media Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImage() {
        currentImage?.let {
            binding.imgStory.setImageURI(it)
        }
    }

    private fun startCamera() {
        currentImage = Util.getImageUri(requireContext())
        launcherCamera.launch(currentImage!!)
    }

    private fun setupButton() {
        binding.btnGallery.setOnClickListener {
            // Galeri
            startGallery()
        }

        binding.btnCamera.setOnClickListener{
            // Camera
            startCamera()
        }


    }

    private fun setupUpload(){
        binding.buttonUpload.setOnClickListener {
            currentImage?.let { uri ->
                val image = Util.uriToFile(uri, requireContext()).reduceFileImage()
                val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "photo",
                    image.name,
                    requestImageFile
                )
                val getDescription = binding.edDescription.text.toString()
                val requestBody = getDescription.toRequestBody("text/plain".toMediaType())

                uploadViewModel.uploadStory(multipartBody, requestBody).observe(viewLifecycleOwner)
                { result ->
                    when(result){
                        is Result.Loading -> showLoading(true)

                        is Result.Success -> {
                            showLoading(false)
                            AlertDialog.Builder(requireContext())
                                .setTitle("Yeeey!")
                                .setMessage(result.data.message)
                                .setPositiveButton("Ok"){_,_ ->
                                    findNavController().navigate(R.id.navigation_home)
                                }
                                .show()
                        }

                        is Result.Error ->{
                            showLoading(false)
                            Toast.makeText(requireContext(), "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                        }
                    }

                }

            }?: Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_SHORT).show()
        }

    }



    private fun showLoading(isLoading: Boolean) {
        binding.pbUpload.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}