package com.alramlawi.unitonetask.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alramlawi.unitonetask.R
import com.alramlawi.unitonetask.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.close.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.send.setOnClickListener {
            login()
        }

        startAuthObserver()
        return root
    }

    private fun startAuthObserver(){
        viewModel.isLogin.observe(viewLifecycleOwner){ isLogin ->
            if(isLogin){
                findNavController().navigate(R.id.action_loginDestination_to_homeFragment)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner){
            it?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.showOptDialog.observe(viewLifecycleOwner){showDialog ->
            if(showDialog){
                OtpFragment{
                    viewModel.verifyPhoneNumberWithCode(requireActivity(), it)
                }.show(parentFragmentManager, "show_dialog")
                viewModel.notifyDialogIsShown()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun login(){
        val phone = binding.edtPhone.text.toString().trim()

        when (phone.length) {
            9 -> {
                val phoneNumber = "+9700$phone"
                viewModel.sendCode(requireActivity(), phoneNumber)
            }
            10 -> {
                val phoneNumber = "+970$phone"
                viewModel.sendCode(requireActivity(), phoneNumber)
            }
            else -> {
                binding.edtPhone.requestFocus()
                binding.edtPhone.error = "Enter valid number"
            }
        }
    }

}