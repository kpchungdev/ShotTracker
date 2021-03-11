package com.example.shottracker_ai.ui.tutorial.instructions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shottracker_ai.databinding.InstructionsImageFirstFragmentBinding
import com.example.shottracker_ai.databinding.InstructionsTitleFirstFragmentBinding
import com.example.shottracker_ai.utilities.ARGS_IMAGE_RES
import com.example.shottracker_ai.utilities.ARGS_INSTRUCTION_TYPE
import com.example.shottracker_ai.utilities.ARGS_SUBTITLE
import com.example.shottracker_ai.utilities.ARGS_TITLE

class InstructionsFragment : Fragment() {

    companion object {
        fun newInstance(imageRes: Int, title: String, subtitle: String, type: InstructionType): InstructionsFragment {
            val args = Bundle().apply {
                putInt(ARGS_IMAGE_RES, imageRes)
                putString(ARGS_TITLE, title)
                putString(ARGS_SUBTITLE, subtitle)
                putSerializable(ARGS_INSTRUCTION_TYPE, type)
            }

            return InstructionsFragment().also {
                it.arguments = args
            }
        }
    }


    private val instructionType
        get() = arguments?.getSerializable(ARGS_INSTRUCTION_TYPE) as InstructionType

    private val imageRes
        get() = arguments?.getInt(ARGS_IMAGE_RES) ?: error("imageRes was not passed in")

    private val title
        get() = arguments?.getString(ARGS_TITLE) ?: error("title was not passed in")

    private val subtitle
        get() = arguments?.getString(ARGS_SUBTITLE) ?: error("subtitle was not passed in")

    private lateinit var bindingTitleFirst: InstructionsTitleFirstFragmentBinding
    private lateinit var bindingImageFirst: InstructionsImageFirstFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = when (instructionType) {
            InstructionType.title_first -> {
                InstructionsTitleFirstFragmentBinding.inflate(inflater, container, false).also {
                    bindingTitleFirst = it
                }
            }
            InstructionType.image_first -> {
                InstructionsImageFirstFragmentBinding.inflate(inflater, container, false).also {
                    bindingImageFirst = it
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (instructionType) {
            InstructionType.title_first -> {
                bindingTitleFirst.imageRes = imageRes
                bindingTitleFirst.title = title
                bindingTitleFirst.subtitle = subtitle
                bindingTitleFirst.lifecycleOwner = this
            }
            InstructionType.image_first -> {
                bindingImageFirst.imageRes = imageRes
                bindingImageFirst.title = title
                bindingImageFirst.subtitle = subtitle
                bindingImageFirst.lifecycleOwner = this
            }
        }
    }


}

enum class InstructionType {
    title_first, image_first
}