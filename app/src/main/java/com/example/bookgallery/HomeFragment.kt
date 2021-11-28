package com.example.bookgallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.example.bookgallery.adapters.PhotoRecyclerViewAdapter
import com.example.bookgallery.databinding.FragmentHomeBinding
import com.example.bookgallery.datamodels.Photo
import com.example.bookgallery.viewmodels.PhotosViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {
    private val viewModel: PhotosViewModel by activityViewModels()
    private lateinit var photosAdapter: PhotoRecyclerViewAdapter
    private lateinit var binding: FragmentHomeBinding
    val allPhotos = mutableListOf<Photo>()

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
        setHasOptionsMenu(true)

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
                allPhotos.addAll(it)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //inflating the menu that has the search icon
        inflater.inflate(R.menu.navigation_menu, menu)

        //declaring the search item and assigning the search view to another variable to implement search functionality.
        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView

        // The code below to search for the text entered by the user.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!!.isNotEmpty()) {
                    photosAdapter.submitList(
                        allPhotos.filter {
                            it.title.lowercase().contains(query.lowercase())
                        }
                    )
                }

                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return true
            }

        })
        //---------------------------------------------------------------------------//


        // The code below to show the full list of photos when the search bar is collapsed.
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                photosAdapter.submitList(allPhotos)
                return true
            }

        })
        //---------------------------------------------------------------------------//

    }
}

