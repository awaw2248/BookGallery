package com.example.bookgallery

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.bookgallery.databinding.ActivityMainBinding

import com.example.bookgallery.repositories.RoomServiceRepository
import com.example.bookgallery.viewmodels.PhotosViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy

val LOCATION_PERMISSION_REQ_CODE = 1000
val IMAGE_REQUEST_CODE = 200
val STORAGE_PERMISSION_REQUEST_CODE = 300


class MainActivity : AppCompatActivity() {


    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val photosViewModel: PhotosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RoomServiceRepository.init(this)
        checkPermission()


//
//        val homeFragment = HomeFragment()
//        val favoriteFragment = FavoriteFragment()
//
//        makeCurrentFragment(homeFragment)

        // here when ever we press on the favorite navigation bottom we will transfer to that fragment
//        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.home -> makeCurrentFragment(homeFragment)
//                supportFragmentManager.
//                R.id.favorite -> makeCurrentFragment(favoriteFragment)
//            }
//            true
//        }
        val bottomNavigation:BottomNavigationView = findViewById(R.id.bottom_app_bar)
        val navigationHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navigationHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavigation, navController)

        setupActionBarWithNavController(navController)
        binding.uploadPhotoButton.setOnClickListener {
            pickImage()
        }


    }


    private fun checkPermission() {
        // checking location permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {

            // request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE
            )
            return
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    photosViewModel.permission.postValue(true)
                    sendStoragePermission()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQ_CODE
                    )
                }
            }
            STORAGE_PERMISSION_REQUEST_CODE -> {

                if ((!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))) {
                    sendStoragePermission()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    //create a function to allow the user to pick an image to upload.
    private fun pickImage() {
        Matisse.from(this).choose(MimeType.ofImage(), false)
            .capture(true)
            .captureStrategy(CaptureStrategy(true, "com.example.bookgallery"))
            .forResult(IMAGE_REQUEST_CODE)
    }

    //this function is used to send storage and camera permission.
    private fun sendStoragePermission() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED

            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED

            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,

                    Manifest.permission.WRITE_EXTERNAL_STORAGE,

                    Manifest.permission.CAMERA
                ),
                STORAGE_PERMISSION_REQUEST_CODE
            )
        }
    }
}