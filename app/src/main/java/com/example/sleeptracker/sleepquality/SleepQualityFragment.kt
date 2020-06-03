package com.example.sleeptracker.sleepquality


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
import com.example.sleeptracker.databinding.FragmentSleepQualityBinding

class SleepQualityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepQualityBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_quality, container, false)

        val application = requireNotNull(this.activity).application

        val args = SleepQualityFragmentArgs.fromBundle(arguments!!)
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepQualityViewModelFactory(args.sleepNightKey,dataSource)
        val viewModel = ViewModelProvider(this,viewModelFactory).get(SleepQualityViewModel::class.java)

        binding.sleepQualityViewModel = viewModel

        viewModel.eventNavigate.observe(this, Observer { event->
            if(event==true){
                findNavController().navigate(SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment())
                viewModel.onNavigate()
            }
        })

        return binding.root
    }
}
