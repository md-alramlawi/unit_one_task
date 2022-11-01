package com.alramlawi.unitonetask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alramlawi.unitonetask.databinding.FragmentFlightsBinding
import com.alramlawi.unitonetask.databinding.FragmentHomeBinding

class FlightsFragment : Fragment() {

    private var _binding: FragmentFlightsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlightsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}