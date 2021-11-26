package com.example.bookgallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.example.bookgallery.adapters.PhotoRecyclerViewAdapter
import com.example.bookgallery.databinding.FragmentHomeBinding
import com.example.bookgallery.viewmodels.PhotosViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {
    private val viewModel: PhotosViewModel by activityViewModels()
   private  lateinit var photosAdapter: PhotoRecyclerViewAdapter
    private lateinit var binding: FragmentHomeBinding

    //===============================================

    private lateinit var fusedLocationClient:
            FusedLocationProviderClient

   private var latitude: Double = 0.0
   private var longitude: Double = 0.0
    //==============================================

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observers()
        // initialize fused location client--------------------------------------------------------
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        //------------------------------------------------------------------------------------------
        photosAdapter = PhotoRecyclerViewAdapter(requireActivity())
        binding.recyclerView.adapter = photosAdapter

        getCurrentLocation()

    }

    private fun observers() {
        viewModel.photosLiveData.observe(viewLifecycleOwner, {
            Log.d(TAG, it.toString())
            it?.let {
                binding.progressBar.visibility = View.INVISIBLE
                photosAdapter.submitList(it)
            }
        })
        viewModel.errorLiveData.observe(viewLifecycleOwner, {
            Log.d(TAG, it)
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()

        })
        viewModel.permission.observe(viewLifecycleOwner, {
            if (it == true) {
                getCurrentLocation()
            }
        })
    }

    //-----------------------------------------------------------------------------------------------
    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    // getting the last known or current location
                    latitude = location.latitude
                    longitude = location.longitude
                    viewModel.getPhotos(longitude, latitude, 1)
                    Log.d(TAG, "$latitude")
                    Log.d(TAG, "$longitude")
                }
                .addOnFailureListener {
                    Toast.makeText(
                        requireActivity(), "Failed on getting current location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}

