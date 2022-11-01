package com.alramlawi.unitonetask.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alramlawi.unitonetask.R
import com.alramlawi.unitonetask.databinding.FragmentLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class LoginFragment : Fragment() {


    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()
        binding.close.setOnClickListener {
            goHome()
        }

        binding.send.setOnClickListener {
            login()
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
                Log.d("login_callback_1", credential.toString())
            }
            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("login_callback_2", e.message.toString())
            }
            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                storedVerificationId = verificationId
                resendToken = token
                OtpFragment{
                    verifyPhoneNumberWithCode(
                        verificationId = storedVerificationId,
                        code = it
                    )
                }.show(parentFragmentManager, "show_dialog")
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                Log.d("login_callback_3", p0)
            }
        }

        return root
    }

    private fun goHome() {
        findNavController().navigate(R.id.action_loginDestination_to_homeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun login(){
        val phone = binding.edtPhone.text.toString()

        if(phone.length >= 9){
            val phoneNumber = "+970$phone".trim()
            sendCode(phoneNumber)

        }else{
            binding.edtPhone.requestFocus()
            binding.edtPhone.error = "Enter valid number"
        }
    }

    private fun sendCode(phone: String){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }



    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    if(user != null){
                        goHome()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        task.exception?.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }


}