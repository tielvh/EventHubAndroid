package com.example.android.eventhub.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.android.eventhub.R
import com.example.android.eventhub.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAccountBinding>(
            inflater,
            R.layout.fragment_account,
            container,
            false
        )

        val application = requireNotNull(activity).application

        val viewModelFactory = AccountViewModelFactory(application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AccountViewModel::class.java)

        viewModel.user.observe(viewLifecycleOwner, Observer {
            if (it == null) findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToLoginFragment())
        })

        viewModel.navigateBack.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().popBackStack()
                viewModel.doneNavigating()
            }
        })

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }
}