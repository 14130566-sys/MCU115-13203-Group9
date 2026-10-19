package com.example.homework7

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.homework7.databinding.FragResultBinding

class ResultFragment : Fragment(R.layout.frag_result) {
    private val viewModel: WorkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragResultBinding.bind(view)

        viewModel.workState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is WorkState.Idle -> binding.tvResultStatus.text = "Idle"
                is WorkState.Preparing -> binding.tvResultStatus.text = "Preparing..."
                is WorkState.Running -> binding.tvResultStatus.text = "Working..."
                is WorkState.Completed -> binding.tvResultStatus.text = "背景工作結束！"
                is WorkState.Canceled -> binding.tvResultStatus.text = "Idle"
            }
        }
    }
}