package com.example.shottracker_ai.ui.profile

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import com.example.shottracker_ai.databinding.ProfileFragmentBinding
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
    ): View? {
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
                Timber.d("Enter was pressed")
                true
            } else false
        }
    }

    override fun changeProfileWithDefault(defaultImage: Uri) {
        viewModel.changeProfileImage(defaultImage)

    }

    override fun saveProfileChanges() {
        TODO("Not yet implemented")
    }
}