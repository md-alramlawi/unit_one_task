package com.alramlawi.unitonetask.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alramlawi.unitonetask.R
import com.alramlawi.unitonetask.databinding.FragmentHomeBinding
import com.alramlawi.unitonetask.ui.home.DefaultViewModelProvider.provideHomeViewModelFactory
import me.relex.circleindicator.CircleIndicator

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        provideHomeViewModelFactory(requireContext())
    }

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.isLogin.observe(viewLifecycleOwner) { isLogin ->
            binding.promoLayout.root.isVisible = isLogin
            binding.signupLayout.root.isVisible = !isLogin
        }

        subscribeUi()

        binding.signupLayout.login.setOnClickListener {
            goLogin()
        }
        return root
    }

    private fun goLogin() {
        findNavController().navigate(R.id.action_homeDestination_to_loginFragment)
    }

    private fun subscribeUi() {
        val cityAdapter = CityAdapter()
        binding.rvCities.adapter = cityAdapter
        viewModel.cities.observe(viewLifecycleOwner) { list ->
            Log.d("city_result", list.toString())
            cityAdapter.submitList(list)
        }

        viewModel.sliders.observe(viewLifecycleOwner){list ->
            list?.let {
                val viewPagerAdapter = ViewPagerAdapter(requireContext(), list)
                binding.vpSliders.adapter = viewPagerAdapter
                binding.indicator.setViewPager(binding.vpSliders)
            }

        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}