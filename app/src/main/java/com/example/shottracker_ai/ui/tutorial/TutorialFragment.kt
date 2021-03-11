package com.example.shottracker_ai.ui.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.shottracker_ai.databinding.TutorialFragmentBinding
import com.example.shottracker_ai.ui.tutorial.instructions.InstructionType
import com.example.shottracker_ai.ui.tutorial.instructions.InstructionsFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TutorialFragment : Fragment() {


    private lateinit var binding: TutorialFragmentBinding

    private val viewModel: TutorialViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TutorialFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val viewpager = binding.viewpager
        val tabLayout = binding.tabLayout

        viewpager.adapter = TutorialAdapter(
            this,
            viewModel.instructions
        )
        TabLayoutMediator(tabLayout, viewpager) { _, _ -> }.attach()

        binding.buttonNext.setOnClickListener {
            if (viewModel.isLastPage()) {
                viewModel.markReadTutorial()

                val direction = TutorialFragmentDirections.actionTutorialFragmentToPlayFragment()
                view.findNavController().navigate(direction)
            } else viewModel.nextPage()
        }
    }

}

data class TutorialInstructions(
    val imageRes: Int,
    val title: String,
    val subtitle: String
)

class TutorialAdapter constructor(
    fragment: Fragment,
    private val instructions: List<TutorialInstructions>
) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return InstructionsFragment.newInstance(
            imageRes = instructions[position].imageRes,
            title = instructions[position].title,
            subtitle = instructions[position].subtitle,
            type = if (position % 2 == 0) InstructionType.title_first else InstructionType.image_first
        )
    }

    override fun getItemCount(): Int {
        return instructions.size
    }
}