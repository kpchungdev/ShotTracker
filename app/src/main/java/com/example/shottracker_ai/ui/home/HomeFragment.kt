package com.example.shottracker_ai.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.shottracker_ai.databinding.HomeFragmentBinding
import com.example.shottracker_ai.result.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel.launchDestination.observe(viewLifecycleOwner, EventObserver { destination ->
            when (destination) {
                LaunchDestination.PROFILE_SCREEN -> {
                    val direction = HomeFragmentDirections.actionFirstScreenHomeFragmentToProfileFragment()
                    view.findNavController().navigate(direction)
                }
            }
        })
    }

}

//EventObserver<LaunchDestination> { destination ->
//    when (destination) {
//        LaunchDestination.PROFILE_SCREEN -> {
//            val direction = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
//            view.findNavController().navigate(direction)
//        }
//    }
//}