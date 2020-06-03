package com.example.sleeptracker.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.sleeptracker.R
import com.example.sleeptracker.database.SleepDatabase
import com.example.sleeptracker.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar

class SleepTrackerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val factoryViewModel = SleepTrackerViewModelFactory(dataSource,application)
        val viewModel = ViewModelProvider(this,factoryViewModel).get(SleepTrackerViewModel::class.java)

        val adapter = SleepNightAdapter()
        binding.sleepList.adapter = adapter

        viewModel.nights.observe(this, Observer {
            it.let {
                adapter.submitList(it) // adapter.data = it
            }
        })

        binding.lifecycleOwner = this
        binding.sleepTrackerViewModel = viewModel

        viewModel.eventNavigate.observe(this, Observer { night ->
            night?.let{
                findNavController().navigate(SleepTrackerFragmentDirections
                    .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
                viewModel.onNavigate()
            }
        })

        viewModel.showSnackbar.observe(this, Observer {
            if(it==true){
                Snackbar.make(
                    activity!!.findViewById(R.id.clear_button),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                viewModel.onShow()
            }
        })

        return binding.root
    }
}
