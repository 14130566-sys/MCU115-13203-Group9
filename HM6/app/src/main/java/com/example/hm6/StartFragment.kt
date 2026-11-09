package com.example.hm6

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class StartFragment : Fragment(R.layout.fragment_start) {
    private val viewModel: WorkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnStart = view.findViewById<Button>(R.id.btnStart)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val tvStartStatus = view.findViewById<TextView>(R.id.tvStartStatus)

        btnStart.setOnClickListener { viewModel.startTask() }
        btnCancel.setOnClickListener { viewModel.cancelTask() }

        viewModel.progressText.observe(viewLifecycleOwner) { text ->
            tvStartStatus.text = text
        }
    }
}