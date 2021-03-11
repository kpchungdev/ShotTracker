package com.example.shottracker_ai.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.shottracker_ai.databinding.HomeFragmentBinding
import com.example.shottracker_ai.ui.home.performance.setUp
import com.example.shottracker_ai.ui.home.ranges.EventHandler
import com.example.shottracker_ai.ui.home.ranges.setUp
import com.example.shottracker_ai.ui.home.stats.StatRange
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.ranges.setUp(
                fragment = this,
                eventHandler = object : EventHandler {
                    override fun changeRange(range: StatRange) {
                        viewModel.changeRange(range)
                    }
                },
                section = viewModel.rangeSection
        )

        binding.fieldGoalChart.setUp(
            this,
            viewModel.averageFieldGoalChartSection
        )

        binding.buttonPlay.setOnClickListener {
            viewModel.clearPerformances()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.launchDestination.observe(viewLifecycleOwner) { destination ->
            when (destination) {
                LaunchDestination.PROFILE_SCREEN -> {
                    val direction = HomeFragmentDirections.actionFirstScreenHomeFragmentToProfileFragment()
                    view.findNavController().navigate(direction)
                }
            }
        }
    }

}