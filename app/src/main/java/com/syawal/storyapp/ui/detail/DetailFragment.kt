package com.syawal.storyapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.syawal.storyapp.data.ResultState
import com.syawal.storyapp.databinding.FragmentDetailBinding
import com.syawal.storyapp.ui.viewmodelfactory.StoryViewModelFactory

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val detailViewModel by viewModels<DetailViewModel> {
        StoryViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val sharedElementName = arguments?.getString("photo")
        val imgView = binding.ivDetailPhoto
        ViewCompat.setTransitionName(imgView, sharedElementName)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idStory = DetailFragmentArgs.fromBundle(arguments as Bundle).idStory

        val idFromWidget = arguments?.getString(EXTRA_ID) ?: ""
        if (idFromWidget.isNotEmpty()) {
            getDetailStory(idFromWidget)
        } else {
            getDetailStory(idStory)
        }
    }

    private fun getDetailStory(id: String) {
        detailViewModel.getDetailStory(id).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        val detailStory = result.data
                        binding.tvDetailName.text = detailStory.name
                        binding.tvDetailDesc.text = detailStory.description
                        Glide.with(binding.root)
                            .load(detailStory.photoUrl)
                            .into(binding.ivDetailPhoto)
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}