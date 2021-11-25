package com.example.bookgallery

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

//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


class HomeFragment : Fragment() {
    private val viewModel: PhotosViewModel by activityViewModels()
    lateinit var photosAdapter: PhotoRecyclerViewAdapter
    lateinit var binding: FragmentHomeBinding
    //===============================================
    val LOCATION_PERMISSION_REQ_CODE = 1000
    lateinit var fusedLocationProviderClient:
            FusedLocationProviderClient
    private lateinit var fusedLocationClient:
            FusedLocationProviderClient

    var latitude: Double = 0.0
    var longitude: Double = 0.0
    //==============================================

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


         getCurrentLocation()


        // initialize fused location client--------------------------------------------------------
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        //------------------------------------------------------------------------------------------
        observers()

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        photosAdapter = PhotoRecyclerViewAdapter(requireActivity())
        binding.recyclerView.adapter = photosAdapter

    }

    private fun observers() {
        viewModel.photosLiveData.observe(viewLifecycleOwner, {
            Log.d(TAG, it.toString())
            it?.let {
                photosAdapter.submitList(it)
            }
        })
        viewModel.errorLiveData.observe(viewLifecycleOwner, {
            Log.d(TAG, it)
            Toast.makeText(requireActivity(), it, Toast.LENGTH_LONG).show()
        })
    }


   //-----------------------------------------------------------------------------------------------
    private fun getCurrentLocation(){
        // checking location permission
        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest. permission. ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){

            // request permission
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQ_CODE) ;
                return
        }
       fusedLocationClient.lastLocation
           .addOnSuccessListener { location ->
               // getting the last known or current location
               latitude = location.latitude
               longitude = location.longitude
               Log.d(TAG,"$latitude")
               Log.d(TAG,"$longitude")



           }
           .addOnFailureListener {
               Toast.makeText(requireActivity(), "Failed on getting current location",
                   Toast.LENGTH_SHORT).show()

    }
    }



}