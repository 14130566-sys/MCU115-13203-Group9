package com.example.hm6

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ProgressFragment : Fragment(R.layout.fragment_progress) {
    private val viewModel: WorkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarCircle)
        val tvProgress = view.findViewById<TextView>(R.id.tvProgress)

        viewModel.progressText.observe(viewLifecycleOwner) { text ->
            tvProgress.text = text
        }

        viewModel.progressPercent.observe(viewLifecycleOwner) { percent ->
            progressBar.progress = percent
        }
    }
}