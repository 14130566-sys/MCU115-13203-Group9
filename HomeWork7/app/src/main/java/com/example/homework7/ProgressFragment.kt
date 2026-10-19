package com.example.homework7

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.homework7.databinding.FragProgressBinding

class ProgressFragment : Fragment(R.layout.frag_progress) {
    private val viewModel: WorkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragProgressBinding.bind(view)

        viewModel.workState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is WorkState.Idle -> {
                    binding.progressBar.progress = 0
                    binding.tvPercent.text = "0%"
                    binding.tvProgressStatus.text = "Idle"
                }
                is WorkState.Preparing -> {
                    binding.progressBar.progress = 0
                    binding.tvPercent.text = "0%"
                    binding.tvProgressStatus.text = "Preparing..."
                }
                is WorkState.Running -> {
                    binding.progressBar.progress = state.progress
                    binding.tvPercent.text = "${state.progress}%"
                    binding.tvProgressStatus.text = "Working..."
                }
                is WorkState.Completed -> {
                    binding.progressBar.progress = 100
                    binding.tvPercent.text = "100%"
                    binding.tvProgressStatus.text = "背景工作結束！"
                }
                is WorkState.Canceled -> {
                    binding.progressBar.progress = 0
                    binding.tvPercent.text = "0%"
                    binding.tvProgressStatus.text = "Canceled"
                }
            }
        }
    }
}