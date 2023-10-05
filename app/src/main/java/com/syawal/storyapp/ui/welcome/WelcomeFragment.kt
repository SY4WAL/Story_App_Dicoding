package com.syawal.storyapp.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.syawal.storyapp.R
import com.syawal.storyapp.databinding.FragmentWelcomeBinding
import com.syawal.storyapp.ui.ViewModelFactory

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private val welcomeViewModel by viewModels<WelcomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        welcomeViewModel.getSession().observe(viewLifecycleOwner) {
            if (it.token.isNotEmpty()) {
                Log.d("check at welcome", it.token)
                findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
            }
        }

        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playAnimation()

        binding.btnLogin.setOnClickListener{
            view.findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }

        binding.btnRegister.setOnClickListener {
            view.findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
        }
    }

    private fun playAnimation() {
        val logo = ObjectAnimator.ofFloat(binding.appLogo, View.ALPHA, 1f).setDuration(150)
        val appName = ObjectAnimator.ofFloat(binding.appName, View.ALPHA, 1f).setDuration(150)
        val tagline = ObjectAnimator.ofFloat(binding.tagline, View.ALPHA, 1f).setDuration(150)
        val desc = ObjectAnimator.ofFloat(binding.descTagline, View.ALPHA, 1f).setDuration(150)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(150)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(150)

        val title = AnimatorSet().apply {
            playTogether(tagline, desc)
        }

        val button = AnimatorSet().apply {
            playTogether(login, register)
        }

        AnimatorSet().apply {
            playSequentially(logo, appName, title, button)
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}