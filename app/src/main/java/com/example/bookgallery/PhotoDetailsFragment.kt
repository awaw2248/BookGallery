package com.example.bookgallery

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.bookgallery.databinding.FragmentPhotoDetailsBinding
import com.example.bookgallery.datamodels.Photo
import com.example.bookgallery.viewmodels.PhotosViewModel
import com.squareup.picasso.Picasso

class PhotoDetailsFragment : Fragment() {

    private val photoViewModel:PhotosViewModel by activityViewModels()
    private lateinit var selectedPhoto:ImageView
    private lateinit var binding:FragmentPhotoDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPhotoDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            photoViewModel.selectPhotoMutableLiveData.observe(viewLifecycleOwner,  {
                it?.let { photo ->
                    binding.titlePhotoDetailsTextView.text = photo.title
                    Picasso.get().load(photo.urlS).into(binding.imageViewPhotoDetails)
                    binding.longitudePhotoDetailsTextView.text = photo.lon.toString()
                    binding.latitudePhotoDetailsTextView.text = photo.lat.toString()
                }
            })


    }

}