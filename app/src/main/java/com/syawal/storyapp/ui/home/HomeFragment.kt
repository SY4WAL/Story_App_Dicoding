package com.syawal.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.syawal.storyapp.R
import com.syawal.storyapp.data.ResultState
import com.syawal.storyapp.data.api.response.ListStoryItem
import com.syawal.storyapp.databinding.FragmentHomeBinding
import com.syawal.storyapp.ui.StoryAdapter
import com.syawal.storyapp.ui.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.getSession().observe(viewLifecycleOwner) {
            Log.d("check token before", it.token)
            if (it.token.isEmpty()) {
                homeViewModel.logout()
                findNavController().navigate(R.id.action_homeFragment_to_welcomeFragment)
            } else {
                getStories()
            }
        }

        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addStoryFragment)
        }

        setMenu()
    }

    private fun setMenu() {
        with(binding) {
            toolbar.inflateMenu(R.menu.home_menu)
            toolbar.title = getString(R.string.app_name)

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_logout -> {
                        homeViewModel.logout()
                        findNavController().navigate(R.id.action_homeFragment_to_welcomeFragment)
                        true
                    }

                    R.id.menu_setting -> {
                        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun getStories() {
        val storyAdapter = StoryAdapter()
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = storyAdapter
        }

        homeViewModel.getStories().observe(viewLifecycleOwner) { result ->

            if (result != null)
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        val storyData = result.data

                        storyAdapter.setOnItemClickCallback(object :
                            StoryAdapter.OnItemClickCallback {
                            override fun onItemClicked(
                                story: ListStoryItem,
                            ) {
                                val position = storyData.indexOf(story)
                                if (position != -1) {
                                    val id = storyData[position].id
                                    val toDetailFragment =
                                        HomeFragmentDirections.actionHomeFragmentToDetailFragment()
                                    toDetailFragment.idStory = id

                                    findNavController().navigate(toDetailFragment)
                                }
                            }
                        })
                        storyAdapter.submitList(storyData)
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                        showToast(result.error)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}