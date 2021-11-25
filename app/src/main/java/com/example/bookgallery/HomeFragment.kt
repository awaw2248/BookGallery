package com.example.bookgallery

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.bookgallery.adapters.PhotoRecyclerViewAdapter
import com.example.bookgallery.databinding.FragmentHomeBinding
import com.example.bookgallery.viewmodels.PhotosViewModel

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {
    private val viewModel: PhotosViewModel by activityViewModels()
    lateinit var photosAdapter: PhotoRecyclerViewAdapter
    lateinit var binding: FragmentHomeBinding
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
        photosAdapter = PhotoRecyclerViewAdapter(requireActivity())
        binding.recyclerView.adapter = photosAdapter
        viewModel.getPhotos(-73.935242, 40.730610, 2)
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
}