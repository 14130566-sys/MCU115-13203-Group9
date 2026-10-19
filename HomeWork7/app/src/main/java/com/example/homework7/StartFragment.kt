package com.example.homework7

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.homework7.databinding.FragStartBinding

class StartFragment : Fragment(R.layout.frag_start) {
    private val viewModel: WorkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragStartBinding.bind(view)

        binding.btnStart.setOnClickListener {
            viewModel.startWork()
        }

        binding.btnCancel.setOnClickListener {
            viewModel.cancelWork()
        }

        viewModel.workState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is WorkState.Idle -> binding.tvStartStatus.text = "Status: Idle"
                is WorkState.Preparing -> binding.tvStartStatus.text = "Status: Preparing..."
                is WorkState.Running -> binding.tvStartStatus.text = "Status: Working (${state.progress}%)"
                is WorkState.Completed -> binding.tvStartStatus.text = "Status: Completed!"
                is WorkState.Canceled -> binding.tvStartStatus.text = "Status: Canceled"
            }
        }
    }
}