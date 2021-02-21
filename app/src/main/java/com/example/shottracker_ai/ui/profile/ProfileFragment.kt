package com.example.shottracker_ai.ui.profile

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.shottracker_ai.R
import com.example.shottracker_ai.databinding.ProfileFragmentBinding
import com.example.shottracker_ai.ui.home.HomeFragmentDirections
import com.example.shottracker_ai.ui.home.LaunchDestination
import com.example.shottracker_ai.utilities.Keyboard
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

interface EventHandler {
    fun changeProfileWithDefault(defaultImage: Uri)
    fun saveProfileChanges()
}

@AndroidEntryPoint
class ProfileFragment : Fragment(), EventHandler {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: ProfileFragmentBinding


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileViewModel = viewModel
        binding.eventHandler = this
        binding.lifecycleOwner = this

        binding.editName.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Keyboard.hideKeyboard(this.requireContext(), binding.root)

                if (viewModel.canSaveProfile.value == true) {
                    saveProfileChanges()
                }

                true
            } else false
        }

        binding.editName.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                Keyboard.hideKeyboard(this.requireContext(), binding.root)
            }
        }

    }

    override fun changeProfileWithDefault(defaultImage: Uri) {
        viewModel.changeProfileImage(defaultImage)

    }

    override fun saveProfileChanges() {
        viewModel.saveProfile.observe(this.viewLifecycleOwner) {
            val direction = ProfileFragmentDirections.actionProfileToHomeFragment()
            view?.findNavController()?.navigate(direction)
        }
    }
}