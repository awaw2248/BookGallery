package com.example.bookgallery

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.bookgallery.adapters.PhotoRecyclerViewAdapter
import com.example.bookgallery.databinding.FragmentHomeBinding
import com.example.bookgallery.viewmodels.PhotosViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.internal.IGoogleMapDelegate
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Marker




private const val TAG = "HomeFragment"
private const val MAP_REQUEST_CODE = 10

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
        photosAdapter = PhotoRecyclerViewAdapter(requireActivity(), viewModel)
        binding.recyclerView.adapter = photosAdapter
//-----------------------------------------------------------------------------------------------
        binding.btOpenMap.setOnClickListener { openMap() }





        getCurrentLocation()
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {

        }

    }

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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

        // checking location permission
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
//===================================================================================================
    override fun onRequestPermissionsResult(

        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                        grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    // permission granted
                } else {
                    //permission denied
                    Toast.makeText(requireActivity(), "You need to grant permission to access location", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private fun openMap(){
        val uri = Uri.parse("geo:${latitude},${longitude}")

        val mapIntent = Intent(Intent.ACTION_VIEW, uri)

        mapIntent.setPackage("com.google.android.apps.maps")

        startActivityForResult(mapIntent, MAP_REQUEST_CODE)
    }



    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK){

        }

    }

    private lateinit var mMap: GoogleMap  //declaration inside class

     fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.position
            } else {
                marker.position
            }
            true
        }
    }
    //==============================================================================================

}


