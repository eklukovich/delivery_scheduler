package com.eklukovich.deliveryscheduler.ui.drivers

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.eklukovich.deliveryscheduler.R
import com.eklukovich.deliveryscheduler.databinding.ListDriversFragmentBinding
import com.eklukovich.deliveryscheduler.ui.drivers.adapter.ListDriversAdapter
import com.eklukovich.deliveryscheduler.ui.drivers.viewmodel.ListDriversEvent
import com.eklukovich.deliveryscheduler.ui.drivers.viewmodel.ListDriversUiState
import com.eklukovich.deliveryscheduler.ui.drivers.viewmodel.ListDriversViewModel
import com.eklukovich.deliveryscheduler.util.viewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ListDriversFragment : Fragment(R.layout.list_drivers_fragment) {

    companion object {
        fun newInstance(): Fragment {
            return ListDriversFragment()
        }
    }

    // Data Management
    private val viewModel: ListDriversViewModel by viewModels()

    // UI Management
    private val binding by viewBinding(ListDriversFragmentBinding::bind)
    private val adapter get() = binding.listDriversRecyclerView.adapter as? ListDriversAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        // Layout Manager
        binding.listDriversRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Decorator
        binding.listDriversRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        // Adapter
        binding.listDriversRecyclerView.adapter = ListDriversAdapter(
            onDriverItemClicked = { driver -> viewModel.onDriverSelected(driver) }
        )
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    updateUiState(uiState)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    handleEvent(event)
                }
            }
        }
    }

    private fun updateUiState(uiState: ListDriversUiState) {
        binding.listDriversProgressBar.isVisible = uiState is ListDriversUiState.Loading

        when (uiState) {
            is ListDriversUiState.Success -> adapter?.submitList(uiState.drivers)
            is ListDriversUiState.Loading -> return
            is ListDriversUiState.Error -> Snackbar.make(binding.root, R.string.deliveries_loading_failed, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun handleEvent(event: ListDriversEvent) {
        when (event) {
            is ListDriversEvent.SchedulingFailed -> Snackbar.make(binding.root, R.string.deliveries_scheduling_failed, Snackbar.LENGTH_SHORT).show()
        }
    }
}