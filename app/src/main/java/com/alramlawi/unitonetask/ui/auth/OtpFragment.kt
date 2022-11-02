package com.alramlawi.unitonetask.ui.auth

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.alramlawi.unitonetask.databinding.FragmentOtpBinding

class OtpFragment(
    val onEntered: (String) -> Unit
) : DialogFragment() {

    private var _binding: FragmentOtpBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpBinding.inflate(inflater, container, false)

        binding.confirm.setOnClickListener {
            val opt = binding.edtOtp.text.toString()
            if(opt.length == 6){
                onEntered(opt)
                dismiss()
            }else{
                binding.edtOtp.requestFocus()
                binding.edtOtp.error = "Enter 6-digits code"
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (Resources.getSystem().displayMetrics.widthPixels * 0.9).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}