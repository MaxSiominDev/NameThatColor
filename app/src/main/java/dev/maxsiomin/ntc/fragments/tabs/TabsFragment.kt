package dev.maxsiomin.ntc.fragments.tabs

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.ntc.R
import dev.maxsiomin.ntc.databinding.FragmentTabsBinding
import dev.maxsiomin.ntc.fragments.BaseFragment

@AndroidEntryPoint
class TabsFragment : BaseFragment(R.layout.fragment_tabs) {

    private lateinit var binding: FragmentTabsBinding

    override val mRoot get() = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTabsBinding.bind(view)

        with (childFragmentManager.findFragmentById(R.id.tabsContainer) as NavHostFragment) {
            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
        }
    }
}
