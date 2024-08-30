package com.example.noteapp.ui.fragments.onboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.viewpager2.widget.ViewPager2
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentOnBoardBinding
import com.example.noteapp.ui.adapter.onBoardAdapter
import com.example.noteapp.utils.PreferenceHelper
import com.google.android.material.tabs.TabLayoutMediator

class onBoardFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardBinding
    private lateinit var sharedPreferences: PreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnBoardBinding.inflate(inflater,container,false)
        sharedPreferences = PreferenceHelper()
        sharedPreferences.unit(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initalize()
        setupListener()
        setupListener2()
        setupTabLayout()
        binding.tvWork.setOnClickListener {
            sharedPreferences.onBoardShow = true // Устанавливаем флаг после завершения онбординга
            findNavController().navigate(R.id.action_onBoardFragment_to_signInFragment)
        }
    }

    private fun setupListener2() = with(binding.vpViewpager2) {
        registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 2) {
                    binding.tvWork.visibility = View.VISIBLE
                } else {
                    binding.tvWork.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun initalize() {
        binding.vpViewpager2.adapter = onBoardAdapter(this)
    }

    private fun setupListener() = with(binding.vpViewpager2) {
        registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 2) {
                    binding.tvSkip.visibility = View.INVISIBLE
                } else {
                    binding.tvSkip.visibility = View.VISIBLE
                }
            }
        })
        binding.tvSkip.setOnClickListener {
            if (currentItem < 3)
                setCurrentItem(currentItem + 2, true)
        }
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.vpViewpager2) { tab, position ->
            val customTab = layoutInflater.inflate(R.layout.custom_tab, null)
            val imageView = customTab.findViewById<ImageView>(R.id.tab_image)

            imageView.setImageResource(R.drawable.bg_tab_select)

            tab.customView = customTab

            if (position == binding.vpViewpager2.currentItem) {
                imageView.setImageResource(R.drawable.bg_tab_indicator)
            } else {
                imageView.setImageResource(R.drawable.bg_tab_select)
            }
        }.attach()

        binding.vpViewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                for (i in 0 until binding.tabLayout.tabCount) {
                    val tab = binding.tabLayout.getTabAt(i)
                    val customView = tab?.customView
                    val imageView = customView?.findViewById<ImageView>(R.id.tab_image)
                    if (i == position) {
                        imageView?.setImageResource(R.drawable.bg_tab_indicator)
                    } else {
                        imageView?.setImageResource(R.drawable.bg_tab_select)
                    }
                }
            }
        })
    }
}
