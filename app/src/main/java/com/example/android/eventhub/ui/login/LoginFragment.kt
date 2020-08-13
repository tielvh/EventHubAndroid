package com.example.android.eventhub.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.android.eventhub.R
import com.example.android.eventhub.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )

        val application = requireNotNull(this.activity).application

        val viewModelFactory = LoginViewModelFactory(application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.viewModel = viewModel

        return binding.root
    }
}